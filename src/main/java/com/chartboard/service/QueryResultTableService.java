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

import org.springframework.stereotype.Service;

import com.chartboard.common.JPAUtil;
import com.chartboard.dto.ChartDashboardConnectDto;
import com.chartboard.dto.ChartInfoDto;
import com.chartboard.dto.ChartIntoDashboardDto;
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
	
	private ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환기

	
	//	customQuery의 결과 데이터 반환
	public List<Map<String, Object>> showResultTableByCustomQuery(String customQuery){
		List resultList = new ArrayList<>();
		
		// DB 연결 - 이 부분은 사용자의 데이터베이스 정보 들어오는 곳. 나중에 로직 바꿀거임.
		String jdbcurl = "";
		String username = "";
		String password = "";
		
		
		
		try {
			Connection connection = DriverManager.getConnection(jdbcurl, username, password);
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

	//	'차트 저장' 버튼 누르면, chartInfo 테이블에 해당 차트 정보 저장
	@Transactional
	public boolean insertIntoChartInfo(ChartInfoDto chartInfoDto) {
		
	
		try {
			
			
			String sql = "INSERT INTO CHART_INFO (CHART_TYPE, RESULT_TABLE_INFO, CHART_CONFIG, CHART_NAME) VALUES (:CHART_TYPE, :RESULT_TABLE_INFO, :CHART_CONFIG, :CHART_NAME)";
			
			// 자바 객체 -> JSON
			String jsonResultTableInfo = objectMapper.writeValueAsString(chartInfoDto.getResultTableInfo());
			String jsonChartConfig = objectMapper.writeValueAsString(chartInfoDto.getChartConfig());
			
			
			Query query = em.createNativeQuery(sql);
			query.setParameter("CHART_TYPE", chartInfoDto.getChartType());
			query.setParameter("RESULT_TABLE_INFO", jsonResultTableInfo);
			query.setParameter("CHART_CONFIG", jsonChartConfig);
			query.setParameter("CHART_NAME", chartInfoDto.getChartName());
			
			query.executeUpdate();
			return true;
			
			
			
		} catch (Exception e) {
			System.out.println("insertIntoChartInfo failed: "+ e.getMessage());
			return false;
		}
		
		
		
	}

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

	// 대시보드 추가
	@Transactional
	public boolean insertIntoDashboardInfo(String dashboardName) {
		try {
			String sql = "INSERT INTO DASHBOARD_INFO (dashboard_name) VALUES (:dashboard_name)";
			
			Query query = em.createNativeQuery(sql);
			query.setParameter("dashboard_name", dashboardName);
			
			query.executeUpdate();
			return true;
			
		} catch (Exception e) {
			System.out.println("insertIntoDashboardInfo failed: "+ e.getMessage());
			return false;
		}
		
		
		
	}
	
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

	//	만들어진 대시보드에 차트들을 추가
	@Transactional
	public boolean insertChartIntoDashboard(ChartIntoDashboardDto chartIntoDashboardDto) {
	
		try {
			
			List<Long> chartInfoIds = chartIntoDashboardDto.getChartInfoIds();
			
			for(Long chartInfoId: chartInfoIds) {
				String sql = "INSERT INTO CHART_DASHBOARD_CONNECT (CHART_INFO_ID, DASHBOARD_INFO_ID) VALUES (:CHART_INFO_ID, :DASHBOARD_INFO_ID)";
				
				Query query = em.createNativeQuery(sql);
				query.setParameter("CHART_INFO_ID", chartInfoId);
				query.setParameter("DASHBOARD_INFO_ID", chartIntoDashboardDto.getDashboardId());
				
				query.executeUpdate();
			}
			
			return true;
			
		} catch (Exception e) {
			System.out.println("insertIntoChartInfo failed: "+ e.getMessage());
			return false;
		}
	}
	
	//	선택된 대시보드의 차트 정보 가져오기(dashboard_x, dashboard_y, dashboard_w, dashboard_h, CHART_TYPE, RESULT_TABLE_INFO, CHART_CONFIG, chart_name)
	public List<Map<String, Object>> selectChartFromDashboard(Long dashboardInfoId){
		
		try {
			String sql = 
					"SELECT con.dashboard_x, con.dashboard_y, con.dashboard_w, con.dashboard_h, ci.id, ci.CHART_TYPE, ci.RESULT_TABLE_INFO, ci.CHART_CONFIG, ci.chart_name "
					+ "FROM chart_dashboard_connect AS con "
					+ "LEFT JOIN chart_info AS ci "
					+ "ON con.chart_info_id = ci.id WHERE dashboard_info_id = :dashboard_info_id";
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
			
				String sql = "UPDATE chart_dashboard_connect SET dashboard_x = :dashboard_x, dashboard_y = :dashboard_y, dashboard_w = :dashboard_w, dashboard_h = :dashboard_h WHERE chart_info_id = :chart_info_id AND dashboard_info_id = :dashboard_info_id";
				
				Query query = em.createNativeQuery(sql);
				query.setParameter("dashboard_x", chartDashboardConnectDto.getDashboardX());
				query.setParameter("dashboard_y", chartDashboardConnectDto.getDashboardY());
				query.setParameter("dashboard_w", chartDashboardConnectDto.getDashboardW());
				query.setParameter("dashboard_h", chartDashboardConnectDto.getDashboardH());
				query.setParameter("chart_info_id", chartDashboardConnectDto.getChartInfoId());
				query.setParameter("dashboard_info_id", chartDashboardConnectDto.getDashboardInfoId());

				
				query.executeUpdate();
			
			
			return true;
			
		} catch (Exception e) {
			System.out.println("updateChartDashboardConnect failed: "+ e.getMessage());
			return false;
		}
	}
	
}
