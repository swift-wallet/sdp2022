package com.sdp.swiftwallet.common;

public class HelperFunctions {
    public static final int SHORTENED_ADDRESS_LENGTH = 4;
    public static final String SHORTENED_ADDRESS_JOINER = "...";

    public static String toShortenedFormatAddress(String fullHexAddress){
        int len = fullHexAddress.length();
        return fullHexAddress.substring(0, SHORTENED_ADDRESS_LENGTH) + SHORTENED_ADDRESS_JOINER +
                fullHexAddress.substring(len - SHORTENED_ADDRESS_LENGTH, len);
    }
}
