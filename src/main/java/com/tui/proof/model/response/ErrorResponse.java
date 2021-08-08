package com.tui.proof.model.response;

import com.tui.proof.model.Entity;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
	
	private Entity entity;
	private ErrorDetails error;

}
