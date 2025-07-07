package com.chartboard.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chartboard.dto.ChartDashboardConnectDto;
import com.chartboard.dto.ChartInfoDto;
import com.chartboard.dto.ChartsIntoDashboardDto;
import com.chartboard.dto.DashboardInfoDto;
import com.chartboard.service.QueryResultTableService;

import jakarta.transaction.Transactional;

@RestController
@CrossOrigin("*")
public class QueryResultTableController {
	@Autowired
	QueryResultTableService queryResultTableService;
	
// -------------------------- 차트 관련 --------------------------
	//	customQuery의 결과 데이터 반환
	@GetMapping("/showResultTableByCustomQuery")
	public List<Map<String, Object>> showResultTableByCustomQuery(@RequestParam String customQuery, @RequestParam Long userId){
		return queryResultTableService.showResultTableByCustomQuery(customQuery, userId);
	}
	/* showResultTableByCustomQuery 반환값 예시
	 * [
		  {
		    "id": 1,
		    "product_name": "Laptop",
		    "category": "Electronics",
		    "price": 999.99,
		    "stock_count": 50
		  },
		  {
		    "id": 2,
		    "product_name": "Smartphone",
		    "category": "Electronics",
		    "price": 799.99,
		    "stock_count": 100
		  },
		  {
		    "id": 3,
		    "product_name": "Tablet",
		    "category": "Electronics",
		    "price": 499.99,
		    "stock_count": 150
		  }, 
		  ...  
		]
	 */
	
//	'차트 저장' 버튼 누르면, chartInfo 테이블에 해당 차트 정보 저장
	@PostMapping("/insertIntoChartInfo")
	public boolean insertIntoChartInfo(@RequestBody ChartInfoDto chartInfoDto) {
		return queryResultTableService.insertIntoChartInfo(chartInfoDto);
	}
	
/*	
//	chartInfo 테이블 전체 select (test 용이라서, 나중에 회원 만들면 지울거임.)
	@GetMapping("/selectAllFromChartInfoTable")
	public List<Map<String, Object>> selectAllFromChartInfoTable(){
		return queryResultTableService.selectAllFromChartInfoTable();
	}
*/
	
	// 회원이 저장한 차트 정보 조회(chart_info table에서 select)
	@GetMapping("/selectFromChartInfoTableByUserId")
	public List<Map<String, Object>> selectFromChartInfoTableByUserId(@RequestParam Long userId){
		return queryResultTableService.selectFromChartInfoTableByUserId(userId);
	}
	/*
	 [
	  {
	    "CHART_CONFIG": "{\"barX\":\"category\",\"barY\":\"price\",\"barColor\":\"product_name\"}",
	    "CHART_TYPE": "bar",
	    "RESULT_TABLE_INFO": "[{\"id\":1,\"product_name\":\"Laptop\",\"category\":\"Electronics\",\"price\":999.99,\"stock_count\":50,\"key\":1},{\"id\":2,\"product_name\":\"Smartphone\",\"category\":\"Electronics\",\"price\":799.99,\"stock_count\":100,\"key\":2},{\"id\":3,\"product_name\":\"Tablet\",\"category\":\"Electronics\",\"price\":499.99,\"stock_count\":150,\"key\":3},{\"id\":4,\"product_name\":\"Smartwatch\",\"category\":\"Electronics\",\"price\":199.99,\"stock_count\":80,\"key\":4},{\"id\":5,\"product_name\":\"Headphones\",\"category\":\"Electronics\",\"price\":89.99,\"stock_count\":200,\"key\":5},{\"id\":6,\"product_name\":\"Desk Chair\",\"category\":\"Furniture\",\"price\":129.99,\"stock_count\":120,\"key\":6},{\"id\":7,\"product_name\":\"Office Desk\",\"category\":\"Furniture\",\"price\":249.99,\"stock_count\":60,\"key\":7},{\"id\":8,\"product_name\":\"Sofa\",\"category\":\"Furniture\",\"price\":499.99,\"stock_count\":30,\"key\":8},{\"id\":9,\"product_name\":\"Dining Table\",\"category\":\"Furniture\",\"price\":699.99,\"stock_count\":15,\"key\":9},{\"id\":10,\"product_name\":\"Coffee Table\",\"category\":\"Furniture\",\"price\":149.99,\"stock_count\":40,\"key\":10},{\"id\":11,\"product_name\":\"T-shirt\",\"category\":\"Clothing\",\"price\":19.99,\"stock_count\":500,\"key\":11},{\"id\":12,\"product_name\":\"Jeans\",\"category\":\"Clothing\",\"price\":39.99,\"stock_count\":350,\"key\":12},{\"id\":13,\"product_name\":\"Jacket\",\"category\":\"Clothing\",\"price\":89.99,\"stock_count\":200,\"key\":13},{\"id\":14,\"product_name\":\"Sneakers\",\"category\":\"Clothing\",\"price\":59.99,\"stock_count\":180,\"key\":14},{\"id\":15,\"product_name\":\"Sweater\",\"category\":\"Clothing\",\"price\":49.99,\"stock_count\":220,\"key\":15},{\"id\":16,\"product_name\":\"Blender\",\"category\":\"Appliances\",\"price\":99.99,\"stock_count\":70,\"key\":16},{\"id\":17,\"product_name\":\"Microwave\",\"category\":\"Appliances\",\"price\":129.99,\"stock_count\":50,\"key\":17},{\"id\":18,\"product_name\":\"Washing Machine\",\"category\":\"Appliances\",\"price\":499.99,\"stock_count\":25,\"key\":18},{\"id\":19,\"product_name\":\"Refrigerator\",\"category\":\"Appliances\",\"price\":799.99,\"stock_count\":40,\"key\":19},{\"id\":20,\"product_name\":\"Air Conditioner\",\"category\":\"Appliances\",\"price\":399.99,\"stock_count\":60,\"key\":20},{\"id\":21,\"product_name\":\"Skirt\",\"category\":\"Clothing\",\"price\":39.99,\"stock_count\":5,\"key\":21}]",
	    "ID": 1
	  },
	  {
	    "CHART_CONFIG": "{\"lineX\":\"month_name\",\"lineY\":\"sales_count\"}",
	    "CHART_TYPE": "line",
	    "RESULT_TABLE_INFO": "[{\"id\":1,\"month_name\":\"Jan\",\"sales_count\":4000,\"key\":1},{\"id\":2,\"month_name\":\"Feb\",\"sales_count\":3000,\"key\":2},{\"id\":3,\"month_name\":\"Mar\",\"sales_count\":5000,\"key\":3},{\"id\":4,\"month_name\":\"Apr\",\"sales_count\":7000,\"key\":4},{\"id\":5,\"month_name\":\"May\",\"sales_count\":6000,\"key\":5}]",
	    "ID": 2
	  }
	]
	 */
	// 차트 삭제 (user_chart_connect 연결 테이블, chart_info 테이블, chart_dashboard_connect 연결 테이블 에서 삭제)
	@DeleteMapping("/deleteChart")
	public boolean deleteChart(@RequestParam Long userId, @RequestParam Long chartId) {
		return queryResultTableService.deleteChart(userId, chartId);
	}
	
// -------------------------- 대시보드 관련 --------------------------
	// 대시보드 추가
	@PostMapping("/insertIntoDashboardInfo")
	public boolean insertIntoDashboardInfo(@RequestBody DashboardInfoDto dashboardInfoDto) {
		return queryResultTableService.insertIntoDashboardInfo(dashboardInfoDto);
	}
	
/*	
//	전체 대시보드 조회 (test 용이라서, 나중에 회원 만들면 지울거임.)
	@GetMapping("/selectAllFromDashboardInfoTable")
	public List<Map<String, Object>> selectAllFromDashboardInfoTable(){
		return queryResultTableService.selectAllFromDashboardInfoTable();
	}
*/
	
