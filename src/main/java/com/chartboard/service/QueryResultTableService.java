package com.chartboard.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chartboard.common.JPAUtil;
import com.chartboard.dto.ChartDashboardConnectDto;
import com.chartboard.dto.ChartInfoDto;
import com.chartboard.dto.ChartsIntoDashboardDto;
import com.chartboard.dto.DashboardInfoDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;

@Service
public class QueryResultTableService {
	@PersistenceContext
	EntityManager em;
	@Autowired
	UserService userService;
	
	private ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환기

	//	customQuery의 결과 데이터 반환(회원의 id를 통해, DB 연결 후)
	public List<Map<String, Object>> showResultTableByCustomQuery(String customQuery, Long userId){
		List resultList = new ArrayList<>();
		
		// 1. UserService에서 사용자의 DB 정보 조회하는 selectFromDbConnection 이용하여, jdbc_url, db_username, db_password 가져옴.
		Map<String, Object> userDbConnection =  userService.selectFromDbConnection(userId);
		
		String jdbcUrl = (String) userDbConnection.get("jdbc_url");
		String dbUsername = (String) userDbConnection.get("db_username");
		String dbPassword = (String) userDbConnection.get("db_password");
		
		
		
		try {
			// 2. DB 연결하여 select 쿼리 실행
			Connection connection = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(customQuery);
			
			int columnCount = rs.getMetaData().getColumnCount();
			
			
			while(rs.next()) {
				Map<String, Object> rowMap = new LinkedHashMap<>();
				
				for(int i = 1; i <= columnCount; i++) {
					String columnName = rs.getMetaData().getColumnName(i);
					Object rowData = rs.getObject(i);
					rowMap.put(columnName, rowData);
				}
				resultList.add(rowMap);			
			}
			return resultList;
						
		} catch (SQLException e) {
			System.out.println("showResultTableByCustomQuery 실패" + e.getMessage());
			return new ArrayList<>();
		}
	}

	//	'차트 저장' 버튼 누르면, chart_info, user_chart_connect 테이블에 해당 차트 정보 저장
	@Transactional
	public boolean insertIntoChartInfo(ChartInfoDto chartInfoDto) {
		try {
			
			// 1.  chart_info TABLE에 insert
			String sql1 = "INSERT INTO CHART_INFO (CHART_TYPE, RESULT_TABLE_INFO, CHART_CONFIG, CHART_NAME) VALUES (:CHART_TYPE, :RESULT_TABLE_INFO, :CHART_CONFIG, :CHART_NAME)";
			
			// 자바 객체 -> JSON
			String jsonResultTableInfo = objectMapper.writeValueAsString(chartInfoDto.getResultTableInfo());
			String jsonChartConfig = objectMapper.writeValueAsString(chartInfoDto.getChartConfig());
			
			
			Query query1 = em.createNativeQuery(sql1);
			query1.setParameter("CHART_TYPE", chartInfoDto.getChartType());
			query1.setParameter("RESULT_TABLE_INFO", jsonResultTableInfo);
			query1.setParameter("CHART_CONFIG", jsonChartConfig);
			query1.setParameter("CHART_NAME", chartInfoDto.getChartName());
			
			int insertChartInfo= query1.executeUpdate();
			
			// 2. chart_info TABLE에 방금 insert한 행의 id 값 가져오기
			String sql2 = "SELECT LAST_INSERT_ID()";
					
	        Query query2 = em.createNativeQuery(sql2);
	    
	        Long recentInsertedChartInfoId = ((Number) query2.getSingleResult()).longValue();	//방금 insert한 행의 id

			
			// 3. user_chart_info TABLE에 insert
			String sql3 = "INSERT INTO user_chart_connect (user_id, chart_info_id) VALUES (:user_id, :chart_info_id)";
					
	        Query query3 = em.createNativeQuery(sql3);
	        query3.setParameter("user_id", chartInfoDto.getUserId());
	        query3.setParameter("chart_info_id", recentInsertedChartInfoId);
	        
	        int insertUserChartConnect = query3.executeUpdate();
			
	        if(insertChartInfo == 1 && insertUserChartConnect == 1) {
	        	System.out.println("insertIntoChartInfo 성공");
	
	        	return true;
	        }
			return false;
			
		} catch (Exception e) {
			System.out.println("insertIntoChartInfo failed: "+ e.getMessage());
			return false;
		}
		
	}
/*
	// chartInfo 테이블 전체 select (test 용이라서, 나중에 회원 만들면 지울거임.)
	public List<Map<String, Object>> selectAllFromChartInfoTable(){
		
		try {
			String sql = "SELECT * FROM CHART_INFO";
			Query query = em.createNativeQuery(sql, Tuple.class);
			List<Tuple> rs = query.getResultList();
			
			List<Map<String, Object>> rsToMap = JPAUtil.convertTupleToMap(rs);
			return rsToMap;
			
		} catch(Exception e) {
			System.out.println("selectAllFromChartInfoTable failed: "+ e.getMessage());
			return new ArrayList<>();
		}
	}
*/
	// 회원이 저장한 차트 정보 조회(chart_info table에서 select)
	public List<Map<String, Object>> selectFromChartInfoTableByUserId(Long userId){
		
		try {
			String sql = "SELECT ci.* FROM CHART_INFO ci JOIN user_chart_connect ucc ON ci.id = ucc.chart_info_id WHERE ucc.user_id = :userId";
			Query query = em.createNativeQuery(sql, Tuple.class);
			query.setParameter("userId", userId);
			
			
			List<Tuple> rs = query.getResultList();
			
			List<Map<String, Object>> rsToMap = JPAUtil.convertTupleToMap(rs);
			return rsToMap;
			
		} catch(Exception e) {
			System.out.println("selectFromChartInfoTableByUserId failed: "+ e.getMessage());
			return new ArrayList<>();
		}
	}

	
	
