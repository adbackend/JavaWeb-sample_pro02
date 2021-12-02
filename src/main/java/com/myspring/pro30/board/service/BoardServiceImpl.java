package com.myspring.pro30.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myspring.pro30.board.dao.BoardDAO;
import com.myspring.pro30.board.vo.ArticleVO;

@Service("boardService")
public class BoardServiceImpl implements BoardService{

	@Autowired
	private BoardDAO boardDAO;
	
	@Override
	public List<ArticleVO> listArticles() throws Exception {
		
		List<ArticleVO> articlesList = boardDAO.selectAllArticlesList();
		
		return articlesList;
	}

	//단일 이미지 추가하기
	@Override
	public int addNewArticle(Map articleMap) throws Exception {
		System.out.println("arrNewArticleService단~~~");
		
		return boardDAO.insertNewArticle(articleMap);
	}

	//글 상세(단일 파일)
	@Override
	public ArticleVO viewArticle(int articleNO) throws Exception {
		
//		ArticleVO articleVO = boardDAO.selectArticle(articleNO);
		
		return boardDAO.selectArticle(articleNO);
	}
	
}
