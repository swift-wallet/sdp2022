package com.sdp.swiftwallet.domain.model.qrCode;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * Represents a QR code generator
 */
public class QRCodeGenerator {

    private static final int DEFAULT_SIZE = 800;
    public static Bitmap encodeAsBitmap(String str) throws WriterException {
        return encodeAsBitmap(str, DEFAULT_SIZE);
    }

    /**
    *   Creates a bitmap object out of a string and a bitmap width
    *   @param str : the string to convert
    *   @param squareSize: the size of the bitmap
    *   @throws WriterException
    **/
    public static Bitmap encodeAsBitmap(String str, int squareSize) throws WriterException {
        BitMatrix result = createQRBitMatrix(str, squareSize);
        int arraySize = squareSize*squareSize;
        int[] pixels = new int[arraySize];
        for (int x = 0; x < arraySize; x++) {
            pixels[x] = result.get(x % squareSize, x / squareSize) ? BLACK : WHITE;
        }
        Bitmap bitmap = Bitmap.createBitmap(squareSize, squareSize, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, squareSize, 0, 0, squareSize, squareSize);
        return bitmap;
    }

    private static BitMatrix createQRBitMatrix(String str, Integer squareSize) throws WriterException {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        return multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, squareSize, squareSize, null);
    }
}
