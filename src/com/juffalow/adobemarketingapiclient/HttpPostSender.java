package com.juffalow.adobemarketingapiclient;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author Matej 'juffalow' Jellus <juffalow@juffalow.com>
 */
public class HttpPostSender implements DataSender {
    @Override
    public String post(String url, Request request, Map<String, String> headers) throws IOException {
        try {
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            
            for(Map.Entry<String, String> entry : headers.entrySet()) {
                con.setRequestProperty(entry.getKey(), entry.getValue());
            }
            
            con.setDoOutput(true);
            
            this.sendData(con, request);
            
            return this.readResponse(con);
        } catch (IOException ex) {
            throw ex;
        }
    }
    
    /**
     * 
     * @param con
     * @param request
     * @throws IOException 
     */
    protected void sendData(HttpsURLConnection con, Request request) throws IOException {
        DataOutputStream wr = null;
        try {
            wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(request.toJSON());
            wr.flush();
            wr.close();
        } catch(IOException exception) {
            throw exception;
        } finally {
            this.closeQuietly(wr);
        }
    }
    
    /**
     * 
     * @param con
     * @return
     * @throws IOException 
     */
    protected String readResponse(HttpsURLConnection con) throws IOException {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            return response.toString();
        } catch(IOException exception) {
            throw exception;
        } finally {
            this.closeQuietly(in);
        }
    }
    
    /**
     * 
     * @param closeable 
     */
    protected void closeQuietly(Closeable closeable) {
        try {
            if( closeable != null ) {
                closeable.close();
            }
        } catch(IOException ex) {
            
        }
    }
}
