package com.tui.proof.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
	
	public static final String PLACE_HOLDER = "[PLACEHOLDER]";
	
	public static final String FIELD_FIRST_NAME = "firstName";
	public static final String FIELD_LAST_NAME = "lastName";
	public static final String FIELD_TELEPHONE = "telephone";
	public static final String FIELD_EMAIL = "email";
	public static final String FIELD_SEX = "sex";
	
	public static final String FIELD_SEX_VALUE_M = "m";
	public static final String FIELD_SEX_VALUE_MALE = "male";
	public static final String FIELD_SEX_VALUE_F = "f";
	public static final String FIELD_SEX_VALUE_FEMALE = "female";
	public static final String FIELD_SEX_VALUE_OTHER = "other";
	
	public static final String FIELD_DELIVERY_ADDRESS = "deliveryAddress";
	public static final String FIELD_DELIVERY_ADDRESS_STREET = "deliveryAddress.street";
	public static final String FIELD_DELIVERY_ADDRESS_POSTCODE = "deliveryAddress.postcode";
	public static final String FIELD_DELIVERY_ADDRESS_CITY = "deliveryAddress.city";
	public static final String FIELD_DELIVERY_ADDRESS_COUNTRY = "deliveryAddress.country";
	
	
	
	public static String getCurrentDate() {
		return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
	}

}
