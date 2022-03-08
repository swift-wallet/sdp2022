package com.sdp.swiftwallet.domain.model;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QRCodeGenerator {
    /*
    *   Creates a bitmap object out of a string and a bitmap width
    *   @param str : the string to convert
    *   @param squareWidth: the width of the bitmap
    *   @throws WriterException
    * */
    public static Bitmap encodeAsBitmap(String str, Integer squareWidth) throws WriterException {
        BitMatrix result;
        result = new MultiFormatWriter().encode(str,
                BarcodeFormat.QR_CODE, squareWidth, squareWidth, null);
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, squareWidth, 0, 0, w, h);
        return bitmap;
    }
}