	// 대시보드 추가
	@Transactional
	public boolean insertIntoDashboardInfo(DashboardInfoDto dashboardInfoDto) {
		
		try {
			
			// 1.  dashboard_info TABLE에 insert
			String sql1 = "INSERT INTO DASHBOARD_INFO (dashboard_name) VALUES (:dashboard_name)";
			
			Query query1 = em.createNativeQuery(sql1);
			query1.setParameter("dashboard_name", dashboardInfoDto.getDashboardName());
			
			int insertDashboardInfo= query1.executeUpdate();
			
			// 2. dashboard_info TABLE에 방금 insert한 행의 id 값 가져오기
			String sql2 = "SELECT LAST_INSERT_ID()";
			Query query2 = em.createNativeQuery(sql2);
			
			Long dashboardId = ((Number) query2.getSingleResult()).longValue();
					

			// 3. user_dashboard_connect 연결 TABLE에 insert
			String sql3 = "INSERT INTO user_dashboard_connect (user_id, dashboard_info_id) VALUES (:user_id, :dashboard_info_id)";
					
	        Query query3 = em.createNativeQuery(sql3);
	        query3.setParameter("user_id", dashboardInfoDto.getUserId());
	        query3.setParameter("dashboard_info_id", dashboardId);
	        
	        int insertUserDashboardConnect = query3.executeUpdate();
			
	        if(insertDashboardInfo == 1 && insertUserDashboardConnect == 1) {
	        	System.out.println("insertIntoDashboardInfo 성공");
	
	        	return true;
	        }
			return false;
			
		} catch (Exception e) {
			System.out.println("insertIntoDashboardInfo failed: "+ e.getMessage());
			return false;
		}
	}
	
	
	
/*	
	//	전체 대시보드 조회 (test 용이라서, 나중에 회원 만들면 지울거임.)
	public List<Map<String, Object>> selectAllFromDashboardInfoTable(){
		
		try {
			String sql = "SELECT * FROM DASHBOARD_INFO";
			Query query = em.createNativeQuery(sql, Tuple.class);
			List<Tuple> rs = query.getResultList();
			
			List<Map<String, Object>> rsToMap = JPAUtil.convertTupleToMap(rs);
			return rsToMap;
			
		} catch(Exception e) {
			System.out.println("selectAllFromDashboardInfoTable failed: "+ e.getMessage());
			return new ArrayList<>();
		}
	}
*/
	
