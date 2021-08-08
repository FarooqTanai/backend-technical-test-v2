package com.tui.proof.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDetails{
	
	private String code;
	private String message;
	private String time;
}
