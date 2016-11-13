package it.bsdsoftware.webservice;

import java.io.InputStream;
import java.util.HashMap;

import it.bsdsoftware.webservice.library.MethodType;
import it.bsdsoftware.webservice.library.Operation;
import it.bsdsoftware.webservice.library.WebServiceTaskResult;

/**
 * Created by smasini on 27/08/16.
 */
public class MyOperation extends Operation {

    @Override
    public WebServiceTaskResult onSuccess(int statusCode, InputStream inputStream) {
        WebServiceTaskResult s = WebServiceTaskResult.success("");
        return s;
    }

    @Override
    public MethodType getMethodType() {
        return null;
    }

    @Override
    public String getUrlComplete(String baseUrl) {
        return null;
    }

    @Override
    public HashMap<String, String> getHeaders() {
        return null;
    }


    @Override
    public HashMap<String, Object> getParams() {
        return null;
    }
}
