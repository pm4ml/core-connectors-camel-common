package com.modusbox.client.enums;

public enum ErrorCode {
    ACCOUNT_NOT_EXIST(3200, 500, "Generic ID error provided by the client."),
    PAYMENT_TYPE_NOT_EXIST(3202, 500, "Provided Payer FSP ID not found."),
    OVER_UNDER_PAYMENT(5200, 500, ""),
    MALFORMED_INPUT(3101, 400, "Format of the parameter is not valid."),
    REQUIRED_FIELD_MISSING(3102, 400, "Generic limit error."),
    TIME_OUT(2004, 504, "Timeout has occurred."),
    INVALID_ACCESS_TOKEN(1001, 403, "Destination of the request failed to be reached."),
    DUPLICATE_REFERENCE_ID(3041, 400, "Duplicate reference ID."),
    CC_LOGICAL_TRANSFORMATION_ERROR(2001, 500, "Generic unexpected exception."),
    ROUNDING_ISSUE(5200, 500, "Generic limit error."),
    GENERIC_DOWNSTREAM_ERROR_PAYEE(5000, 500, "Generic error due to the Payer or Payer FSP."),
    GENERIC_DOWNSTREAM_ERROR_PAYER(4000, 500, "Generic error related to the Payer or Payer FSP.");

    private final Integer statusCode;
    private final Integer httpResponseCode;
    private final String defaultMessage;

    private ErrorCode(Integer statusCode, Integer httpResponseCode, String defaultMessage) {

        this.statusCode = statusCode;
        this.httpResponseCode = httpResponseCode;
        this.defaultMessage = defaultMessage;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public Integer getHttpResponseCode() {
        return httpResponseCode;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
