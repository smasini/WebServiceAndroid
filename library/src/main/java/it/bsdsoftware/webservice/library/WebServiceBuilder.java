package it.bsdsoftware.webservice.library;

import android.content.Context;

/**
 * Created by Simone on 30/06/16.
 */
public class WebServiceBuilder {

    protected String baseUrlProduction, baseUrlTest, baseUrlStaging, baseUrlLocal, baseUrlDemo, baseUrlHandled;
    protected UrlType urlType;
    protected DialogInterface dialogInterface;
    protected Context context;

    public WebServiceBuilder(Context context){
        this.urlType = UrlType.PRODUCTION;
        this.context = context;
    }

    public WebServiceBuilder setBaseUrlHandled(String baseUrlHandled) {
        this.baseUrlHandled = baseUrlHandled;
        return this;
    }

    public WebServiceBuilder setBaseUrlProduction(String baseUrlProduction){
        this.baseUrlProduction = baseUrlProduction;
        return this;
    }

    public WebServiceBuilder setBaseUrlStaging(String baseUrlStaging) {
        this.baseUrlStaging = baseUrlStaging;
        return this;
    }

    public WebServiceBuilder setBaseUrlLocal(String baseUrlLocal) {
        this.baseUrlLocal = baseUrlLocal;
        return this;
    }

    public WebServiceBuilder setBaseUrlDemo(String baseUrlDemo) {
        this.baseUrlDemo = baseUrlDemo;
        return this;
    }

    public WebServiceBuilder setBaseUrlTest(String baseUrlTest){
        this.baseUrlTest = baseUrlTest;
        return this;
    }

    public WebServiceBuilder setUrlType(UrlType urlType){
        this.urlType = urlType;
        return this;
    }

    public WebServiceBuilder setDialogInterface(DialogInterface dialogInterface) {
        this.dialogInterface = dialogInterface;
        return this;
    }
}
