package com.chartboard.dto;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChartIntoDashboardDto {
	Long dashboardId;
	List<Long> chartInfoIds;
}
