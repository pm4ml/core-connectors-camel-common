package com.modusbox.client.utils;

import com.modusbox.client.enums.ErrorCode;

import java.math.BigDecimal;

import static com.modusbox.client.enums.ErrorCode.*;

public class Utility {

    public static boolean isPhoneNumberMatch(String walletPhoneNumber, String mfiPhoneNumber) {
        return walletPhoneNumber.equals(mfiPhoneNumber) ||
                walletPhoneNumber.substring(1).equals(mfiPhoneNumber) ||
                (walletPhoneNumber.length() >= 2 && walletPhoneNumber.substring(2).equals(mfiPhoneNumber));
    }
    public static String stripMyanmarPhoneNumberCode(String number) {

        if(number.startsWith("+")) {
            number = number.substring(1);
        }

        if(number.startsWith("95")) {
            number = number.substring(2);
        }

        if(number.startsWith("9")) {
            number = "0" + number;
        }
        return number;
    }

    public static String stripTrailingZerosAfterDecimalPoint(String strNumber) throws Exception {
        String resultNumber = strNumber;
            if (strNumber != null) {
                BigDecimal stripedVal = new BigDecimal(strNumber).stripTrailingZeros();
                resultNumber = stripedVal.toPlainString();
            }
            if( resultNumber != null && resultNumber.equals("") && getDecimalCount(resultNumber) > 4)
            {
                throw new Exception(ROUNDING_VALUE_ERROR.getDefaultMessage());
            }
            return resultNumber;
    }

    public static int getDecimalCount(String num)
    {
        String str = num;
        //Find the index of the decimal
        int index = str.indexOf(".");
        //Return the index subtracted from the length of the string
        return str.length() - index - 1;
    }
}
