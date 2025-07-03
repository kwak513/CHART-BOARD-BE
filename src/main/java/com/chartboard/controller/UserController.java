package com.chartboard.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chartboard.dto.InsertDbConnectionDto;
import com.chartboard.dto.UserLoginDto;
import com.chartboard.dto.UserRegisterDto;
import com.chartboard.service.UserService;

@RestController
@CrossOrigin("*")
public class UserController {
	@Autowired
	UserService userService;

	//------------------------ 회원 관련 (USER Table) ------------------------	
	
	// 회원가입
	@PostMapping("/userRegister")
	public boolean userRegister(@RequestBody UserRegisterDto userRegisterDto) {
		return userService.userRegister(userRegisterDto);
	}
	
	// 로그인	
	@PostMapping("/userLogin")
	public Long userLogin(@RequestBody UserLoginDto userLoginDto) {
		return userService.userLogin(userLoginDto);
	}
	
	// 사용자의 DB 정보 추가 (1. DB_CONNECTION TABLE, 2. USER와 DB_CONNECTION의 연결 테이블인 USER_DB_CONNECT에 INSERT)
	@PostMapping("/insertIntoDbConnection")
	public boolean insertIntoDbConnection(@RequestBody InsertDbConnectionDto insertDbConnectionDto) {
		return userService.insertIntoDbConnection(insertDbConnectionDto);
	}
	
	// 사용자의 DB 정보 조회 (로그인 때 프론트로 보낸 user table의 id를 이용하여, db_connection 테이블에서 조회)
	@GetMapping("/selectFromDbConnection")
	public Map<String, Object> selectFromDbConnection(@RequestParam Long userId){
		return userService.selectFromDbConnection(userId);
	}
	
}
