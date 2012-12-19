package com.hortonworks.buildstats;

import com.offbytwo.jenkins.model.BaseModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jzhong
 * Date: 12/13/12
 * Time: 10:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestSuite extends BaseModel{
    List<TestCaseResult> cases;

    public TestSuite() {
        this(new ArrayList<TestCaseResult>());
    }

    public TestSuite(List<TestCaseResult> s) {
        this.cases = s;
    }

    public TestSuite(TestCaseResult... s) {
        this(Arrays.asList(s));
    }

    public List<TestCaseResult> getCases() {
        return cases;
    }

    public void setCases(List<TestCaseResult> s) {
        cases = s;
    }
}
