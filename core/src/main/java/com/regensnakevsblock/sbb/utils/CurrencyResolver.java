package com.regensnakevsblock.sbb.utils;

import java.util.HashMap;
import java.util.Map;

public class CurrencyResolver {
    private static final Map<String, String> SYMBOL_TO_CODE = new HashMap<>();

    static {
        SYMBOL_TO_CODE.put("USD","$" );
        SYMBOL_TO_CODE.put("EUR","€" );
        SYMBOL_TO_CODE.put("GBP","£" );
        SYMBOL_TO_CODE.put( "JPY","¥");
        SYMBOL_TO_CODE.put("NGN","₦");
        SYMBOL_TO_CODE.put( "INR","₹");
        SYMBOL_TO_CODE.put( "KRW","₩");
        SYMBOL_TO_CODE.put( "RUB","₽");
        SYMBOL_TO_CODE.put( "TRY","₺");
        SYMBOL_TO_CODE.put( "UAH","₴");
        SYMBOL_TO_CODE.put( "VND","₫");
        SYMBOL_TO_CODE.put( "ZAR","R");
    }

    public static String getCurrencyCode(String symbol) {
        return SYMBOL_TO_CODE.getOrDefault(symbol, "?");
    }
    public static String formatPrice(double value) {
        String s = String.valueOf(value);

        StringBuilder out = new StringBuilder();

        int count = 0;

        for (int i = s.length() - 1; i >= 0; i--) {
            out.insert(0, s.charAt(i));
            count++;

            if (count % 3 == 0 && i != 0) {
                out.insert(0, ",");
            }
        }

        return out.toString();
    }
}
