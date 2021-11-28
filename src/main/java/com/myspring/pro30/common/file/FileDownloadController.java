package com.myspring.pro30.common.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FileDownloadController {
	
	private static final String ARTICLE_IMAGE_REPO = "c:\\board\\article_image";
	
	@RequestMapping("/download.do")
	protected void download(@RequestParam("imageFileName") String imageFileName,
							@RequestParam("articleNO") String articleNO,
							HttpServletResponse response) throws Exception{
		
		OutputStream out = response.getOutputStream();
		
		String downFile = ARTICLE_IMAGE_REPO + "\\" + articleNO + "\\" + imageFileName;
		
		File file = new File(downFile);
	
		//다운로드 준비로 서버에서 클라이언트에게 다운로드 준비를 시키는 부분(다운로드 창을 띄운다)
		response.setHeader("Cache-Controller", "nocache");
		response.addHeader("Content-disposition", "attachment; fileName="+imageFileName);
		
		//실제 다운로드 하는부분
		FileInputStream in = new FileInputStream(file);
		
		byte[] buffer = new byte[1024*8];
		
		while(true) {
			int count = in.read(buffer);
			
			if(count==-1) {
				break;
			}
			out.write(buffer,0,count);
		}
		
		in.close();
		out.close();
	} 
}
