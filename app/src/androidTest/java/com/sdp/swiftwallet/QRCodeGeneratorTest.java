package com.sdp.swiftwallet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import android.graphics.Bitmap;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.zxing.WriterException;
import com.sdp.swiftwallet.domain.model.QRCodeGenerator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class QRCodeGeneratorTest {
    public final String correctString = "encodind";
    public final Integer correctWidth = 400;
    public final String emptyString = "";
    public final Integer zero = 0;
    @Test
    public void ableToEncodeCorrectValues(){
        try {
            Bitmap result = QRCodeGenerator.encodeAsBitmap(correctString, correctWidth);
        }
        catch(WriterException we){
            Assert.fail("Exception was thrown: "+we.getMessage());
        }
    }
    @Test
    public void failsWhenEncodingFalsyValues(){
        try{
            Bitmap result = QRCodeGenerator.encodeAsBitmap(correctString, zero);
        }
        catch(Exception e){
           assertTrue(e instanceof IllegalArgumentException);
        }
        try{
            Bitmap result = QRCodeGenerator.encodeAsBitmap(emptyString, correctWidth);
        }
        catch(Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }
    }
}
