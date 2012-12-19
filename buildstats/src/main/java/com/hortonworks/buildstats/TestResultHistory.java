package com.hortonworks.buildstats;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;


public class TestResultHistory
{
    public final static String STATUS_REGRESSION = "REGRESSION";
    public final static String STATUS_FAILED = "FAILED";
    public final static String STATUS_PASSED = "PASSED";
    public final static String STATUS_FIXED  = "FIXED";
    public final static int BUILD_HISTORY_NUM = 15;

    private JenkinsHttpClient client;
    private String jobName;

    public TestResultHistory(String apacheHTTPURL, String jobName, String userName, String passWord)
        throws URISyntaxException {
        this.client = new JenkinsHttpClient(new URI(apacheHTTPURL), userName, passWord);
        this.jobName = jobName;
    }

    public static void main(String[] args) {

        if(args.length < 2) {
            printUsage();
            return;
        }

        String apacheHTTPUrl = args[0];
        String jobName = args[1];

        try {
            TestResultHistory buildHistory = new TestResultHistory(apacheHTTPUrl,  jobName, "", "");
            HistoryReport report = buildHistory.getReport();

            // display result in console
            System.out.printf("%-30s", "Failed Test Cases");
            for(Integer i : report.getBuildsWithTestResults()) {
                System.out.printf("%5d", i);
            }
            System.out.println("\n========================================================");
            SortedSet<String> keys = new TreeSet<String>(report.getHistoryResults().keySet());
            for (String failedTestCase : keys) {
                System.out.println();
                int[] resultHistory = report.getHistoryResults().get(failedTestCase);
                System.out.print(failedTestCase);
                for(int i = 0 ; i < resultHistory.length ; i++) {
                    System.out.printf("%5d", resultHistory[i]);
                }
            }
            System.out.println();
        } catch (Exception ex) {
            System.out.println("Got unexpected exception: " + ex.getMessage());
        }
    }


    protected static void printUsage() {
        System.out.println("<Jenkins HTTP URL> <Job Name>");
        System.out.println("Sample Input: \"https://builds.apache.org\" \"HBase-TRUNK-on-Hadoop-2.0.0\" ");
    }

    public HistoryReport getReport() {
        HistoryReport report = new HistoryReport();

        List<Integer> buildWithTestResults = new ArrayList<Integer>();
        Map<String, int[]> failureStats = new HashMap<String, int[]>();

        try {
            JenkinsServer jenkins = new JenkinsServer(this.client);
            Map<String, Job> jobs = jenkins.getJobs();
            JobWithDetails job = jobs.get(jobName.toLowerCase()).details();

            // build test case failures stats for the past 10 builds
            Build lastBuild = job.getLastBuild();
            int startingBuildNumber = (lastBuild.getNumber() - BUILD_HISTORY_NUM > 0) ?
                    lastBuild.getNumber() - BUILD_HISTORY_NUM + 1:
                    1;

            Map<Integer, HashMap<String, String>> executedTestCases = new HashMap<Integer, HashMap<String, String>>();

            String buildUrl = lastBuild.getUrl();
            for(int i = startingBuildNumber; i <= lastBuild.getNumber(); i++) {
                HashMap<String, String> buildExecutedTestCases = new HashMap<String, String>(2048);
                String curBuildUrl = buildUrl.replaceFirst("/" + lastBuild.getNumber(), "/" + i);
                List<String> failedCases = null;
                try {
                    failedCases = getBuildFailedTestCases(curBuildUrl, buildExecutedTestCases);
                    buildWithTestResults.add(i);
                } catch (Exception ex) {
                    // can't get result so skip it
                    continue;
                }
                executedTestCases.put(i, buildExecutedTestCases);

                // set test result failed cases of current build
                for(String curFailedTestCase : failedCases) {
                    if(failureStats.containsKey(curFailedTestCase)) {
                        int[] testCaseResultArray = failureStats.get(curFailedTestCase);
                        testCaseResultArray[i - startingBuildNumber] = -1;
                    } else {
                        int[] testResult = new int[BUILD_HISTORY_NUM];
                        testResult[i - startingBuildNumber] = -1;
                        // refill previous build test results for newly failed test case
                        for(int k = startingBuildNumber; k < i ; k++) {
                            HashMap<String, String> tmpBuildExecutedTestCases = executedTestCases.get(k);
                            if(tmpBuildExecutedTestCases != null
                                    && tmpBuildExecutedTestCases.containsKey(curFailedTestCase)) {
                                String statusStr = tmpBuildExecutedTestCases.get(curFailedTestCase);
                                testResult[k-startingBuildNumber] = convertStatusStringToInt(statusStr);
                            }
                        }
                        failureStats.put(curFailedTestCase, testResult);
                    }

                }

                // set test result for previous failed test cases
                for(String curTestCase : failureStats.keySet()) {
                    if(!failedCases.contains(curTestCase) && buildExecutedTestCases.containsKey(curTestCase)) {
                        String statusVal = buildExecutedTestCases.get(curTestCase);
                        int[] testCaseResultArray =  failureStats.get(curTestCase);
                        testCaseResultArray[i - startingBuildNumber] = convertStatusStringToInt(statusVal);
                    }
                }
            }

            report.setBuildsWithTestResults(buildWithTestResults);
            for (String failedTestCase : failureStats.keySet()) {
                int[] resultHistory = failureStats.get(failedTestCase);
                int[] compactHistory = new int[buildWithTestResults.size()];
                int index = 0;
                for(Integer i : buildWithTestResults) {
                    compactHistory[index] = resultHistory[i-startingBuildNumber];
                    index++;
                }
                failureStats.put(failedTestCase, compactHistory);
            }

            report.setHistoryResults(failureStats);

        } catch(Exception ex) {
            System.out.println(ex);
        }

        return report;
    }

