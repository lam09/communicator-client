package com.lataa.vrpprinter.communication;

import com.google.gson.Gson;
import com.lataa.vrpprinter.model.DashboardResponse;
import com.lataa.vrpprinter.model.LoginResponse;
import com.lataa.vrpprinter.model.ReceiptData;
import com.lataa.vrpprinter.model.ReceiptListResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class VrpCommunicator {
    String token;
    Gson gson = new Gson();
    public LoginResponse login(LoginRequest req){
        String url = "https://vcrp.financnasprava.sk/crp/api/v1/security/autentify/vrp";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpOptions httpOptions = new HttpOptions(url);
        httpOptions.setHeader("Origin", "https://vrp.financnasprava.sk");
        httpOptions.setHeader("Access-Control-Request-Method", "POST");
        httpOptions.setHeader("Access-Control-Request-Headers", "content-type,crpbrowser,crpbrowserversion,crpdate,crpos,crposversion,ignoreloadingbar,lang,sessionid");
        LoginResponse res = null;
        try {
            CloseableHttpResponse response = client.execute(httpOptions);
            int statusCode = response.getStatusLine().getStatusCode();
            String body = IOUtils.toString(response.getEntity().getContent(), "utf8");
            System.out.println("Option response: " +body);
            Header header = response.getFirstHeader("Access-Control-Allow-Origin");
           // client.close();

            String loginUrl = "https://vcrp.financnasprava.sk/crp/api/v1/security/autentify/vrp";
            HttpPost httpPost = new HttpPost(loginUrl);
            httpPost.setHeader("Accept", "application/json, text/plain, */*");
            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
            httpPost.setHeader("crpDate", new Long(System.currentTimeMillis()).toString());
            httpPost.setHeader("Origin", "https://vrp.financnasprava.sk");
            StringEntity entity = new StringEntity(gson.toJson(req));
            httpPost.setEntity(entity);
            CloseableHttpResponse loginResponse = client.execute(httpPost);
            String loginResponseBody = IOUtils.toString(loginResponse.getEntity().getContent(), "utf8");
            System.out.println("Login response: " +loginResponseBody);
            res = gson.fromJson(loginResponseBody,LoginResponse.class);
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public DashboardResponse getDashBoard(LoginResponse loginResponse){
        String token = loginResponse.token;
        String url = "https://vcrp.financnasprava.sk/crp/api/v2/user/getdashboard";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", "application/json, text/plain, */*");
        httpGet.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpGet.setHeader("crpDate", new Long(System.currentTimeMillis()).toString());
        httpGet.setHeader("Origin", "https://vrp.financnasprava.sk");
        httpGet.setHeader("crpToken",token);
        DashboardResponse dashboardResponse = null;
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            String body = IOUtils.toString(response.getEntity().getContent(), "utf8");
            System.out.println("DashBoard response: " +body);
            dashboardResponse = gson.fromJson(body,DashboardResponse.class);
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dashboardResponse;
    }

    public ReceiptListResponse getReceiptList(LoginResponse loginResponse, DashboardResponse dashboardResponse){
        String token = loginResponse.token;
        String url = "https://vcrp.financnasprava.sk/crp/api/v4/receipt/receiptlist";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept", "application/json, text/plain, */*");
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpPost.setHeader("crpDate", new Long(System.currentTimeMillis()).toString());
        httpPost.setHeader("Origin", "https://vrp.financnasprava.sk");
        httpPost.setHeader("crpToken",token);
        ReceiptListResponse receiptListResponse = null;
        try {
            ReceiptListRequest req = new ReceiptListRequest(
                    new ReceiptListRequest.Paging(10,"0"),
                    new ReceiptListRequest.Ordering("createDate", "desc"),
                    new ReceiptListRequest.FilterCriteria(1567288800000L,new Long(System.currentTimeMillis())));
            StringEntity entity = new StringEntity(gson.toJson(req));
            httpPost.setEntity(entity);
            CloseableHttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            String body = IOUtils.toString(response.getEntity().getContent(), "utf8");
            System.out.println("ReceiptList response: " +body);
            receiptListResponse=gson.fromJson(body,ReceiptListResponse.class);
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receiptListResponse;
    }

    public ReceiptData getReceiptData(LoginResponse loginResponse,long receiptId){
        String token = loginResponse.token;
        String url = "https://vcrp.financnasprava.sk/crp/api/v4/receipt/getdata"+"?id="+receiptId;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", "application/json, text/plain, */*");
        httpGet.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpGet.setHeader("crpDate", new Long(System.currentTimeMillis()).toString());
        httpGet.setHeader("Origin", "https://vrp.financnasprava.sk");
        httpGet.setHeader("crpToken",token);
        ReceiptData receiptData = null;
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            String body = IOUtils.toString(response.getEntity().getContent(), "utf8");
            System.out.println("Receipt data response: " +body);
            receiptData = gson.fromJson(body,ReceiptData.class);
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receiptData;
    }

}
