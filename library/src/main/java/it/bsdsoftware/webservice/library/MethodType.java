package it.bsdsoftware.webservice.library;

/**
 * Created by Simone on 30/06/16.
 */
public enum MethodType {
    POST,
    GET,
    PUT,
    DELETE;

    public String name;

    static {
        POST.name = "POST";
        GET.name = "GET";
        PUT.name = "PUT";
        DELETE.name = "DELETE";
    }
}