	// 회원이 저장한 대시보드 id, 이름 조회(dashboard_info table에서 select)
	@GetMapping("/selectFromDashboardInfoTableByUserId")
	public List<Map<String, Object>> selectFromDashboardInfoTableByUserId(@RequestParam Long userId){
		return queryResultTableService.selectFromDashboardInfoTableByUserId(userId);
	}
	
	//	선택된 대시보드의 차트 정보 가져오기(dashboard_x, dashboard_y, dashboard_w, dashboard_h, CHART_TYPE, RESULT_TABLE_INFO, CHART_CONFIG, chart_name)
	@GetMapping("/selectChartFromDashboard")
	public List<Map<String, Object>> selectChartFromDashboard(@RequestParam Long dashboardInfoId){
		return queryResultTableService.selectChartFromDashboard(dashboardInfoId);
	}
	
	
	//	대시보드에서 차트의 x, ,y, w, h 수정
	@PutMapping("/updateChartDashboardConnect")
	public boolean updateChartDashboardConnect(@RequestBody ChartDashboardConnectDto chartDashboardConnectDto) {
		return queryResultTableService.updateChartDashboardConnect(chartDashboardConnectDto);
	}
	
	// 프론트에서 직접 호출 O - 대시보드에 차트 1개 이상 추가(차트의 id를 순회하며, insertChartIntoDashboard 호출)
	@PostMapping("/insertManyChartsIntoDashboard")
	public boolean insertManyChartsIntoDashboard(@RequestBody ChartsIntoDashboardDto chartsIntoDashboardDto) {
		return queryResultTableService.insertManyChartsIntoDashboard(chartsIntoDashboardDto);
	}
		
	/*
	 [
	 {
	    "chart_name": "차트 이름",
	    "dashboard_y": null,
	    "CHART_CONFIG": "{\"barX\":\"month_name\",\"barY\":\"sales_count\",\"barColor\":\"id\"}",
	    "dashboard_w": null,
	    "dashboard_x": null,
	    "dashboard_h": null,
	    "CHART_TYPE": "bar",
	    "RESULT_TABLE_INFO": "[{\"id\":1,\"month_name\":\"Jan\",\"sales_count\":4000,\"key\":1},{\"id\":2,\"month_name\":\"Feb\",\"sales_count\":3000,\"key\":2},{\"id\":3,\"month_name\":\"Mar\",\"sales_count\":5000,\"key\":3},{\"id\":4,\"month_name\":\"Apr\",\"sales_count\":7000,\"key\":4},{\"id\":5,\"month_name\":\"May\",\"sales_count\":6000,\"key\":5}]",
	    "id": 5
	  },
	  {}, {}, ...
  ]
  
   */
	
	// 대시보드 삭제 (user_dashboard_connect 연결 테이블, dashboard_info 테이블, chart_dashboard_connect 연결 테이블에서 삭제)
	@DeleteMapping("/deleteDashboard")
	public boolean deleteDashboard(@RequestParam Long userId, @RequestParam Long dashboardId) {
		return queryResultTableService.deleteDashboard(userId, dashboardId);
	}

	
}
