package com.modusbox.client.enums;

public enum ErrorCode {
    GENERIC_ID_NOT_FOUND(3200, 500, "Generic ID error provided by the client."),
    PAYER_FSP_ID_NOT_FOUND(3202, 500, "Provided Payer FSP ID not found."),
    MALFORMED_SYNTAX(3101, 400, "Format of the parameter is not valid."),
    MISSING_MANDATORY_ELEMENT(3102, 400, "Mandatory element in the data model was missing."),
    SERVER_TIMED_OUT(2004, 504, "Timeout has occurred."),
    DESTINATION_COMMUNICATION_ERROR(1001, 403, "Destination of the request failed to be reached."),
    DUPLICATE_REFERENCE_ID(3041, 400, "Duplicate reference ID."),
    INTERNAL_SERVER_ERROR(2001, 500, "Generic unexpected exception."),
    PAYEE_LIMIT_ERROR(5200, 500, "Generic limit error."),
    GENERIC_DOWNSTREAM_ERROR_PAYEE(5000, 500, "Generic error due to the Payer or Payer FSP."),
    GENERIC_DOWNSTREAM_ERROR_PAYER(4000, 500, "Generic error related to the Payer or Payer FSP."),
    PHONE_NUMBER_MISMATCH(3241, 500, "There was an error with your account number and phone number combination. Please contact your DFSP to verify the numbers.");


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

    //
    public static String getErrorResponse(ErrorCode ec, String message){

        if(message == null || message.trim().isEmpty()) {
            message = ec.getDefaultMessage();
        }
        return "{" +
                "\"errorCode\": " + ec.getHttpResponseCode() + ", " +
                "\"errorInformation\": {" +
                "\"statusCode\": " + ec.getStatusCode() + ", " +
                "\"description\": \""+ message + "\"}}";

    }

    public static String getErrorResponse(ErrorCode ec){
        return getErrorResponse(ec, null);
    }
}
