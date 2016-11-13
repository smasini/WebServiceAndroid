package it.bsdsoftware.webservice.library;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;

/**
 * Created by Simone on 22/04/16.
 */
public abstract class Operation {

    public WebServiceTaskResult onSuccess(String response){
        return WebServiceTaskResult.ok;
    }
    public abstract WebServiceTaskResult onSuccess(int statusCode, InputStream inputStream);
    public abstract MethodType getMethodType();
    public abstract String getUrlComplete(String baseUrl);
    public abstract HashMap<String, String> getHeaders();
    public abstract HashMap<String, Object> getParams();

    public HttpURLConnection buildUrlConnection(){
        String url = getUrlComplete(WebService.getInstance().getUrl());
        HttpURLConnection connection = null;
        HashMap<String, Object> params = getParams();

        try {
            Uri uri = Uri.parse(url);
            switch (getMethodType()){
                case GET:
                    Uri.Builder builder = uri.buildUpon();
                    if(params!=null){
                        Iterator it = params.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();
                            builder.appendQueryParameter(pair.getKey().toString(), pair.getValue().toString());
                            it.remove();
                        }
                    }
                    URL myUrl = new URL(builder.toString());
                    connection = (HttpURLConnection) myUrl.openConnection();
                    connection.setRequestMethod("GET");

                    connection = setHeaders(connection);
                    break;
                case POST:
                    myUrl = new URL(url);
                    connection = (HttpURLConnection) myUrl.openConnection();
                    connection.setRequestMethod("POST");
                    connection = setHeaders(connection);

                    JSONObject jsonObject = new JSONObject();
                    if(params!=null){
                        Iterator it = params.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();
                            jsonObject.put((String)pair.getKey(),pair.getValue());
                            it.remove();
                        }
                    }
                    OutputStream os = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, getCharset()));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();
                    os.close();
                    break;
            }
        }catch (Throwable t){
            throw new RuntimeException(t);
        }
        return connection;
    }

    public HttpURLConnection setHeaders(HttpURLConnection connection){
        HashMap<String, String> headers = getHeaders();
        if(headers!=null) {
            Iterator it = headers.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                connection.addRequestProperty(pair.getKey().toString(), pair.getValue().toString());
                it.remove();
            }
        }
        return connection;
    }

    protected String readStringFromInputStream(InputStream inputStream){
        try {
            //"iso-8859-1"
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, getCharset()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            inputStream.close();
            return sb.toString();
        }
        catch (Throwable t){

        }
        return "";
    }

    protected JSONObject readJSONObjectFromInputStream(InputStream inputStream){
        String str = readStringFromInputStream(inputStream);
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(str);
        }
        catch (JSONException e){
            jsonObject = new JSONObject();
        }
        return jsonObject;
    }

    protected JSONArray readJSONArrayFromInputStream(InputStream inputStream){
        String str = readStringFromInputStream(inputStream);
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(str);
        } catch (JSONException e) {
            jsonArray = new JSONArray();
            e.printStackTrace();
        }
        return jsonArray;
    }

    public WebServiceTaskResult onFail(int statusCode, Throwable t, InputStream errorStream) {
        if (statusCode == -1) {
            return WebServiceTaskResult.fail(t);
        } else {
            return WebServiceTaskResult.fail(String.format("Status Code: %d", statusCode));
        }
    }

    protected String getCharset(){
        return "UTF-8";
    }

    public boolean isUploadingImage(){
        return false;
    }

    public HttpEntity getHttpEntity(){
        return null;
    }

    public HttpEntity getHttpEntityFromFile(File file){
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        if (file != null && file.exists()){
            FileBody fileBody = new FileBody(file);
            builder.addPart(file.getName(), fileBody);
        }
        return builder.build();
    }

    public List<Integer> validStatusCodes(){
        return Arrays.asList(200);
    }
}
