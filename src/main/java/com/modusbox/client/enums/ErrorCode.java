package com.modusbox.client.enums;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public enum ErrorCode {
    
    COMMUNICATION_ERROR(1000, 500, "Generic communication error."),
    DESTINATION_COMMUNICATION_ERROR(1001, 403, "Destination of the request failed to be reached."),
    GENERIC_SERVER_ERROR(2000, 500, "Generic server error."),
    INTERNAL_SERVER_ERROR(2001, 500, "Generic unexpected exception."),
    NOT_IMPLEMENTED(2002, 501, "Not implemented."),
    SERVICE_UNAVAILABLE(2003, 503, "Service currently unavailable."),
    SERVER_TIMED_OUT(2004, 504, "Timeout has occurred."),
    SERVER_BUSY(2005, 503, "Server is rejecting requests due to overloading. Try again later."),
    GENERIC_CLIENT_ERROR(3000, 500, "Generic client error."),
    UNACCEPTABLE_VERSION(3001, 406, "Unacceptable version requested."),
    UNKNOWN_URI(3002, 400, "Unknown URI."),
    DUPLICATE_REFERENCE_ID(3041, 400, "Duplicate reference ID."),    
    GENERIC_VALIDATION_ERROR(3100, 400, "Generic validation error."),
    MALFORMED_SYNTAX(3101, 400, "Format of the parameter is not valid."),
    MISSING_MANDATORY_ELEMENT(3102, 400, "Mandatory element in the data model was missing."),    
    GENERIC_ID_NOT_FOUND(3200, 500, "Generic ID error provided by the client."),
    DESTINATION_FSP_ERROR(3201, 500, "Destination FSP does not exist or cannot be found."),
    PAYER_FSP_ID_NOT_FOUND(3202, 500, "Provided Payer FSP ID not found."),
    TRANSFER_ID_NOT_FOUND(3208, 404, "Provided Transfer ID not found."),
    PHONE_NUMBER_MISMATCH(3241, 500, "There was an error with your account number and phone number combination. Please contact your DFSP to verify the numbers."),
    INACTIVE_ACCOUNT(3242, 404, "Account is not active."),
    GENERIC_DOWNSTREAM_ERROR_PAYER(4000, 500, "Generic error related to the Payer or Payer FSP."),
    GENERIC_DOWNSTREAM_ERROR_PAYEE(5000, 500, "Generic error due to the Payer or Payer FSP."),    
    PAYEE_LIMIT_ERROR(5200, 500, "Generic limit error."),
    ROUNDING_VALUE_ERROR(5241, 500, "Amount is invalid. Please enter the format specified by the service provider."),
    ADD_PARTY_INFORMATION_ERROR(3003, 500, "Error occurred while adding or updating information regarding a Party."),
    MISSING_MANDATORY_SYNTAX(3103, 400, "Mandatory element in the data model was missing."),
    TOO_MANY_ELEMENT(3104, 400, "Number of elements of an array exceeds the maximum number allowed."),
    INVALID_SIGNATURE(3105, 500, "Some parameters have changed in the message, making the signature invalid. This may indicate that the message may have been modified maliciously."),
    MODIFIED_REQUEST(3106, 500, "Request with the same ID has previously been processed in which the parameters are not the same."),
    MISSING_MANDATORY_EXTENSION_PARAMETER(3107, 400, "Scheme-mandatory extension parameter was missing."),
    PAYEE_FSP_ID_NOT_FOUND(3203, 500, "Provided Payee FSP ID not found."),
    PARTY_NOT_FOUND(3204, 404, "Party with the provided identifier, identifier type, and optional sub id or type was not found."),
    QUOTE_ID_NOT_FOUND(3205, 404, "Provided Quote ID was not found on the server."),
    TRANSACTION_REQUEST_ID_NOT_FOUND(3206, 404, "Provided Transaction Request ID was not found on the server."),
    TRANSACTION_ID_NOT_FOUND(3207, 404, "Provided Transaction ID was not found on the server."),
    BULK_QUOTE_ID_NOT_FOUND(3209, 404, "Provided Bulk Quote ID was not found on the server."),
    BULK_TRANSFER_ID_NOT_FOUND(3210, 404, "Provided Bulk Transfer ID was not found on the server."),
    GENERIC_EXPIRED_ERROR(3300, 500, "Generic expired object error, to be used in order not to disclose information that may be considered private."),
    TRANSACTION_REQUEST_EXPIRED(3301, 500, "Client requested to use a transaction request that has already expired."),
    QUOTE_EXPIRED(3302, 500, "Client requested to use a quote that has already expired."),
    TRANSFER_EXPIRED(3303, 500, "Client requested to use a transfer that has already expired."),
    GENERIC_PAYER_REJECTION(4100, 500, "Payer or Payer FSP rejected the request."),
    PAYER_REJECTED_TRANSACTION_REQUEST(4101, 500, "Payer rejected the transaction request from the Payee."),
    PAYER_FSP_UNSUPPORTED_TRANSACTION_TYPE(4102, 500, "Payer FSP does not support or rejected the requested transaction type."),
    PAYER_UNSUPPORTED_CURRENCY(4103, 500, "Payer does not have an account which supports the requested currency."),
    PAYER_LIMIT_ERROR(4200, 500, "Generic limit error."),
    PAYER_PERMISSION_ERROR(4300, 500, "Generic permission error, the Payer or Payer FSP does not have the access rights to perform the service."),
    GENERIC_PAYER_BLOCKED_ERROR(4400, 500, "Generic Payer blocked error; the Payer is blocked or has failed regulatory screenings."),
    PAYEE_FSP_INSUFFICIENT_LIQUIDITY(5001, 500, "Payee FSP has insufficient liquidity to perform the transfer."),
    GENERIC_PAYEE_REJECTION(5100, 500, "Payee or Payee FSP rejected the request."),
    PAYEE_REJECTED_REQUEST(5101, 500, "Payee does not want to proceed with the financial transaction after receiving a quote."),
    PAYMENT_TYPE_NOT_FOUND(5102, 500, "Payee FSP does not support or rejected the requested transaction type."),
    PAYEE_FSP_REJECTED_QUOTE(5103, 500, "Payee FSP does not want to proceed with the financial transaction after receiving a quote."),
    PAYEE_REJECTED_TRANSACTION(5104, 500, "Payee rejected the financial transaction."),
    PAYEE_FSP_REJECTED_TRANSACTION(5105, 500, "Payee FSP rejected the financial transaction."),
    PAYEE_UNSUPPORTED_CURRENCY(5106, 500, "Payee does not have an account that supports the requested currency."),
    PAYEE_PERMISSION_ERROR(5300, 500, "Generic permission error, the Payee or Payee FSP does not have the access rights to perform the service."),
    GENERIC_PAYEE_BLOCKED_ERROR(5400, 500, "Generic Payee Blocked error, the Payee is blocked or has failed regulatory screenings.");

    private final Integer statusCode;
    private final Integer httpResponseCode;
    private final String defaultMessage;
    public static JSONObject errorMessageJsonObject;
    public static String endUserFriendlyMessage = "";
    public static String localeMessage = "";
    public static String defaultLanguage = "en_US";
    public static String defaultCode = "ml_code_5000";

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
                "\"description\": \"" + message + "\"}}";

    }

    public static String getMojaloopErrorResponseByStatusCode(String statusCode, String locale) {

        if (errorMessageJsonObject == null) {
            try {
                InputStream inputStream = ErrorCode.class.getClassLoader().getResourceAsStream("error_message_language.json");
                System.out.println("InputStream of Json Languagae File: " + inputStream.toString());
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader bufferReader = new BufferedReader(inputStreamReader);

                JSONParser jsonParser = new JSONParser();
                JSONObject parsedObject = (JSONObject) jsonParser.parse(bufferReader);

                errorMessageJsonObject = (JSONObject) parsedObject.get("language");
                System.out.println("errorMessageJsonObject length in Locale Json File:" + errorMessageJsonObject.size());
                bufferReader.close();

            } catch (Exception e) {
                System.out.println("Exception in getting locale error message.");
                System.out.println("errorMessageJsonObject in catch:" + errorMessageJsonObject);

                return "{" + "\"errorInformation\": {" +
                        "\"statusCode\": " + ErrorCode.GENERIC_DOWNSTREAM_ERROR_PAYER.getStatusCode() + ", " +
                        "\"description\": \"" + ErrorCode.GENERIC_DOWNSTREAM_ERROR_PAYER.getDefaultMessage() + "\", " +
                        "\"descriptionLocale\": \"\"}}";
            }
        }
        String strStatusCode = statusCode;
        String appendedStatusCode = "ml_code_" + statusCode;
        System.out.println("appendedStatusCode: " + appendedStatusCode);

        if (errorMessageJsonObject.containsKey(defaultLanguage)) {
            JSONObject enJsonObject = (JSONObject) errorMessageJsonObject.get(defaultLanguage);
            if (enJsonObject.containsKey(appendedStatusCode)) {
                endUserFriendlyMessage = (String) enJsonObject.get(appendedStatusCode);
            } else {
                strStatusCode = "5000";
                endUserFriendlyMessage = (String) enJsonObject.get(defaultCode);
            }
        }

        if (errorMessageJsonObject.containsKey(locale) && !locale.equals(null)) {
            JSONObject localeJsonObject = (JSONObject) errorMessageJsonObject.get(locale);
            if (localeJsonObject.containsKey(appendedStatusCode)) {
                localeMessage = (String) localeJsonObject.get(appendedStatusCode);
                if (strStatusCode != statusCode) {
                    strStatusCode = statusCode;
                    endUserFriendlyMessage = "";
                }
            } else {
                if (strStatusCode != statusCode) {
                    localeMessage = (String) localeJsonObject.get(defaultCode);
                } else {
                    localeMessage = endUserFriendlyMessage;
                }
            }
        } else {
            localeMessage = endUserFriendlyMessage;
        }
        return "{" + "\"errorInformation\": {" +
                "\"statusCode\": " + strStatusCode + ", " +
                "\"description\": \"" + endUserFriendlyMessage + "\", " +
                "\"descriptionLocale\": \"" + localeMessage + "\"}}";
    }

    public static String getErrorResponse(ErrorCode ec){
        return getErrorResponse(ec, null);
    }
}
