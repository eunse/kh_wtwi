package com.wtwi.fin.qnaboard.model.service;

import java.util.List;

import com.wtwi.fin.qnaboard.model.vo.Pagination;
import com.wtwi.fin.qnaboard.model.vo.QnaBoard;
import com.wtwi.fin.qnaboard.model.vo.QnaCategory;
import com.wtwi.fin.qnaboard.model.vo.Search;

public interface QnaBoardService {

	/** 전체 게시글 수 + 게시판 이름 조회
	 * @param pg
	 * @return pagination
	 */
	Pagination getPagination(Pagination pg);
	
	/** 문의게시글 수 조회 (검색)
	 * @param search
	 * @param pg
	 * @return pagination
	 */
	Pagination getPagination(Search search, Pagination pg);

	/** 문의게시판 이름 조회
	 * @param pagination
	 * @return boardList
	 */
	List<QnaBoard> selectBoardList(Pagination pagination);

	/** 문의 게시글 목록
	 * @param search
	 * @param pagination
	 * @return
	 */
	List<QnaBoard> selectBoardList(Search search, Pagination pagination);

	/** 게시글 상세 조회
	 * @param qnaNo
	 * @return
	 */
	QnaBoard selectqnaBoard(int qnaNo);

	/** 카테고리 조회
	 * @return category
	 */
	List<QnaCategory> selectCategory();

	/**
	 * @param board
	 * @return boardNo
	 */
	int insertBoard(QnaBoard board);


}
