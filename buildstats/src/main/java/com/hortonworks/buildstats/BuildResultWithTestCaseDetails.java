package com.hortonworks.buildstats;

import com.offbytwo.jenkins.model.BaseModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jzhong
 * Date: 12/12/12
 * Time: 6:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class BuildResultWithTestCaseDetails extends BaseModel {

    List<TestSuite> suites;

    /* default constructor needed for Jackson */
    public BuildResultWithTestCaseDetails() {
        this(new ArrayList<TestSuite>());
    }

    public BuildResultWithTestCaseDetails(List<TestSuite> s) {
        this.suites = s;
    }

    public BuildResultWithTestCaseDetails(TestSuite... s) {
        this(Arrays.asList(s));
    }

    public List<TestSuite> getSuites() {
        return suites;
    }

    public void setSuites(List<TestSuite> s) {
        suites = s;
    }
}
