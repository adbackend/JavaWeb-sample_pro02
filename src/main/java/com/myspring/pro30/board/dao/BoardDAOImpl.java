package com.myspring.pro30.board.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.pro30.board.vo.ArticleVO;

@Repository("boardDAO")
public class BoardDAOImpl implements BoardDAO{

	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public List selectAllArticlesList() throws DataAccessException {
		List<ArticleVO> articleList = sqlSession.selectList("mapper.board.selectAllArticlesList");
		return articleList;
	}

	
	//글쓰기
	@Override
	public int insertNewArticle(Map articleMap) throws DataAccessException {
		
		System.out.println("DAOImpl단 - insertNewArticle");
		
		String aa = (String)articleMap.get("id");
		System.out.println("아이디 받아오냐 "+aa);
		
		int articleNO = selectNewArticleNO();
		
		System.out.println("DAOImpl단 articleNO 값-->"+articleNO);
		
		articleMap.put("articleNO", articleNO);
		
		sqlSession.insert("mapper.board.insertNewArticle", articleMap);
		
		return articleNO;
	}
	
	private int selectNewArticleNO() throws DataAccessException{
		
		return sqlSession.selectOne("mapper.board.selectNewArticleNO");
	}
	
	//글상세(단일 파일)
	@Override
	public ArticleVO selectArticle(int articleNO) throws DataAccessException {
		
		
		return sqlSession.selectOne("mapper.board.selectArticle",articleNO);
	}
	
	//글 수정(한개 이미지)
	@Override
	public void updateArticle(Map articleMap) throws DataAccessException {

		sqlSession.update("mapper.board.updateArticle",articleMap);
	}
	
	//글 삭제
	@Override
	public void deleteArticle(int articleNO) throws DataAccessException {
		sqlSession.delete("mapper.board.deleteArticle",articleNO);
	}
	

}
