package it.bsdsoftware.webservice.library;


import android.os.Build;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

/**
 * Created by Simone Masini on 03/09/2016.
 */
public class WebServiceSyncTask {

    public WebServiceTaskResult getResult(Operation operation){
        HttpURLConnection urlConnection = null;
        WebServiceTaskResult result;
        try {
            urlConnection = operation.buildUrlConnection();
            urlConnection.connect();
            Integer statuCode = urlConnection.getResponseCode();
            if (operation.validStatusCodes().contains(statuCode)) {
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                result = operation.onSuccess(statuCode, inputStream);
            } else {
                Throwable t = null;
                if (statuCode == 404) {
                    t = new Throwable("");
                }
                InputStream errorStream = urlConnection.getErrorStream();
                result = operation.onFail(statuCode, t, errorStream);
            }
        } catch (Throwable t) {
            InputStream errorStream = null;
            if(urlConnection!=null)
                errorStream = urlConnection.getErrorStream();
            result = operation.onFail(-1, t, errorStream);
        } finally {
            if(urlConnection!=null)
                urlConnection.disconnect();
        }
        return result;
    }

    public WebServiceTaskResult getResultUploading(Operation operation){
        HttpResponse response = null;
        String url = operation.getUrlComplete(WebService.getInstance().getUrl());
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(operation.getHttpEntity());
        HashMap<String, String> headers = operation.getHeaders();
        Iterator it = headers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            httppost.setHeader(pair.getKey().toString(), pair.getValue().toString());
            it.remove();
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
                response = httpClient.execute(httppost);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            HttpClient httpclient = new DefaultHttpClient();
            try {
                response = httpclient.execute(httppost);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(response!=null){
            return WebServiceTaskResult.ok;
        }
        return WebServiceTaskResult.fail("Error");
    }
}
