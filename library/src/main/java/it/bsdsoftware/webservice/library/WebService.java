package it.bsdsoftware.webservice.library;

import android.content.Context;

/**
 * Created by Simone on 30/06/16.
 */
public class WebService {

    private static WebService instance;

    public static WebService getInstance(){
        if(instance == null){
            throw new RuntimeException("WebService: Istanza non inizializzata! Assicurati di chiamare il metodo 'init' all'avvio dell'applicazione");
        }
        return instance;
    }

    public static void init(WebServiceBuilder builder){
        if(builder.baseUrlTest == null || builder.baseUrlTest.equals("")){
            instance = new WebService(builder.baseUrl);
        }else{
            instance = new WebService(builder.baseUrl, builder.baseUrlTest);
        }
        instance.setUrlType(builder.urlType);
        instance.setDialogInterface(builder.dialogInterface);
    }

    private String baseUrl, baseUrlTest;
    private UrlType urlType;
    private DialogInterface dialogInterface;

    private WebService(String baseUrl, String baseUrlTest)
    {
        this.baseUrl = baseUrl;
        this.baseUrlTest = baseUrlTest;
    }

    private WebService(String baseUrl)
    {
        this.baseUrl = baseUrl;
    }

    public void setUrlType(UrlType urlType) {
        this.urlType = urlType;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setBaseUrlTest(String baseUrlTest) {
        this.baseUrlTest = baseUrlTest;
    }

    public void setDialogInterface(DialogInterface dialogInterface) {
        this.dialogInterface = dialogInterface;
    }

    public String getUrl(){
        String url = null;
        switch (urlType){
            case PRODUCTION:
                url = baseUrl;
                break;
            case TEST:
                url = baseUrlTest;
                break;
        }
        if(url!=null){
            return url;
        }
        if(baseUrl!=null && !baseUrl.equals("")){
            return baseUrl;
        }
        if(baseUrlTest!=null && !baseUrlTest.equals("")){
            return baseUrlTest;
        }
        throw new RuntimeException("Non è stato impostato nessun baseUrl");
    }

    public void showDialog(Context context){
        if(dialogInterface!=null){
            dialogInterface.showDialog(context);
        }
    }

    public void hideDialog(){
        if(dialogInterface!=null){
            dialogInterface.hideDialog();
        }
    }

}