	// 회원이 저장한 대시보드 id, 이름 조회(dashboard_info table에서 select)
	public List<Map<String, Object>> selectFromDashboardInfoTableByUserId(Long userId){
		
		try {
			String sql = "SELECT di.* FROM dashboard_info di JOIN user_dashboard_connect udc ON di.id = udc.dashboard_info_id WHERE udc.user_id = :userId";
			Query query = em.createNativeQuery(sql, Tuple.class);
			query.setParameter("userId", userId);
			
			List<Tuple> rs = query.getResultList();
			
			List<Map<String, Object>> rsToMap = JPAUtil.convertTupleToMap(rs);
			return rsToMap;
			
		} catch(Exception e) {
			System.out.println("selectFromDashboardInfoTableByUserId failed: "+ e.getMessage());
			return new ArrayList<>();
		}
	}
	
	
		
	
	//	선택된 대시보드의 차트 정보 가져오기(dashboard_x, dashboard_y, dashboard_w, dashboard_h, CHART_TYPE, RESULT_TABLE_INFO, CHART_CONFIG, chart_name)
	public List<Map<String, Object>> selectChartFromDashboard(Long dashboardInfoId){
		
		try {
			String sql = 
					"SELECT cdc.dashboard_x, cdc.dashboard_y, cdc.dashboard_w, cdc.dashboard_h, ci.id, ci.CHART_TYPE, ci.RESULT_TABLE_INFO, ci.CHART_CONFIG, ci.chart_name "
					+ "FROM chart_dashboard_connect AS cdc "
					+ "JOIN chart_info AS ci "
					+ "ON cdc.chart_info_id = ci.id WHERE cdc.dashboard_info_id = :dashboard_info_id";
			Query query = em.createNativeQuery(sql, Tuple.class);
			query.setParameter("dashboard_info_id", dashboardInfoId);
			
			List<Tuple> rs = query.getResultList();
			
			List<Map<String, Object>> rsToMap = JPAUtil.convertTupleToMap(rs);
			return rsToMap;
			
		} catch(Exception e) {
			System.out.println("selectChartFromDashboard failed: "+ e.getMessage());
			return new ArrayList<>();
		}
	}

	//	대시보드에서 차트의 x, ,y, w, h 수정
	@Transactional
	public boolean updateChartDashboardConnect(ChartDashboardConnectDto chartDashboardConnectDto) {
	
		try {
			
				String sql =
						"UPDATE chart_dashboard_connect "
						+ "SET dashboard_x = :dashboard_x, dashboard_y = :dashboard_y, dashboard_w = :dashboard_w, dashboard_h = :dashboard_h "
						+ "WHERE chart_info_id = :chart_info_id "
						+ "AND dashboard_info_id = :dashboard_info_id";
				
				Query query = em.createNativeQuery(sql);
				query.setParameter("dashboard_x", chartDashboardConnectDto.getDashboardX());
				query.setParameter("dashboard_y", chartDashboardConnectDto.getDashboardY());
				query.setParameter("dashboard_w", chartDashboardConnectDto.getDashboardW());
				query.setParameter("dashboard_h", chartDashboardConnectDto.getDashboardH());
				query.setParameter("chart_info_id", chartDashboardConnectDto.getChartInfoId());
				query.setParameter("dashboard_info_id", chartDashboardConnectDto.getDashboardInfoId());

				
				int chartConnectUpdateNum = query.executeUpdate();
				if(chartConnectUpdateNum == 1) {
		        	System.out.println("updateChartDashboardConnect 성공");
		        	return true;
		        }
	        	
		        return false;
			
			
		} catch (Exception e) {
			System.out.println("updateChartDashboardConnect failed: "+ e.getMessage());
			return false;
		}
	}
	
	
	
	// 프론트에서 직접 호출 X - 대시보드에 차트 1개 추가(차트, 대시보드는 먼저 생성되어있을테니, chart_dashboard_connect 테이블에만 처트와 대시보드 연결 정보 insert하면 됨)
	@Transactional
	public boolean insertChartIntoDashboard(Long chartInfoId, Long dashboardInfoId) {
	
		try {
			String sql = "INSERT INTO chart_dashboard_connect (chart_info_id, dashboard_info_id) VALUES (:chart_info_id, :dashboard_info_id)";
			Query query = em.createNativeQuery(sql, Tuple.class);
			query.setParameter("chart_info_id", chartInfoId);
			query.setParameter("dashboard_info_id", dashboardInfoId);
			
			
			int insertIntoChartDashboardConnectNum = query.executeUpdate();
			
			if(insertIntoChartDashboardConnectNum == 1) {
	        	System.out.println("insertChartIntoDashboard 성공");
	        	return true;
	        }
        	
	        return false;
			
		} catch(Exception e) {
			System.out.println("insertChartIntoDashboard failed: "+ e.getMessage());
			return false;
		}
		
	}
	
	// 프론트에서 직접 호출 O - 대시보드에 차트 1개 이상 추가(차트의 id를 순회하며, insertChartIntoDashboard 호출)
	@Transactional
	public boolean insertManyChartsIntoDashboard(ChartsIntoDashboardDto chartsIntoDashboardDto) {
	
		try {
			
			List<Long> chartInfoIds = chartsIntoDashboardDto.getChartInfoIds();
			Long dashboardInfoId = chartsIntoDashboardDto.getDashboardInfoId();
			
			for(Long chartInfoId: chartInfoIds) {
				if(!insertChartIntoDashboard(chartInfoId, dashboardInfoId)) {
					return false;
				}
			}
			return true;
			
			
		} catch(Exception e) {
			System.out.println("insertManyChartsIntoDashboard failed: "+ e.getMessage());
			return false;
		}
		
	}
	
}
