package com.nuts.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nuts.domain.BoardAttachVO;
import com.nuts.domain.BoardVO;
import com.nuts.domain.Criteria;

public interface BoardMapper {

	public List<BoardVO> getList();
	
	public List<BoardVO> exdata();

	public List<BoardVO> getListWithPaging(Criteria cri);
	
	public int exregister(BoardVO board); // 운동기록등록
	
	public List<BoardVO> getListWithPaging1(Criteria cri);

	public void insert(BoardVO board);
	
	public int extimeregister(BoardVO board); // 운동 시간 등록

	public Integer insertSelectKey(BoardVO board);

	public BoardVO read(Long bno);

	public int delete(Long bno);

	public int update(BoardVO board);

	public int getTotalCount(Criteria cri);
	
	public int getTotalCount1(Criteria cri); // for exdata

	public void updateReplyCnt(@Param("bno") Long bno, @Param("amount") int amount);
	
	public List<BoardAttachVO> findByBno(Long bno);

}
 