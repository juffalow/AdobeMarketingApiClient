package com.juffalow.adobemarketingapiclient;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;

/**
 *
 * @see https://marketing.adobe.com/developer/documentation/authentication-1/wsse-authentication-2#concept_687FE68B1E38430D9A8B8199C0B179C5
 * @author Matej 'juffalow' Jellus <juffalow@juffalow.com>
 */
public class WSSEAuthentication implements AuthenticationHeaderGenerator {
    
    protected String username;
    
    protected String secret;
    
    public WSSEAuthentication(String username, String secret) {
        this.username = username;
        this.secret = secret;
    }

    @Override
    public String getHeaderName() {
        return "X-WSSE";
    }

    /**
     * 
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException 
     */
    @Override
    public String getHeaderValue() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String nonce = getNonce();

        String created = getCreatedTime();
        String digest = getDigest(nonce, created);
        return "UsernameToken Username=\"" + username + "\", PasswordDigest=\"" + digest + "\", Nonce=\"" + base64Encode(nonce) + "\", Created=\"" + created + "\"";
    }
    
    /**
     * Generates random sequence
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException 
     */
    protected String getNonce() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String uuid = String.valueOf(Math.random());
        return md5(uuid);
    }
    
    /**
     * This is used for authentication
     * base64_encode(sha1(nonce + created + sharedSecret))
     * 
     * @param nonce
     * @param created
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException 
     */
    protected String getDigest(String nonce, String created) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.reset();
        md.update(nonce.getBytes("utf-8"));
        md.update(created.getBytes("utf-8"));
        md.update(secret.getBytes("utf-8"));
        
        return Base64.getEncoder().encodeToString(md.digest());
    }
    
    /**
     * Returns date and time in specified format.
     * !ACHTUNG! The server requires certain time zone, so returned time is - one hour!
     * @return YYYY-MM-DDTH:i:sZ
     */
    protected String getCreatedTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        return "" + currentTime.getYear() + "-" + String.format("%02d", currentTime.getMonthValue()) + "-" + String.format("%02d", currentTime.getDayOfMonth()) + "T" + String.format("%02d", (currentTime.getHour() - 1)) + ":" + String.format("%02d", currentTime.getMinute()) + ":" + String.format("%02d", currentTime.getSecond()) + "Z";
    }
    
    /**
     * Just MD5 hash function
     * @param str
     * @return
     * @throws NoSuchAlgorithmException 
     */
    protected String md5(String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(str.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
    }
    
    /**
     * Just base64 encode function
     * @param str
     * @return
     * @throws UnsupportedEncodingException 
     */
    protected String base64Encode(String str) throws UnsupportedEncodingException {
        return Base64.getEncoder().encodeToString(str.getBytes("utf-8"));
    }
    
}
