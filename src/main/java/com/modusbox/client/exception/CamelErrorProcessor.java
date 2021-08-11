package com.modusbox.client.exception;

import com.modusbox.log4j2.message.CustomJsonMessage;
import com.modusbox.log4j2.message.CustomJsonMessageImpl;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.RuntimeExpressionException;
import org.apache.camel.component.bean.validator.BeanValidationException;
import org.apache.camel.support.processor.validation.SchemaValidationException;
import org.springframework.stereotype.Component;

import java.net.SocketTimeoutException;

@Component("errorProcessor")
public class CamelErrorProcessor implements Processor {

    CustomJsonMessage customJsonMessage = new CustomJsonMessageImpl();

    @Override
    public void process(Exchange exchange) {

        boolean errorHandled = true;
        String reasonText = "{ \"statusCode\": \"2001\"," +
                                "\"message\": \"Unknown\" }";
        int httpResponseCode = 500;
        String conflictReason = "";

        // The exception may be in 1 of 2 places
        Exception exception = exchange.getException();
        if (exception == null) {
            exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        }

        if (exception != null) {
            conflictReason = exception.getMessage();
            if (exception instanceof BeanValidationException) {
                // Bad Request
                httpResponseCode = 400;
                reasonText = "{ \"statusCode\": \"3100\"," +
                                "\"message\": \"Bad Request\" }";
            } else if (exception instanceof SocketTimeoutException) {
                httpResponseCode = 408;
                reasonText = " { \"statusCode\": \"1001\"," +
                                "\"message\": \"Time Out\" }";
            } else if (exception instanceof SchemaValidationException) {
                SchemaValidationException e = (SchemaValidationException) exception;
                reasonText = " { \"statusCode\": \"3100\"," +
                                "\"message\": \"" + e.getErrors().get(0).getMessage() + "\"}";
            } else if (exception instanceof IllegalArgumentException) {
                IllegalArgumentException e = (IllegalArgumentException) exception;
                if (e.getMessage().contains("Problem executing map")) {
                    reasonText = " { \"statusCode\": \"2001\"," +
                                    "\"message\": \"Datasonnet mapping failed:" + e.getMessage()
                                                                                    .replace("\n", "\\n")
                                                                                    .replace("\r", "\\r")
                                                                                    .replace("\t", "\\t")
                                                                                    .replace("\"", "\\\"") + "\"}";
                }
            } else if (exception instanceof RuntimeExpressionException) {
                RuntimeExpressionException e = (RuntimeExpressionException) exception;
                reasonText = " { \"statusCode\": \"2001\"," +
                                "\"message\": \"Datasonnet mapping failed:" + e.getCause().getMessage()
                                                                                .replace("\n", "\\n")
                                                                                .replace("\r", "\\r")
                                                                                .replace("\t", "\\t")
                                                                                .replace("\"", "\\\"") + "\"}";
            } else {
                errorHandled = false;
                exchange.getMessage().setHeader("errorHandled", errorHandled);
                customJsonMessage.logJsonMessage("error", String.valueOf(exchange.getIn().getHeader("X-CorrelationId")),
                                                    "Processing the exception at CamelErrorProcessor", null, null,
                                                    "Redirecting the exception to Core Connector");
            }
            if(errorHandled) {
                customJsonMessage.logJsonMessage("error", String.valueOf(exchange.getIn().getHeader("X-CorrelationId")),
                                                    "Processing the exception at CamelErrorProcessor", null, null,
                                                        exception.getMessage());
            }
        }

        if(errorHandled) {
            exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, httpResponseCode);
            exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, "application/json");
            exchange.getMessage().setHeader("Conflict-Reason", conflictReason);
            exchange.getMessage().setBody(reasonText);
        }

    }
}
