package com.service.pricing.exception;

public class PricingDaoException extends Exception {

	private static final long serialVersionUID = 7537616283946030765L;

	public PricingDaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public PricingDaoException(String message) {
		super(message);

	}
}
