package com.juffalow.adobemarketingapiclient.datasources;

import com.juffalow.adobemarketingapiclient.Request;
import java.util.ArrayList;
import com.google.gson.Gson;
/**
 *
 * @author Matej 'juffalow' Jellus <juffalow@juffalow.com>
 */
public class UploadDataRequest implements Request {

    protected String[] columns;

    protected int dataSourceID = 1;

    protected boolean finished;

    protected String jobName;

    protected String reportSuiteID;

    protected ArrayList<String[]> rows;

    public UploadDataRequest(String[] columns, int dataSourceId, boolean finished, String jobName, String reportSuiteId, ArrayList<String[]> rows) {
        this.columns = columns;
        this.dataSourceID = dataSourceId;
        this.finished = finished;
        this.jobName = jobName;
        this.reportSuiteID = reportSuiteId;
        this.rows = rows;
    }

    @Override
    public String toJSON() {
        Gson gson = new Gson();

        return gson.toJson(this);
    }
}
