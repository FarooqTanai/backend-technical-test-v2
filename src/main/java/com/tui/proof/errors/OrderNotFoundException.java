package com.tui.proof.errors;

import lombok.Data;

@Data
public class OrderNotFoundException extends RuntimeException {

	private final String orderNumber;
}