    /**
     *
     * @param statusVal
     * @return 1 means PASSED, -1 means FAILED, 0 means SKIPPED
     */
     static int convertStatusStringToInt(String statusVal) {

        if(statusVal.equalsIgnoreCase(STATUS_REGRESSION)
                || statusVal.equalsIgnoreCase(STATUS_FAILED)) {
            return -1;
        } else if (statusVal.equalsIgnoreCase(STATUS_PASSED)){
            return 1;
        }

        return 0;
    }

    /**
     * Get failed test cases of a build
     *
     * @param buildURL Jenkins build job URL
     * @param executedTestCases Set of test cases which was executed for the build
     * @return list of failed test case names
     */
    List<String> getBuildFailedTestCases(String buildURL, HashMap<String, String> executedTestCases)
            throws IOException {
        List<String> result = new ArrayList<String>();

        String apiPath = urlJoin(buildURL,
                "testReport/api/json?depth=10&tree=suites[cases[className,name,status,failedSince]]");
        URI requestUri = URI.create(apiPath);

        List<TestSuite> suites =
                client.get(requestUri, BuildResultWithTestCaseDetails.class).getSuites();

        result = getTestSuiteFailedTestcase(suites, executedTestCases);

        return result;
    }

    private List<String> getTestSuiteFailedTestcase(List<TestSuite> suites, HashMap<String, String> executedTestCases) {
        List<String> result = new ArrayList<String>();

        if(suites == null) {
            return result;
        }

        for(TestSuite curTestSuite: suites) {
            for(TestCaseResult curTestCaseResult: curTestSuite.getCases()) {
                if(curTestCaseResult.getStatus().equalsIgnoreCase(STATUS_FAILED) ||
                        curTestCaseResult.getStatus().equalsIgnoreCase(STATUS_REGRESSION)) {
                    // failed test case
                    result.add(curTestCaseResult.getFullName());
                }
                executedTestCases.put(curTestCaseResult.getFullName(), curTestCaseResult.getStatus());
            }
        }

        return result;
    }

    String urlJoin(String path1, String path2) {
        if (!path1.endsWith("/")) {
            path1 += "/";
        }
        if (path2.startsWith("/")) {
            path2 = path2.substring(1);
        }
        return path1 + path2;
    }
}
