package it.bsdsoftware.webservice.library;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
        instance = new WebService(builder.baseUrlProduction, builder.baseUrlTest, builder.baseUrlStaging, builder.baseUrlLocal, builder.baseUrlDemo);
        instance.setContext(builder.context);
        instance.setUrlType(builder.urlType);
        instance.setDialogInterface(builder.dialogInterface);
        instance.setBaseUrlHandled(builder.baseUrlHandled);
        instance.restoreHandled();
    }

    private String baseUrlProduction, baseUrlTest, baseUrlStaging, baseUrlLocal, baseUrlDemo, baseUrlHandled;
    private UrlType urlType;
    private DialogInterface dialogInterface;
    private Context context;

    private WebService(String baseUrlProduction, String baseUrlTest, String baseUrlStaging, String baseUrlLocal, String baseUrlDemo) {
        this.baseUrlProduction = baseUrlProduction;
        this.baseUrlTest = baseUrlTest;
        this.baseUrlStaging = baseUrlStaging;
        this.baseUrlLocal = baseUrlLocal;
        this.baseUrlDemo = baseUrlDemo;
    }

    public void setUrlType(UrlType urlType) {
        this.urlType = urlType;
    }

    public void setBaseUrlProduction(String baseUrlProduction) {
        this.baseUrlProduction = baseUrlProduction;
    }

    public void setBaseUrlStaging(String baseUrlStaging) {
        this.baseUrlStaging = baseUrlStaging;
    }

    public void setBaseUrlLocal(String baseUrlLocal) {
        this.baseUrlLocal = baseUrlLocal;
    }

    public void setBaseUrlDemo(String baseUrlDemo) {
        this.baseUrlDemo = baseUrlDemo;
    }

    public void setBaseUrlTest(String baseUrlTest) {
        this.baseUrlTest = baseUrlTest;
    }

    public void setBaseUrlHandled(String baseUrlHandled) {
        this.baseUrlHandled = baseUrlHandled;
        if(baseUrlHandled!=null && !baseUrlHandled.isEmpty()){
            saveHandled();
            urlType = UrlType.HANDLED;
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setDialogInterface(DialogInterface dialogInterface) {
        this.dialogInterface = dialogInterface;
    }

    public String getUrl(){
        String url = null;
        switch (urlType){
            case TEST:
                url = baseUrlTest;
                break;
            case LOCAL:
                url = baseUrlLocal;
                break;
            case STAGING:
                url = baseUrlStaging;
                break;
            case PRODUCTION:
                url = baseUrlProduction;
                break;
            case DEMO:
                url = baseUrlDemo;
                break;
            case HANDLED:
                url = baseUrlHandled;
                break;
        }
        if(url!=null){
            return url;
        }
        if(baseUrlProduction!=null && !baseUrlProduction.equals("")){
            return baseUrlProduction;
        }
        if(baseUrlStaging!=null && !baseUrlStaging.equals("")){
            return baseUrlStaging;
        }
        if(baseUrlTest!=null && !baseUrlTest.equals("")){
            return baseUrlTest;
        }
        if(baseUrlDemo!=null && !baseUrlDemo.equals("")){
            return baseUrlDemo;
        }
        if(baseUrlLocal!=null && !baseUrlLocal.equals("")){
            return baseUrlLocal;
        }
        throw new RuntimeException("Non è stato impostato nessun baseUrl");
    }

    private void saveHandled(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString("base_url_handled_key", baseUrlHandled).apply();
    }

    private void restoreHandled(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        baseUrlHandled = sp.getString("base_url_handled_key", getUrl());
        if(baseUrlHandled!=null && !baseUrlHandled.isEmpty()){
            urlType = UrlType.HANDLED;
        }
    }

    public void resetToProduction(){
        setBaseUrlHandled(baseUrlProduction);
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
