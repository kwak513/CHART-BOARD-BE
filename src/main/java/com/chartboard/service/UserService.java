package com.chartboard.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.chartboard.common.JPAUtil;
import com.chartboard.dto.InsertDbConnectionDto;
import com.chartboard.dto.UserLoginDto;
import com.chartboard.dto.UserRegisterDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
@Service
public class UserService {

	@PersistenceContext
	EntityManager em;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	//------------------------ 회원 관련 (USER Table) ------------------------	
		
	// 회원가입	
	@Transactional
	public boolean userRegister(UserRegisterDto userRegisterDto){
		
		try {
			// 이미 가입된 회원 있는지 확인
			String sql = "SELECT COUNT(*) FROM user WHERE username = :username";
					

	        Query query = em.createNativeQuery(sql);
	        query.setParameter("username", userRegisterDto.getUsername());
	        
	        Long rs = (Long) query.getSingleResult();
	        
	        if(rs > 0) {
	        	System.out.println("이미 존재하는 회원입니다. 다른 아이디를 입력하세요.");
	        	return false;
	        }
			
	        else {
	        	String hashedPassword = passwordEncoder.encode(userRegisterDto.getPassword());

                String sql2 = "INSERT INTO user (username, password, full_name) VALUES (:username, :password, :full_name)";
                Query query2 = em.createNativeQuery(sql2);
                query2.setParameter("username", userRegisterDto.getUsername());
                query2.setParameter("password", hashedPassword); // 해싱된 비밀번호 저장
                query2.setParameter("full_name", userRegisterDto.getFullName());

                int registerNum = query2.executeUpdate();
		        
		        if(registerNum == 1) {
		        	System.out.println("회원가입 성공");
		        	return true;
		        }
	        	
		        return false;
	        }
			
		} catch(Exception e) {
			System.out.println("userRegister failed: "+ e.getMessage());
			return false;
		}
	}
	
	// 로그인	
	public Long userLogin(UserLoginDto userLoginDto){
		// 유효성 체크
	    if (userLoginDto.getUsername() == null || userLoginDto.getUsername().trim().isEmpty()) {
	        return -1L;
	    }
	    if (userLoginDto.getPassword() == null || userLoginDto.getPassword().trim().isEmpty()) {
	        return -1L;
	    }
		
		try {
			// 이미 가입된 회원 있는지 확인
			String sql = "SELECT id, username, password FROM user WHERE username = :username";
					
	        Query query = em.createNativeQuery(sql, Tuple.class);
	        query.setParameter("username", userLoginDto.getUsername());
	        
	        Tuple rs = (Tuple) query.getSingleResult();
	        Map<String, Object> rsToMap = JPAUtil.convertTupleToMap(rs);
			
	        Long id = ((Number) rsToMap.get("id")).longValue();
	        String usernameResult = (String) rsToMap.get("username");
	        String passwordResult = (String) rsToMap.get("password");
	        
	        // 비밀번호 확인 (저장된 해시값과 입력된 비밀번호 비교)
            if (passwordEncoder.matches(userLoginDto.getPassword(), passwordResult)) {
                System.out.println("로그인 성공: " + usernameResult);
                return id;
            } else {
                System.out.println("존재하지 않는 회원입니다. ");
                return -1L;
            }
	
		} catch(Exception e) {
			System.out.println("userLogin failed: "+ e.getMessage());
			return -1L;
		}
	}
	
	// 사용자의 DB 정보 추가 (1. DB_CONNECTION TABLE, 2. USER와 DB_CONNECTION의 연결 테이블인 USER_DB_CONNECT에 INSERT)
	@Transactional
	public boolean insertIntoDbConnection(InsertDbConnectionDto insertDbConnectionDto){
		
		try {
			// 1. DB_CONNECTION TABLE에 insert
			String sql1 = "INSERT INTO db_connection (jdbc_url, db_username, db_password) VALUES (:jdbc_url, :db_username, :db_password)";
					
	        Query query1 = em.createNativeQuery(sql1);
	        query1.setParameter("jdbc_url", insertDbConnectionDto.getJdbcUrl());
	        query1.setParameter("db_username", insertDbConnectionDto.getDbUsername());
	        query1.setParameter("db_password", insertDbConnectionDto.getDbPassword());
	        
	        int insertConnectTableNum = query1.executeUpdate();
	        
	        // 2. DB_CONNECTION TABLE에 방금 insert한 행의 id 값 가져오기
			String sql2 = "SELECT LAST_INSERT_ID()";
					
	        Query query2 = em.createNativeQuery(sql2);
	        
	        Long recentInsertedDbConnectionId = ((Number) query2.getSingleResult()).longValue();	//방금 insert한 행의 id
	        
	        
	     // 3. USER_DB_CONNECT TABLE에 insert
			String sql3 = "INSERT INTO user_db_connect (user_id, db_connection_id) VALUES (:user_id, :db_connection_id)";
					
	        Query query3 = em.createNativeQuery(sql3);
	        query3.setParameter("user_id", insertDbConnectionDto.getUserId());
	        query3.setParameter("db_connection_id", recentInsertedDbConnectionId);
	        
	        int insertUserDbConnect = query3.executeUpdate();
	        
	        
	        if(insertConnectTableNum == 1 && insertUserDbConnect == 1) {
	        	System.out.println("insertIntoDbConnection 성공");
	
	        	return true;
	        }
	        
	        System.out.println("insertIntoDbConnection 실패");
	        return false;
	        
		} catch(Exception e) {
			System.out.println("insertIntoDbConnection failed: "+ e.getMessage());
			return false;
		}
	}
	
	// 사용자의 DB 정보 조회 (로그인 때 프론트로 보낸 user table의 id를 이용하여, db_connection 테이블에서 조회)
	public Map<String, Object> selectFromDbConnection(Long userId){
		
		try {
			String sql = "SELECT dc.jdbc_url, dc.db_username, dc.db_password FROM db_connection dc "
						+ "JOIN  user_db_connect udc "
						+ "ON dc.id = udc.db_connection_id "
						+ "WHERE udc.user_id = :userId ";
			Query query = em.createNativeQuery(sql, Tuple.class);
			query.setParameter("userId", userId);
		
			Tuple rs = (Tuple) query.getSingleResult();
			
			Map<String, Object> rsToMap = JPAUtil.convertTupleToMap(rs);
			return rsToMap;
			
		} catch(Exception e) {
			System.out.println("selectFromDbConnection failed: "+ e.getMessage());
			return new HashMap<>();
		}
	}
	

}
