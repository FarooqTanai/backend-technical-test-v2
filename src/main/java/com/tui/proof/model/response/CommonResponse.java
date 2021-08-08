package com.tui.proof.model.response;

import com.tui.proof.model.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonResponse<T>{
	
	private Entity entity;
	private T data;

}
