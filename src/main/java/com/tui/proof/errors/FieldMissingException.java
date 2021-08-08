package com.tui.proof.errors;

import com.tui.proof.model.Entity;
import lombok.Data;

@Data
public class FieldMissingException extends RuntimeException {
	private final String field;
	private final Entity entity;
}
