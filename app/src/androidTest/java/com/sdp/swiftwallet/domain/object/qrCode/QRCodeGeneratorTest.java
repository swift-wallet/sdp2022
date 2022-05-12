package com.sdp.swiftwallet.domain.object.qrCode;

import static org.junit.Assert.assertTrue;
import com.google.zxing.WriterException;
import com.sdp.swiftwallet.domain.model.qrCode.QRCodeGenerator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class QRCodeGeneratorTest {
    public final String correctString = "encoding";
    public final int correctWidth = 400;
    public final String emptyString = "";
    public final int zero = 0;

    @Test
    public void ableToEncodeCorrectValues(){
        try {
            QRCodeGenerator.encodeAsBitmap(correctString, correctWidth);
        }
        catch(WriterException we){
            Assert.fail("Exception was thrown: "+we.getMessage());
        }
    }
    @Test
    public void failsWhenEncodingFalsyValues(){
        try{
            QRCodeGenerator.encodeAsBitmap(correctString, zero);
        }
        catch(Exception e){
           assertTrue(e instanceof IllegalArgumentException);
        }
        try{
            QRCodeGenerator.encodeAsBitmap(emptyString, correctWidth);
        }
        catch(Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }
    }
    @Test
    public void ableToEncodeWithDefaultSize(){
        try {
            QRCodeGenerator.encodeAsBitmap(correctString);
        }
        catch(WriterException we){
            Assert.fail("Exception was thrown: "+we.getMessage());
        }
    }
}
