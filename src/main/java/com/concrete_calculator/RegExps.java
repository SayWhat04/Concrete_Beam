package com.concrete_calculator;

public class RegExps {
    public static final String POSITIVE_INTEGER = "\\d{0,7}\\d*$";
    public static final String POSITIVE_TWO_DIGITS_INTEGER = "\\d{0,2}?$";
    public static final String ANY_DOUBLE_REGEX = "-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?";
    public static final String POSITIVE_DOUBLE_REGEX = "\\d{0,2}([.]\\d{0,1})?";
}
