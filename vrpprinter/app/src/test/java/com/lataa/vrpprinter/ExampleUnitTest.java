package com.lataa.vrpprinter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.lataa.vrpprinter.communication.LoginRequest;
import com.lataa.vrpprinter.communication.VrpCommunicator;
import com.lataa.vrpprinter.model.DashboardResponse;
import com.lataa.vrpprinter.model.LoginResponse;
import com.lataa.vrpprinter.model.ReceiptData;
import com.lataa.vrpprinter.model.ReceiptListResponse;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }





    @Test
    public void test_communication(){
        VrpCommunicator communicator = new VrpCommunicator();
        LoginResponse loginResponse = communicator.login(new LoginRequest("2143115368","Thuong2019"));
        DashboardResponse dashboardResponse = communicator.getDashBoard(loginResponse);
        ReceiptListResponse receiptListResponse = communicator.getReceiptList(loginResponse,dashboardResponse);

        Arrays.asList(receiptListResponse.results).stream().forEach(
                receipt->{
                    ReceiptData receiptData = communicator.getReceiptData(loginResponse,receipt.id);
                    byte[] data = receiptData.toPrintData();
                    Path path = FileSystems.getDefault().getPath("./ticket"+receipt.id);
                    //Bitmap bmp= BitmapFactory.decodeByteArray(data,0,data.length);
                    File file = path.toFile();
                    try {
                        FileOutputStream outputStream = new FileOutputStream(file);
                        outputStream.write(data);
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }


}