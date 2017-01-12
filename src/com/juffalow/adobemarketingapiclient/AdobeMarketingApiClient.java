package com.juffalow.adobemarketingapiclient;

import com.juffalow.adobemarketingapiclient.datasources.UploadData;

/**
 *
 * @author Matej 'juffalow' Jellus <juffalow@juffalow.com>
 */
public class AdobeMarketingApiClient {
    /**
     * Adobe username
     */
    protected String username;
    /**
     * Adobe secret
     */
    protected String secret;
    
    /**
     * 
     * @param username
     * @param secret 
     */
    public AdobeMarketingApiClient(String username, String secret) {
        this.username = username;
        this.secret = secret;
    }
    
    /**
     * Returns specific method from DataSources API.
     * 
     * <i>I don't know if there is any common thing among the methods, so I
     * used <code>Object</code> as the return type instead of some <code>interface</code>.</i>
     * @param method DataSources API method name
     * @param dataSender your own <code>DataSender</code>, or <code>null</code>
     * @param ahg your own <code>AuthenticationHeaderGenerator</code>, or <code>null</code>
     * @return DataSources API method object, or <code>null</code>
     */
    public Object getDataSourcesMethod(String method, DataSender dataSender, AuthenticationHeaderGenerator ahg) {
        if( method.equals("UploadData") ) {
            return new UploadData((dataSender != null) ? dataSender : this.getDataSender(), (ahg != null) ? ahg : this.getAuthenticationHeaderGenerator());
        }
        return null;
    }
    
    /**
     * 
     * @return default DataSender
     */
    protected DataSender getDataSender() {
        return new HttpPostSender();
        //return new EchoSender(); for testing
    }
    
    /**
     * 
     * @return default AuthenticationHeaderGenerator
     */
    protected AuthenticationHeaderGenerator getAuthenticationHeaderGenerator() {
        return new WSSEAuthentication(this.username, this.secret);
    }
}
