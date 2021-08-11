# core-connectors-camel-common

This project has below implementations,

1. Handles all the global errors/exceptions that gets raised in pm4l-core-connector.
2. Implements HealthRouter for GET /health endpoint.

## How to Use

1. Build the project to generate the jar.
    ```
    mvn clean package
    ```
2. Use this jar as dependency in pm4ml-core-connector.
    ```
    <dependency>
        <groupId>com.modus.pm4ml.core.connectors.common</groupId>
        <artifactId>core-connectors-camel-common</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
    ```
3. In the core connector, implement the route for "direct:extractCustomErrors".
    
    Below is the sample implementation of this route, in core connector.
    
    Declare the route for, "direct:extractCustomErrors"
    
    ```
    import com.modusbox.client.processor.CustomErrorProcessor;
    import org.apache.camel.builder.RouteBuilder;
    
    public final class CustomErrorRouter extends RouteBuilder {
        private CustomErrorProcessor customErrorProcessor = new CustomErrorProcessor();
    
        public void configure() {
    
            from("direct:extractCustomErrors")
                .process(customErrorProcessor)
            ;
        }
    }
    ```
    Add this router to the Camel Context
    
    ```
    <bean id="customErrorRouter" class="com.modusbox.client.router.CustomErrorRouter" />
   
    <camel:camelContext>
        ....
        ....
        <camel:routeBuilder ref="customErrorRouter"/>
    </camel:camelContext>
   
   or you can load whole package into the camel context like below,
   
   <camel:camelContext>
       <camel:package>com.modusbox.client.router</camel:package>
   </camel:camelContext>
    ```
    Implement the CustomErrorProcessor to handle Core Connector specific errors.
   
    ```
    import com.modusbox.log4j2.message.CustomJsonMessage;
    import com.modusbox.log4j2.message.CustomJsonMessageImpl;
    import org.apache.camel.Exchange;
    import org.apache.camel.Processor;
    import org.apache.camel.http.base.HttpOperationFailedException;
    import org.json.JSONObject;
    import org.springframework.stereotype.Component;
    
    @Component("customErrorProcessor")
    public class CustomErrorProcessor implements Processor {
    
        CustomJsonMessage customJsonMessage = new CustomJsonMessageImpl();
    
        @Override
        public void process(Exchange exchange) throws Exception {
    
            String reasonText = "{ \"statusCode\": \"5000\"," +
                                    "\"message\": \"Unknown\" }";
            String statusCode = "5000";
            int httpResponseCode = 500;
    
            // The exception may be in 1 of 2 places
            Exception exception = exchange.getException();
            if (exception == null) {
                exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
            }
    
            if (exception != null) {
                if (exception instanceof HttpOperationFailedException) {
                    HttpOperationFailedException e = (HttpOperationFailedException) exception;
                    String errorDescription = "Downstream API failed.";
                    try {
                        if (null != e.getResponseBody()) {
                            /* Below if block needs to be changed as per the error object structure specific to 
                                CBS back end API that is being integrated in Core Connector. */
                            JSONObject respObject = new JSONObject(e.getResponseBody());
                            if (respObject.has("returnStatus")) {
                                statusCode = String.valueOf(respObject.getInt("returnCode"));
                                errorDescription = respObject.getString("returnStatus");
                            }
                        }
                    } finally {
                        reasonText = "{ \"statusCode\": \"" + statusCode + "\"," +
                                        "\"message\": \"" + errorDescription + "\"} ";
                    }
                }
                customJsonMessage.logJsonMessage("error", String.valueOf(exchange.getIn().getHeader("X-CorrelationId")),
                                                    "Processing the exception at CustomErrorProcessor", null, null,
                                                        exception.getMessage());
            }
    
            exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, httpResponseCode);
            exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, "application/json");
            exchange.getMessage().setBody(reasonText);
        }
    }
    ```
4. In the core connector, add HealthRouter to the Camel Context
       
   ```
   <bean id="healthRouter" class="com.modusbox.client.router.HealthRouter" />
  
   <camel:camelContext>
       ....
       ....
       <camel:routeBuilder ref="healthRouter"/>
   </camel:camelContext>
   
   or you can load whole package into the camel context like below,
   
   <camel:camelContext>
       <camel:package>com.modusbox.client.router</camel:package>
   </camel:camelContext>
   ```
       
   And add below as servicebean to cxf server,
   
   ```
   <bean class="com.modusbox.client.jaxrs.HealthApiImpl"/>
   
   Eg:
   
   <camelcxf:rsServer ....>
       <camelcxf:serviceBeans>
           ....
           ....
           <bean class="com.modusbox.client.jaxrs.HealthApiImpl"/>
       </camelcxf:serviceBeans>
       ....
       ....
   </camelcxf:rsServer>
   ```