package com.tui.proof.utils;

import static com.tui.proof.utils.Util.*;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber.CountryCodeSource;
import com.tui.proof.errors.EmailNotValidException;
import com.tui.proof.errors.FieldMissingException;
import com.tui.proof.errors.SexNotValidException;
import com.tui.proof.errors.TelephoneNotValidException;
import com.tui.proof.model.Address;
import com.tui.proof.model.Entity;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.validator.routines.EmailValidator;

@Log4j2
public class Validator {

	
	public static String validateSex(String sex, Entity entity) {
		validateField(FIELD_SEX, sex, entity);
		if (sex.equalsIgnoreCase(FIELD_SEX_VALUE_M) || sex.equalsIgnoreCase(FIELD_SEX_VALUE_MALE)) {
			return FIELD_SEX_VALUE_MALE;
		} else if (sex.equalsIgnoreCase(FIELD_SEX_VALUE_F) || sex.equalsIgnoreCase(FIELD_SEX_VALUE_FEMALE)) {
			return FIELD_SEX_VALUE_FEMALE;
		} else if (sex.equalsIgnoreCase(FIELD_SEX_VALUE_OTHER)) {
			return FIELD_SEX_VALUE_OTHER;
		} else {
			log.error("Sex: {} not valid", sex);
			throw new SexNotValidException();
		}
	}

	public static void validateTelephone(String telephone, Entity entity) {
		validateField(FIELD_TELEPHONE, telephone, entity);
		PhoneNumberUtil numberUtil = PhoneNumberUtil.getInstance();
		try {
			PhoneNumber phone = numberUtil.parse(telephone, CountryCodeSource.UNSPECIFIED.name());
			if (!numberUtil.isValidNumber(phone)) {
				log.error("Telephone: {} not valid", phone);
				throw new TelephoneNotValidException();
			}
		} catch (NumberParseException e) {
			log.error("Telephone {} not valid, error: {}", telephone, e);
			throw new TelephoneNotValidException();
		}
	}

	public static void validateField(String fieldName, Object value, Entity entity) {
		if (value == null) {
			log.error("Field: {} is missing", fieldName);
			throw new FieldMissingException(fieldName, entity);
		}
		if(value instanceof String && value.toString().trim().isEmpty()) {
			log.error("Field: {} is empty", fieldName);
			throw new FieldMissingException(fieldName, entity);
		}
	}

	public static void validateEmail(String email, Entity entity) {
		validateField(FIELD_EMAIL, email, entity);
		if (!EmailValidator.getInstance(true).isValid(email)) {
			log.error("Email: {} not valid", email);
			throw new EmailNotValidException();
		}
	}

	public static void validateDeliverAddress(Address deliveryAddress, Entity entity) {
		validateField(FIELD_DELIVERY_ADDRESS, deliveryAddress, entity);
		validateField(FIELD_DELIVERY_ADDRESS_STREET, deliveryAddress.getStreet(), entity);
		validateField(FIELD_DELIVERY_ADDRESS_POSTCODE, deliveryAddress.getPostcode(), entity);
		validateField(FIELD_DELIVERY_ADDRESS_CITY, deliveryAddress.getCity(), entity);
		validateField(FIELD_DELIVERY_ADDRESS_COUNTRY, deliveryAddress.getCountry(), entity);
	}
}
