package com.tui.proof.errors;

import lombok.Data;

@Data
public class ClientNotFoundException extends RuntimeException {

	private final String clientId;
}
