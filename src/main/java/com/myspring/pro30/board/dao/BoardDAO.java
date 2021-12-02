package com.myspring.pro30.board.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.myspring.pro30.board.vo.ArticleVO;

public interface BoardDAO {
	
	public List selectAllArticlesList() throws DataAccessException; //목록
	
	public int insertNewArticle(Map article) throws DataAccessException; //글쓰기
	
	public ArticleVO selectArticle(int articleNO) throws DataAccessException; //글 상세
}
