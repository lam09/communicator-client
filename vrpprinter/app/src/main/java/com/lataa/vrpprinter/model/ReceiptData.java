package com.lataa.vrpprinter.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;

import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.lataa.vrpprinter.utils.QRCodeGenerator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;


public class ReceiptData {

    String getReceiptType(String type){
        switch (type){
            case "INVALID": return "Neplatný doklad";
            case "VALID": return "";
            default: return "";
        }
    }

    int returnValue;
    public static class Dto{
        public Long id;
        public Long createDate;
        public int invalidReceiptNumber;
        public Long issueDate;
        public int priceWithVat;
        public String receiptId;
        public int receiptNumber;
        public String receiptTypeName;
        public static class ReceiptItem{
            public int unitPriceWithVat;
            public int totalPriceWithVat;
            public int quantity;
            public String type;
            public static class VatRate{
                public int vatRate;
            }
            public VatRate vat;
           public static class Service{
               public String name;
           }
           public Service service;
           public String itemName;
        }
        public ReceiptItem[] items;

        public Organization organization;

        public Unit unit;

        public CashRegister cashRegister;
        public String createDateFormated;
        public String issueDateFormated;
        public String dynamicText;
        public String staticText;
        public static class Payment{

        }
        public Payment[] payments;
        public String okp;
        public static class Customer{

        }
        public Customer customer;
        public boolean oldLayout;
    }
    public Dto dto;
    public String toString(){
        return null;
    }

    public byte[] toPrintData(){
        byte[] QRCodeData = null;
      /*  try {
            QRCodeData = QRCodeGenerator.generateQRCode(dto.receiptId,350,350);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        String billHeader = "     "+getReceiptType(dto.receiptTypeName) + " c. "+dto.receiptNumber+"\n"
                + "      " + dto.organization.name + "\n"
                + dto.organization.address.street+", "+dto.organization.address.zipCode+" "+dto.organization.address.city + "\n"
                + "Predajné miesto: " + "\n"
                + dto.unit.address.street+", "+dto.unit.address.zipCode+" "+dto.unit.address.city + "\n"
                + "DIČ: "+ dto.organization.dic + "\n"
                + "IČO: "+ dto.organization.ico + "\n"
                + "Kód pokladnice: "+ "\n"
                + dto.cashRegister.dkp + "\n"
                + " Čas: " + dto.createDateFormated+ "\n"
                + "_____________________________________________"+"\n\n";
        String billData = "";
        for(Dto.ReceiptItem item:dto.items){
            billData += (item.itemName==null? item.service.name:item.itemName) + "\n";
            billData += (item.type.equals("POSITIVE")?"":"-") + item.quantity +"x    "+item.vat.vatRate+"%"+"    "+item.unitPriceWithVat +"   "+ item.totalPriceWithVat +"\n";
        }
        billData +="_____________________________________________"+"\n";
        billData +="SPOLU: " +"        "+ dto.priceWithVat +"\n";
        billData +="_____________________________________________"+"\n";

        String billFoot="";
        billFoot+="               "+"OKP\n";
        billFoot+=" "+dto.okp+"\n";
        billFoot+= "              ID dokladu:  "+ "\n";
        billFoot+=" "+ dto.receiptId + "\n";

        byte[]bill = (billHeader+billData+billFoot).getBytes();
      /*  byte[] result = new byte[bill.length + QRCodeData.length];
        System.arraycopy(bill, 0, result, 0, bill.length);
        System.arraycopy(QRCodeData, 0, result, bill.length, QRCodeData.length);*/
        return bill;
    }


}
