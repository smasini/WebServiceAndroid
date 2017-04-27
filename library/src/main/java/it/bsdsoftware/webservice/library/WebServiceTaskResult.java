package it.bsdsoftware.webservice.library;

/**
 * Created by Simone on 30/06/16.
 */
public class WebServiceTaskResult<T> {

    private final String message;
    private final Throwable exception;
    private final TaskResult taskResult;
    private T data;

    public static WebServiceTaskResult fail(String msg) {
        return fail(null, msg);
    }

    public static WebServiceTaskResult fail(Throwable t) {
        return fail(t, null);
    }

    public static WebServiceTaskResult fail(Throwable t, String msg) {
        return new WebServiceTaskResult(TaskResult.FAIL, msg, t);
    }

    public static WebServiceTaskResult success(){
        return success(null);
    }

    public static WebServiceTaskResult success(String msg){
        return new WebServiceTaskResult(TaskResult.SUCCESS, msg);
    }

    public static WebServiceTaskResult cached() {
        return new WebServiceTaskResult(TaskResult.CACHED);
    }

    public WebServiceTaskResult(T data){
        this(TaskResult.SUCCESS);
        this.data = data;
    }

    public WebServiceTaskResult(T data, String msg){
        this(TaskResult.SUCCESS, msg);
        this.data = data;
    }

    private WebServiceTaskResult(TaskResult taskResult) {
        this(taskResult, null, null);
    }
    private WebServiceTaskResult(TaskResult taskResult, String msg) {
        this(taskResult, msg, null);
    }
    private WebServiceTaskResult(TaskResult taskResult, String msg, Throwable t) {
        this.taskResult = taskResult;
        this.message = msg;
        this.exception = t;
    }

    public String getMessageForUser() {
        StringBuilder msg = new StringBuilder();
        if (message != null)
            msg.append(message);

        if (exception != null) {
            Throwable t = exception;
            do {
                msg.append(System.getProperty("line.separator")).append(t.getLocalizedMessage());
            } while((t = t.getCause()) != null);
        }

        return msg.toString();
    }

    public TaskResult getTaskResult() {
        return taskResult;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isCompletedWithoutError(){
        switch (taskResult){
            case SUCCESS:
            case CACHED:
                return true;
            default:
            case FAIL:
                return false;
        }
    }

    public WebServiceTaskResult addResult(WebServiceTaskResult r) {
        TaskResult taskResult = this.taskResult;
        //boolean result = isCompletedWithoutError();
        String msg = this.message == null ? "" : this.message;
        Throwable t = this.exception;

        if (!r.isCompletedWithoutError()) {
            //result = false;
            taskResult = TaskResult.FAIL;
        }
        if (r.message != null) {
            if (msg.length() != 0)
                msg += System.getProperty("line.separator") + System.getProperty("line.separator");

            msg += r.message;
        }
        if (r.exception != null) {
            try {
                Throwable newT = (Throwable) r.clone();
                newT.initCause(t);
                t = newT;
            } catch (CloneNotSupportedException e) {
                t = r.exception;
            }
        }
        return new WebServiceTaskResult(taskResult, msg, t);
    }
}
