package com.myspring.pro30.board.controller;


import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;

import com.myspring.pro30.board.service.BoardService;
import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.member.vo.MemberVO;

@Controller("boardController")
public class BoardControllerImpl implements BoardController{

	private static final String ARTICLE_IMAGE_REPO = "C:\\board\\article_image";
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private ArticleVO articleVO;
	
	@Override
	@RequestMapping(value="/board/listArticles.do", method= {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView listArticles(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String viewName = (String)request.getAttribute("viewName");
		
		List<ArticleVO> articlesList  = boardService.listArticles();
		
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("articlesList",articlesList);
		
		return mav;
	}
	
	//한개 이미지 글쓰기
	@Override
	@ResponseBody
	@RequestMapping(value="/board/addNewArticle.do", method=RequestMethod.POST)
	public ResponseEntity addNewArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {
		
		System.out.println("한개 이미지 글쓰기~~~~~111");
		
		multipartRequest.setCharacterEncoding("utf-8");
		
		Map<String, Object> articleMap = new HashMap<String, Object>(); //글 정보를 저장하기 위한 articleMap생성
		
		Enumeration enu = multipartRequest.getParameterNames(); //id, name 파라미터 값을 불러온다
		
		//글쓰기창에 전송된 글 정보를 Map에 key/value로 저장한다
		while(enu.hasMoreElements()) {
			String name = (String)enu.nextElement(); //id
			String value = multipartRequest.getParameter(name);
			
			articleMap.put(name, value);
		}
		
		String imageFileName = upload(multipartRequest);
		
		HttpSession session = multipartRequest.getSession();
		
		MemberVO memberVO = (MemberVO)session.getAttribute("member");
		
		String id = memberVO.getId(); //세션에 저장된 회원정보로 부터 id를 가져온다
		
		//id,부모글 번호, 이미지 파일이름을 articleMap에 저장한다
		articleMap.put("parentNo", 0);
		articleMap.put("id", id);
		articleMap.put("imageFileName", imageFileName);
		
		String message;
		ResponseEntity resEnt = null;
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		System.out.println("한개 이미지 글쓰기~~~~~222");

		try {
			System.out.println("들어오긴하냐..");
			int articleNO = boardService.addNewArticle(articleMap);
			
			System.out.println("아디클노값--->"+articleNO);
			if(imageFileName!=null && imageFileName.length()!=0) {
				File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
				File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
				
				FileUtils.moveFileToDirectory(srcFile, destDir,true);
			}
			
			message = "<script>";
			message += "alert('새 글을 추가 했습니다.');";
			message += "location.href='"+multipartRequest.getContextPath()+"/board/listArticles.do;'";
			message += "</script>";
			
			resEnt = new ResponseEntity(message,responseHeaders,HttpStatus.CREATED);// 결과가 성공적이였으며 새로운 리소스가 생성
			System.out.println("한개 이미지 글쓰기~~~~~33333");

		}catch (Exception e) {
			System.out.println("한개 이미지 글쓰기~~~~~44444");

			
			File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+imageFileName);
			srcFile.delete();
			
			message = "<script>";
			message += "alert('오류가 발생했습니다. 다시 시도해주세요. ');";
			message += "location.href='"+multipartRequest.getContextPath()+"/board/articleForm.do';";
			message += "</script>";
			
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			e.printStackTrace();
		}
		System.out.println("한개 이미지 글쓰기~~~~~555555");

		return resEnt;
	}
	
	
	//글쓰기 폼
	@RequestMapping(value="/board/*Form.do", method=RequestMethod.GET)
	public ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String viewName = (String)request.getAttribute("viewName");
		System.out.println("뷰네임...?->"+viewName);
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName(viewName);
		
		return mav;
	}
	
	//글 상세
	@Override
	@RequestMapping(value="/board/viewArticle.do", method=RequestMethod.GET)
	public ModelAndView viewArticle(@RequestParam("articleNO") int articleNO, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String viewName = (String)request.getAttribute("viewName");
		
		articleVO = boardService.viewArticle(articleNO);
		
		System.out.println("파일첨부"+articleVO.getImageFileName());
		
		System.out.println("articleVO"+articleVO.getContent());
		
		ModelAndView mav = new ModelAndView(viewName);
		mav.setViewName(viewName);
		mav.addObject("article",articleVO);
				
		return mav;
	}
	
