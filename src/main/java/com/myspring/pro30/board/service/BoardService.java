package com.myspring.pro30.board.service;

import java.util.List;
import java.util.Map;

import com.myspring.pro30.board.vo.ArticleVO;

public interface BoardService {

	public List<ArticleVO> listArticles() throws Exception; //목록
	
	public int addNewArticle(Map articleMap) throws Exception; //추가
	
	public ArticleVO viewArticle(int articleNO) throws Exception; //글 상세
	
	public void modArticle(Map articleMap) throws Exception;//글 수정(한개 이미지)
}
