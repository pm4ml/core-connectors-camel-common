package com.modusbox.client.enums;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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
    PHONE_NUMBER_MISMATCH(3241, 500, "There was an error with your account number and phone number combination. Please contact your DFSP to verify the numbers."),
    ROUNDING_VALUE_ERROR(5241, 500, "Amount is invalid. Please enter the amount that is divisible by XXXX.");

    private final Integer statusCode;
    private final Integer httpResponseCode;
    private final String defaultMessage;
    public static JSONObject errorMessageJsonObject;
    public static String endUserFriendlyMessage = "";
    public static String localeMessage = "";

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

                return "{" +   "\"errorInformation\": {" +
                        "\"statusCode\": " + ErrorCode.GENERIC_DOWNSTREAM_ERROR_PAYER.getStatusCode() + ", " +
                        "\"description\": \"" + ErrorCode.GENERIC_DOWNSTREAM_ERROR_PAYER.getDefaultMessage() + "\", " +
                        "\"descriptionLocale\": \"\"}}";
            }
        }
        String strStatusCode = statusCode;
        String appendedStatusCode = "ml_code_"+statusCode;
        System.out.println("appendedStatusCode: "+appendedStatusCode);

        if (errorMessageJsonObject.containsKey("en_US")) {
            JSONObject enJsonObject = (JSONObject) errorMessageJsonObject.get("en_US");
            if (enJsonObject.containsKey(appendedStatusCode)) {
                endUserFriendlyMessage = (String) enJsonObject.get(appendedStatusCode);
            }
            else {
                strStatusCode = "5000";
                endUserFriendlyMessage = (String) enJsonObject.get("ml_code_5000");
            }
        }

        if (errorMessageJsonObject.containsKey(locale)){
            JSONObject localeJsonObject = (JSONObject) errorMessageJsonObject.get(locale);
            if (localeJsonObject.containsKey(appendedStatusCode)){
                localeMessage = (String) localeJsonObject.get(appendedStatusCode);
                if (strStatusCode != statusCode){
                    strStatusCode = statusCode;
                    endUserFriendlyMessage = "";
                }
            }
            else {
                if (strStatusCode != statusCode)
                {
                    localeMessage = (String) localeJsonObject.get("ml_code_5000");
                }
            }
        }
        return "{" +   "\"errorInformation\": {" +
                "\"statusCode\": " + strStatusCode + ", " +
                "\"description\": \"" + endUserFriendlyMessage + "\", " +
                "\"descriptionLocale\": \"" + localeMessage + "\"}}";
    }

    public static String getErrorResponse(ErrorCode ec){
        return getErrorResponse(ec, null);
    }
}
