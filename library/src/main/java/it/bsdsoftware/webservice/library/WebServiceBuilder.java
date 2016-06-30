package it.bsdsoftware.webservice.library;

/**
 * Created by Simone on 30/06/16.
 */
public class WebServiceBuilder {

    protected String baseUrl, baseUrlTest;
    protected UrlType urlType;
    protected DialogInterface dialogInterface;

    public WebServiceBuilder(){
        this.urlType = UrlType.PRODUCTION;
    }

    public WebServiceBuilder setBaseUrl(String baseUrl){
        this.baseUrl = baseUrl;
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
