package it.bsdsoftware.webservice.library;

import android.content.Context;
import android.os.AsyncTask;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Simone on 22/04/16.
 */
public class WebServiceTask extends AsyncTask<Void, Integer, WebServiceTaskResult> {

    private WebServiceTaskListener webServiceTaskListener;
    private List<Operation> operations;
    private WebServiceTaskResult result;
    private Context context;
    private boolean showDialog, hideDialog;

    public WebServiceTask(Operation operation, Context context) {
        this(operation, context, true);
    }

    public WebServiceTask(Operation operation, Context context, boolean showAndHideDialog) {
        this(operation, context, showAndHideDialog, showAndHideDialog);
    }

    public WebServiceTask(Operation operation, Context context, boolean showDialog, boolean hideDialog){
        this(Arrays.asList(new Operation[]{operation}), context, showDialog, hideDialog);
    }

    public WebServiceTask(List<Operation> operations, Context context, boolean showDialog, boolean hideDialog){
        this.operations = operations;
        this.context = context;
        this.showDialog = showDialog;
        this.hideDialog = hideDialog;
    }

    public WebServiceTask(List<Operation> operations, Context context, boolean showAndHideDialog){
        this(operations, context, showAndHideDialog, showAndHideDialog);
    }

    public WebServiceTask(List<Operation> operations, Context context){
        this(operations, context, true);
    }


    @Override
    protected void onPreExecute() {
        if(showDialog)
            WebService.getInstance().showDialog(context);
        super.onPreExecute();
    }

    @Override
    protected WebServiceTaskResult doInBackground(Void... params) {
        for(Operation operation : operations){
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
            if(result!=null){
                if(result.result || this.result == null){
                    this.result = result;
                }
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(WebServiceTaskResult risultato) {
        super.onPostExecute(risultato);
        if (webServiceTaskListener != null) {
            webServiceTaskListener.onWebServiceTaskCompletato(risultato);
        }
        if(!risultato.result || hideDialog)
            WebService.getInstance().hideDialog();
    }

    public void setWebServiceTaskListener(WebServiceTaskListener webServiceTaskListener) {
        this.webServiceTaskListener = webServiceTaskListener;
    }
}