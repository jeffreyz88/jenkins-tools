package com.hortonworks.buildstats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jzhong
 * Date: 12/15/12
 * Time: 5:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class HistoryReport {
    private List<Integer> buildsWithTestResults;
    private Map<String, int[]> historyResults;

    public HistoryReport() {
        buildsWithTestResults = new ArrayList<Integer>();
        this.historyResults = new HashMap<String, int[]>();
    }

    public Map<String, int[]> getHistoryResults() {
        return this.historyResults;
    }

    public List<Integer> getBuildsWithTestResults() {
        return this.buildsWithTestResults;
    }

    public void setBuildsWithTestResults(List<Integer> src) {
        this.buildsWithTestResults = src;
    }

    public void setHistoryResults(Map<String, int[]> src) {
        this.historyResults = src;
    }
}
