package com.modusbox.client.exception;

import com.modusbox.log4j2.message.CustomJsonMessage;
import com.modusbox.log4j2.message.CustomJsonMessageImpl;
import org.apache.camel.Exchange;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    CustomJsonMessage customJsonMessage = new CustomJsonMessageImpl();

    @Override
    public Response toResponse(Exception exception) {
        customJsonMessage.logJsonMessage("error", null,"Exception has been processed at GlobalExceptionMapper",
                null, null, exception.getMessage());

        return Response.status(Response.Status.BAD_REQUEST)
                .header(Exchange.HTTP_RESPONSE_CODE, "400")
                .header("Conflict-Reason", exception.getMessage())
                .header("Content-Type", "application/json")
                .entity("{ \"statusCode\": \"3100\"," +
                        "\"message\": \"Bad Request\" }")
                .build();
    }

}
