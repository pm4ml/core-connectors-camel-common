package com.modusbox.client.validator;

import com.modusbox.client.customexception.AccountNumberFormatException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.regex.Pattern;

public class AccountNumberFormatValidator implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String idType = (String) exchange.getIn().getHeader("idType");
        String loanAccount = "";
        if(idType.equalsIgnoreCase("ACCOUNT_ID")) {
            loanAccount = (String) exchange.getIn().getHeader("idValue");
            String regex = exchange.getProperty("accountNumberFormat", "[0-9]+", String.class);
            Pattern pattern = Pattern.compile(regex);
            if(loanAccount == null || loanAccount.trim().isEmpty()) {
                throw new AccountNumberFormatException("Account Number is null or empty");
            }

            if(!pattern.matcher(loanAccount).matches()) {
                throw new AccountNumberFormatException("Invalid Account Number Format");
            }
        }
    }
}
