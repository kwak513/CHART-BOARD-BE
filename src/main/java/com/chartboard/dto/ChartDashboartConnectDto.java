package com.chartboard.dto;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChartDashboartConnectDto {
	Long chartInfoId;
	Long dashboardInfoId;
	int dashboardX;
	int dashboardY;
	int dashboardW;
	int dashboardH;
	
}
