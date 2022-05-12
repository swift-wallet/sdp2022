package com.sdp.swiftwallet.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class HelperFunctionsTest {
    public static String address= "0x000000000000000000000000000000000000dead";
    public static String shortAddress = "0x0000...dead";
    @Test
    public void shouldCreateAShortenedAddressFormat(){
        String shortened = HelperFunctions.toShortenedFormatAddress(address);
        assert(shortened.equals(shortAddress));
    }
}
