package com.hortonworks.buildstats;

/**
 * Created with IntelliJ IDEA.
 * User: jzhong
 * Date: 12/13/12
 * Time: 10:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestCaseResult {
        private String className;
        private int failedSince;
        private String name;
        private String status;

        public String getName() {
            return name;
        }

        public String getClassName() {
            return className;
        }

        public int failedSince() {
            return failedSince;
        }

        public String getStatus() {
            return status;
        }

        public void setName(String s) {
            name = s;
        }

        public void setClassName(String s) {
            className = s;
        }

        public void setFailedSince(int s) {
            failedSince = s;
        }

        public void setStatus(String s) {
            status = s;
        }

        public String getFullName() {
            return (this.className + "." + this.name).toLowerCase();
        }
}
