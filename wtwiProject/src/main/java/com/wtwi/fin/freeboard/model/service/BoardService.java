package com.wtwi.fin.freeboard.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.wtwi.fin.freeboard.model.vo.Board;
import com.wtwi.fin.freeboard.model.vo.Category;
import com.wtwi.fin.freeboard.model.vo.Pagination;
import com.wtwi.fin.freeboard.model.vo.Search;

/**
 * @author 세은
 */
public interface BoardService {
	
	/** 전체 게시글 수 조회(1)
	 * @param pg
	 * @return pagination
	 */
	Pagination getPaganation(Pagination pg);

	/** 게시글 목록 조회(2)
	 * @param pagination
	 * @return boardList
	 */
	List<Board> selectBoardList(Pagination pagination);

	/** 게시글 상세 조회(3)
	 * @param boardNo
	 * @return
	 */
	Board selectBoard(int boardNo);

	/** 카테고리 조회(4)
	 * @return category
	 */
	List<Category> selectCategory();

	/** 검색 게시글 수 조회(5)
	 * @param search
	 * @param pg
	 * @return pagination
	 */
	Pagination getPaganation(Search search, Pagination pg);

	/** 검색 게시글 목록 조회(6)
	 * @param search
	 * @param pagination
	 * @return boardList
	 */
	List<Board> selectBoardList(Search search, Pagination pagination);

	/** 이미지 파일을 서버에 저장(7)
	 * @param file
	 * @param webPath
	 * @param savePath
	 * @return fileName
	 */
	String uploadFile(MultipartFile file, String webPath, String savePath);

	/** 게시글 삽입(8)
	 * @param board
	 * @param imgs
	 * @param webPath
	 * @return
	 */
	int insertBoard(Board board, List<String> imgs, String webPath);

}
