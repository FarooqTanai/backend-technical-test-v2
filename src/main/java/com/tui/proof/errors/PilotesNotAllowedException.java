package com.tui.proof.errors;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PilotesNotAllowedException extends RuntimeException {

	private final List<Integer> allowedPilotes;
}