	//글 수정(한개 이미지)
	@Override
	@RequestMapping(value="/board/modArticle.do", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity modArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {
		
		multipartRequest.setCharacterEncoding("utf-8");
		
		Map<String, Object> articleMap = new HashMap<String, Object>();
		
		Enumeration enu = multipartRequest.getParameterNames(); // id, name 파라미터 값을 불러온다
		
		while(enu.hasMoreElements()) {
			
			String name = (String)enu.nextElement(); //id
			System.out.println("name값은? ->" +name);
			String value = multipartRequest.getParameter(name);
			
			articleMap.put(name, value);
		}
		
		String imageFileName = upload(multipartRequest);
		articleMap.put("imageFileName", imageFileName);
		
		String articleNO = (String)articleMap.get("articleNO");
		System.out.println("수정할 글 번호 articleNO....?"+articleNO);
		String message;
		ResponseEntity resEnt = null;
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		
		try {
			boardService.modArticle(articleMap);
			
			if(imageFileName != null && imageFileName.length()!=0) {
				File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
				File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
				
				FileUtils.moveFileToDirectory(srcFile, destDir, true); //새로 첨부한 파일을 폴더로 이동
				
				//기존의 파일을 삭제
				String originalFileName = (String)articleMap.get("originalFileName");
				System.out.println("기준파일 이름....?"+originalFileName);
				File oldFile = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO+"\\"+originalFileName);
				oldFile.delete();
			}
			
			message = "<script>";
			message += "alert('글을 수정했습니다.');";
			message += " location.href='"+multipartRequest.getContextPath()+"/board/viewArticle.do?articleNO="+articleNO+"';";
			message += "</script>";
			
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			
		}catch (Exception e) {
			File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
			srcFile.delete();
			
			message = "<script>";
			message += "alert('오류가 발생했습니다. 다시 시도해 주세요')";
			message += "location.href='"+multipartRequest.getContextPath()+"/board/viewArticle.do?articleNO="+articleNO+"';";
			message += "</script>";
			
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		}
		
		return resEnt;
	}
	
	
	//한개 이미지 업로드하기
	private String upload(MultipartRequest multipartRequest) throws Exception{
		
		System.out.println("한개이미지 업로드~~~~~~11111");
		String imageFileName = null;
		
		Iterator<String> fileNames = multipartRequest.getFileNames(); //파일 이름이 아니라 파라미터 네임(file1, file2, ..)
		
		while(fileNames.hasNext()) {
			
			String fileName = fileNames.next();
			
			MultipartFile mFile = multipartRequest.getFile(fileName); //파일(이름, 타입, 크기)
			
			imageFileName = mFile.getOriginalFilename(); //실제 업로드된 파일 이름
			
			// String ARTICLE_IMAGE_REPO = "C:\\board\\article_image";
			
			File file = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+fileName);
			
			if(mFile.getSize()!=0) { //디렉토리가 존재하는지 검사
				if(!file.exists()) { //파일이 존재하지 않으면
					file.getParentFile().mkdirs(); //경로에 해당하는 디렉토리들 생성
					mFile.transferTo(new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName)); //임시로 저장된 multipartFile을 실제 파일로 전송
					
				}
			}
			
		}
		System.out.println("upload메서드 리턴되는 imageFileName값은..?"+imageFileName);
		return imageFileName;
	}
	
	@Override
	@RequestMapping(value="/board/removeArticle.do", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity removeArticle(@RequestParam("articleNO") int articleNO, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		response.setContentType("text/html; charset=utf-8");
		
		System.out.println("글삭제 articleNO값 넘어오냐?...->"+articleNO);
		String message ;
		
		ResponseEntity resEnt = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		
		try {
			boardService.removeArticle(articleNO);
			
			File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
			FileUtils.deleteDirectory(destDir);
			
			message = "<script>";
			message += " alert('글을 삭제했습니다.');";
			message += " location.href='"+request.getContextPath()+"/board/listArticles.do';";
			message +=" </script>";
			
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			
		}catch (Exception e) {
			
			message = "<script>";
			message += "alert('작업중 오류가 발생 했습니다. 다시 시도해 주세요');";
			message += "location.href='"+request.getContextPath()+"/board/listArticles.do';";
			message += "</script>";
			
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			e.printStackTrace();
		}
		return resEnt;
	}

}











