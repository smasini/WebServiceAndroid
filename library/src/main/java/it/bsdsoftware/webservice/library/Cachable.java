package it.bsdsoftware.webservice.library;

/**
 * Created by Simone on 20/03/17.
 */

public interface Cachable {
    boolean isDataAlreadyCached();
    void setCacheData();
}
