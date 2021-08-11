package com.modusbox.client.exception;

import org.apache.camel.builder.RouteBuilder;

public class RouteExceptionHandlingConfigurer {
    private CamelErrorProcessor errorProcessor = new CamelErrorProcessor();

    public void configureExceptionHandling(RouteBuilder routeBuilder) {

        routeBuilder.onException(Exception.class)
                .handled(true)
                .to("bean:customJsonMessage?method=logJsonMessage('info', ${header.X-CorrelationId}, " +
                        "'Processing the exception at RouteExceptionHandlingConfigurer', null, null, null)")
                .process(errorProcessor)
                .choice()
                .when().simple("${header.errorHandled} == false")
                .toD("direct:extractCustomErrors")
                .end()
                .to("bean:customJsonMessage?method=logJsonMessage('info', ${header.X-CorrelationId}, " +
                        "'Exception has been processed at RouteExceptionHandlingConfigurer', null, null, null)")
        ;
    }
}
