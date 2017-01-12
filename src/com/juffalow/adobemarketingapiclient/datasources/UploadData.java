package com.juffalow.adobemarketingapiclient.datasources;

import com.juffalow.adobemarketingapiclient.AdobeMarketingApiClient;
import com.juffalow.adobemarketingapiclient.AuthenticationHeaderGenerator;
import com.juffalow.adobemarketingapiclient.DataSender;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The limits are set to 10 000 for rows per upload and 100 for uploads per job.
 * It is taken from their docs.
 * 
 * 
 * 
 * @see https://marketing.adobe.com/developer/documentation/data-sources/r-uploaddatadatasources
 * @see https://marketing.adobe.com/developer/api-explorer#DataSources.UploadData
 * @author Matej 'juffalow' Jellus <juffalow@juffalow.com>
 */
public class UploadData {
    /**
     * The number of rows of data per upload
     */
    protected final static int ROWS_LIMIT = 1;
    /**
     * The maximum number of uploads to a job
     */
    protected final static int JOB_LIMIT = 100;
    /**
     * The maximum allowed job size including all uploads is 50 MB of data.
     * This is set to 48, to be sure that it will not exceed the limit.
     */
    protected final static int MAXIMUM_UPLOAD_SIZE = 48 * 1024 * 1024;
    /**
     * Must match valid events and evars that the report suite has enabled.
     * It also requires a date. The format for the columns is "Date", "Evar X", "Event X"
     */
    protected String[] columns;
    /**
     * 
     */
    protected int dataSourceId;
    /**
     * 
     */
    protected String reportSuiteId;
    /**
     * This value is an array of arrays of strings. The number of strings in
     * each array must match the number of columns.
     */
    protected ArrayList<String[]> rows;
    /**
     * Approximate size of the request. If this reaches or exceeds the
     * <code>MAXIMUM_UPLOAD_SIZE</code>, the data has to be sent with
     * <code>finished</code> flag set to <code>true</code> and new job name
     * has to be generated.
     */
    protected long requestSize;
    /**
     * Count of uploads for actual job. If this reaches the <code>JOB_LIMIT</code>, the data
     * has to be sent with <code>finished</code> flag set to <code>true</code>
     * and new job name has to be generated.
     */
    protected int uploadsPerJob;
    
    protected DataSender dataSender;
    
    protected String jobNamePrefix;
    
    protected int jobCounter;
    
    protected AuthenticationHeaderGenerator authHeaderGenerator;
    
    public UploadData(DataSender dataSender, AuthenticationHeaderGenerator ahg) {
        this.requestSize = 0;
        this.uploadsPerJob = 1;
        this.dataSender = dataSender;
        this.jobCounter = 1;
        this.authHeaderGenerator = ahg;
    }
    
    /**
     * 
     * @param columns 
     */
    public void setColumns(String[] columns) {
        this.columns = columns;
    }
    
    /**
     * 
     * @param dataSourceId 
     */
    public void setDataSourceId(int dataSourceId) {
        this.dataSourceId = dataSourceId;
    }
    
    /**
     * 
     * @param reportSuiteId 
     */
    public void setReportSuiteId(String reportSuiteId) {
        this.reportSuiteId = reportSuiteId;
    }
    
    /**
     * 
     * @param values 
     */
    public void addRow(String[] values) throws Exception {
        if( this.rows != null && this.rows.size() == ROWS_LIMIT ) {
            this.flush(this.uploadsPerJob == JOB_LIMIT);
        }
        
        if( this.requestSize > MAXIMUM_UPLOAD_SIZE) {
            this.flush(true);
        }
        
        if( this.rows == null ) {
            this.rows = new ArrayList<String[]>();
        }
        
        this.rows.add(values);
        this.requestSize += this.getByteSize(values);
    }
    
    /**
     * 
     * @param values
     * @return 
     */
    protected long getByteSize(String[] values) {
        long size = 0;
        for( String value : values) {
            size += value.getBytes().length;
        }
        return size;
    }
    
    /**
     * 
     * @throws Exception
     */
    public void finish() throws Exception {
        this.flush(true);
    }
    
    /**
     * Post data to the server. If <code>finished</code> is set to <code>true</code>
     * that means, this job is over and everything is going over again.
     * @param finished
     * @throws Exception
     */
    protected void flush(boolean finished) throws Exception {
        if( this.rows == null || this.rows.size() == 0 ) {
            return;
        }
        
        DataSourcesRequest request = new DataSourcesRequest(this.columns, this.dataSourceId, finished, this.getJobName(), this.reportSuiteId, this.rows);
        String response = this.dataSender.post("https://api.omniture.com/admin/1.4/rest/?method=DataSources.UploadData", request, this.getHeaders());
        if( response.equals("false") ) {
            throw new Exception("Server responded with false!");
        }
        
        this.rows = null;
        this.uploadsPerJob++;
        
        if( finished) {
            this.requestSize = 0;
            this.uploadsPerJob = 1;
            this.jobCounter++;
        }
    }
    
    protected HashMap getHeaders() throws Exception {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put(this.authHeaderGenerator.getHeaderName(), this.authHeaderGenerator.getHeaderValue());
        return headers;
    }
    
    protected String getJobName() {
        if( this.jobNamePrefix == null ) {
            this.jobNamePrefix = "job-";
        }
        return "job-" + this.jobCounter;
    }
}
