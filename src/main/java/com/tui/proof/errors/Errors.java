package com.tui.proof.errors;

import static com.tui.proof.utils.Util.*;

public class Errors {

	public static final String GENERIC_ERROR_CODE = "0001";
	public static final String GENERIC_ERROR_MESSAGE = "Generic error";
	
	public static final String CLIENTS_NOT_FOUND_CODE= "0002";
	public static final String CLIENTS_NOT_FOUND_MESSAGE = "Clients not found";
	
	public static final String CLIENT_NOT_FOUND_CODE= "0003";
	public static final String CLIENT_NOT_FOUND_MESSAGE = "Client with id '" + PLACE_HOLDER + "' not found";
	
	public static final String EMAIL_NOT_VALID_CODE = "0004";
	public static final String EMAIL_NOT_VALID_MESSAGE = "Email address is not valid";
	
	public static final String TELEPHONE_NOT_VALID_CODE = "0005";
	public static final String TELEPHONE_NOT_VALID_MESSAGE = "Telephone number is not valid";
	
	public static final String SEX_NOT_VALID_CODE = "0006";
	public static final String SEX_NOT_VALID_MESSAGE = "Sex is not valid";
	
	public static final String PILOTES_NOT_ALLOWED_CODE= "0007";
	public static final String PILOTES_NOT_ALLOWED_MESSAGE = "Pilotes not allowd, pilotes must be one of these: " + PLACE_HOLDER;
	
	public static final String ORDER_NOT_FOUND_CODE = "0008";
	public static final String ORDER_NOT_FOUND_MESSAGE = "Order with orderNumber '" + PLACE_HOLDER + "' not found";
	
	public static final String ORDER_NOT_UPDATABLE_CODE = "0009";
	public static final String ORDER_NOT_UPDATABLE_MESSAGE = "Order cannot be updated";
	
	public static final String ORDER_TOTAL_NOT_VALID_CODE = "0010";
	public static final String ORDER_TOTAL_NOT_VALID_MESSAGE = "Order total is not valid";
	
	public static final String FIELD_MISSING_CODE = "0011";
	public static final String FIELD_MISSING_MESSAGE = "Field '" + PLACE_HOLDER + "' is required";
	
	
	
	
}
