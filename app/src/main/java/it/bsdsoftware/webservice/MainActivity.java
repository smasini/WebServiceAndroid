package it.bsdsoftware.webservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import it.bsdsoftware.webservice.library.WebServiceTask;
import it.bsdsoftware.webservice.library.WebServiceTaskListener;
import it.bsdsoftware.webservice.library.WebServiceTaskResult;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyOperation op = new MyOperation();
        WebServiceTask task = new WebServiceTask(op, this);
        task.setWebServiceTaskListener(new WebServiceTaskListener() {
            @Override
            public void onWebServiceTaskCompletato(WebServiceTaskResult result) {

            }
        });
        task.setWebServiceTaskListener(new WebServiceTaskListener<String>() {
            @Override
            public void onWebServiceTaskCompletato(WebServiceTaskResult<String> result) {

            }
        });
    }
}
