package com.juffalow.adobemarketingapiclient;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Matej 'juffalow' Jellus <juffalow@juffalow.com>
 */
public class EchoSender implements DataSender {
    @Override
    public String post(String url, Request request, Map<String, String> headers) {
        System.out.print("curl");
        
        for(Map.Entry<String, String> entry : headers.entrySet()) {
            System.out.print(" -H '" + entry.getKey() + ": " + entry.getValue() + "'");
        }
        
        System.out.print(" -XPOST '" + url + "' -d '");
        System.out.print(request.toJSON());
        System.out.print("'\n");
        return "true";
    }
}
