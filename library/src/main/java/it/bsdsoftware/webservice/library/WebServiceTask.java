package it.bsdsoftware.webservice.library;

import android.content.Context;
import android.os.AsyncTask;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Simone on 22/04/16.
 */
public class WebServiceTask extends AsyncTask<Void, Integer, WebServiceTaskResult> {

    private WebServiceTaskListener webServiceTaskListener;
    private List<Operation> operations;
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
        WebServiceTaskResult mainResult = null;
        WebServiceSyncTask webServiceSyncTask = new WebServiceSyncTask();
        for(Operation operation : operations){
            WebServiceTaskResult result = null;
            boolean handled = false;
            if(operation instanceof Cachable){
                Cachable cachable = (Cachable)operation;
                if(cachable.isDataAlreadyCached()){
                    result = WebServiceTaskResult.cached();
                    handled = true;
                }
            }
            if(!handled) {
                if (operation.isUploadingImage()) {
                    result = webServiceSyncTask.getResultUploading(operation);
                } else {
                    result = webServiceSyncTask.getResult(operation);
                }
            }
            if(result!=null){
                if(mainResult == null){
                    mainResult = result;
                }else{
                    mainResult.addResult(result);
                }
            }
        }
        return mainResult;
    }

    @Override
    protected void onPostExecute(WebServiceTaskResult result) {
        super.onPostExecute(result);
        if(hideDialog)
            WebService.getInstance().hideDialog();
        if (webServiceTaskListener != null) {
            webServiceTaskListener.onWebServiceTaskCompletato(result);
        }
    }

    public void setWebServiceTaskListener(WebServiceTaskListener webServiceTaskListener) {
        this.webServiceTaskListener = webServiceTaskListener;
    }
}