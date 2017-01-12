package com.juffalow.adobemarketingapiclient;

import java.io.IOException;
import java.util.Map;

/**
 *
 * @author Matej 'juffalow' Jellus <juffalow@juffalow.com>
 */
public interface DataSender {
    /**
     * 
     * @param url
     * @param request
     * @return 
     * @throws IOException
     */
    public String post(String url, Request request, Map<String, String> headers) throws IOException;
}
