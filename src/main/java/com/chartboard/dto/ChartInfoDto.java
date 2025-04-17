package com.chartboard.dto;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChartInfoDto {
	String chartType;
	List<Map<String, Object>> resultTableInfo;
	Map<String, Object> chartConfig;
	String chartName;
}
