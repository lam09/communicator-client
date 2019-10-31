package com.lataa.vrpprinter.utils;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;


public class QRCodeGenerator {
    private static final String QR_CODE_IMAGE_PATH = "./MyQRCode.png";

    private static void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public static BitMatrix generateQRCodeBitMatrix(String text, int width, int height)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        return qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
    }
    public static byte[] generateQRCode(String text, int width, int height)
            throws WriterException, IOException {
        try {
            Bitmap bmp = encodeToQrCode(text, width, height);
            if (bmp != null ) {
                return Utils.decodeBitmap(bmp);
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
        return null;
    }


    public static Bitmap encodeToQrCode(String text, int width, int height){
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = null;
        try {
            matrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height);
        } catch (WriterException ex) {
            ex.printStackTrace();
        }
        int h = matrix.getHeight();
        int w = matrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        for (int x = 0; x < w; x++){
            for (int y = 0; y < h; y++){
                bmp.setPixel(x, y, matrix.get(x,y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }

    public static void main(String[] args) {
        try {
            //generateQRCodeImage("O-F9019A6A54D044DC819A6A54D004DCD3", 350, 350, QR_CODE_IMAGE_PATH);
            generateQRCodeImage("O-F9019A6A54D044DC819A6A54D004DCD3", 350, 350, QR_CODE_IMAGE_PATH);
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }
    }
}