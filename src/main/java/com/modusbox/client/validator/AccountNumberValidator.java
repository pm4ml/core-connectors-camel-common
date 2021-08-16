package com.modusbox.client.validator;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.regex.Pattern;

public class AccountNumberValidator implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String idType = (String) exchange.getIn().getHeader("idType");
        String loanAccount = "";
        if(idType.equalsIgnoreCase("ACCOUNT_ID")) {
            loanAccount = (String) exchange.getIn().getHeader("idValue");
            String regex = exchange.getProperty("accountNumberFormat", "[0-9]+", String.class);
            Pattern pattern = Pattern.compile(regex);
            if(loanAccount == null || loanAccount.trim().isEmpty()) {
                //throw exception
            }

            if(!pattern.matcher(regex).matches()) {
                //throw
            }
        }
    }
}
