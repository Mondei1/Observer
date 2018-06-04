package de.Mondei1.utils.backend;

import org.apache.http.HttpResponse;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public interface ResponseInterface {

    /**
     *
     * @param res Contains the content from the response.
     */
    void onResponse(HttpResponse res, Exception err) throws ParseException, IOException;

}
