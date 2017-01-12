package com.juffalow.adobemarketingapiclient;

/**
 *
 * @author Matej 'juffalow' Jellus <juffalow@juffalow.com>
 */
public interface AuthenticationHeaderGenerator {
    public String getHeaderName();
    
    public String getHeaderValue() throws Exception;
}
