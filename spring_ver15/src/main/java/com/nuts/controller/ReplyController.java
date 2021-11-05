package com.nuts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nuts.domain.Criteria;
import com.nuts.domain.ReplyPageDTO;
import com.nuts.domain.ReplyVO;
import com.nuts.service.ReplyService;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RequestMapping("/replies/")
@RestController
@Log4j
@AllArgsConstructor
public class ReplyController {
	@Setter(onMethod_ = {@Autowired})
	private ReplyService service;

	@PreAuthorize("isAuthenticated()") // 로그인한 사용자는 모두 댓글 작성 가능
	@PostMapping(value = "/new", consumes = "application/json", produces = { MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<String> create(@RequestBody ReplyVO vo) {

		log.info("ReplyVO: " + vo);

		int insertCount = service.register(vo);

		log.info("Reply INSERT COUNT: " + insertCount);

		return insertCount == 1 ? new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping(value = "/{rno}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<ReplyVO> get(@PathVariable("rno") Long rno) {

		log.info("get: " + rno);

		return new ResponseEntity<>(service.get(rno), HttpStatus.OK);
	}

	@PreAuthorize("principal.username == #vo.replyer") // 댓글 작성자의 경우 권한 부여
	@RequestMapping(method = { RequestMethod.PUT,
			RequestMethod.PATCH }, value = "/{rno}", consumes = "application/json")
	public ResponseEntity<String> modify(@RequestBody ReplyVO vo, @PathVariable("rno") Long rno) {

		log.info("rno: " + rno); // 댓글 수정 기능
		log.info("modify: " + vo);

		return service.modify(vo) == 1 ? new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

	}

	// @DeleteMapping(value = "/{rno}", produces = { MediaType.TEXT_PLAIN_VALUE })
	// public ResponseEntity<String> remove(@PathVariable("rno") Long rno) {
	//
	// log.info("remove: " + rno);
	//
	// return service.remove(rno) == 1 ? new ResponseEntity<>("success",
	// HttpStatus.OK)
	// : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	//
	// }

	@PreAuthorize("principal.username == #vo.replyer")
	@DeleteMapping("/{rno}")
	public ResponseEntity<String> remove(@RequestBody ReplyVO vo, @PathVariable("rno") Long rno) {

		log.info("remove: " + rno); //댓글 삭제 기능

		log.info("replyer: " + vo.getReplyer());

		return service.remove(rno) == 1 ? new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

	}

	// @GetMapping(value = "/pages/{bno}/{page}",
	// produces = {
	// MediaType.APPLICATION_XML_VALUE,
	// MediaType.APPLICATION_JSON_UTF8_VALUE })
	// public ResponseEntity<List<ReplyVO>> getList(
	// @PathVariable("page") int page,
	// @PathVariable("bno") Long bno) {
	//
	//
	// log.info("getList.................");
	// Criteria cri = new Criteria(page,10);
	// log.info(cri);
	//
	// return new ResponseEntity<>(service.getList(cri, bno), HttpStatus.OK);
	// }

	@GetMapping(value = "/pages/{bno}/{page}", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<ReplyPageDTO> getList(@PathVariable("page") int page, @PathVariable("bno") Long bno) {

		Criteria cri = new Criteria(page, 10); // 댓글 페이징

		log.info("get Reply List bno: " + bno);

		log.info("cri:" + cri);

		return new ResponseEntity<>(service.getListPage(cri, bno), HttpStatus.OK);
	}

}
