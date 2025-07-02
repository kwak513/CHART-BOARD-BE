package com.chartboard.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InsertDbConnectionDto {
	String jdbcUrl;
	String dbUsername;
	String dbPassword;

	Long userId;
}
