jenkins-tools
=============

A tool which pulls test case results from Jenkins server. It displays a union of failed test cases from the last 15(could be less depending on availablity) runs recorded in Jenkins sever and track how each of them are performed for all the last 15 runs(passed, not run or failed)

Usage are: 
	java -jar buildstats-1.0-jar-with-dependencies.jar <Jenkins HTTP URL> <Job Name>

Sample commands are:
        java -jar buildstats-1.0-jar-with-dependencies.jar https://builds.apache.org HBase-TRUNK

Sample output:

Failed Test Cases              3621 3622 3623 3624 3625 3626 3627 3628 3629 3630 3632 3633 3634 3635
========================================================

org.apache.hadoop.hbase.catalog.testmetareadereditor.testretrying    1    1   -1    0    1    1    1    1   -1    0    1    1    1    1
org.apache.hadoop.hbase.client.testadmin.testdeleteeditunknowncolumnfamilyandortable    0    1    1    1   -1    0    1    1    0    1    1    1    1    1
org.apache.hadoop.hbase.client.testfromclientsidewithcoprocessor.testclientpoolthreadlocal    1    1    1    1    1    1    1    1    0    1    1   -1    0    1
org.apache.hadoop.hbase.client.testhcm.testregioncaching    1    1   -1    0    1    1   -1    0   -1    0   -1    0    1    1
org.apache.hadoop.hbase.client.testmultiparallel.testflushcommitswithabort    1    1    1    1    1    1    1    1   -1    0    1    1    1    1
org.apache.hadoop.hbase.client.testscannertimeout.test3686a    1    1    1    1    1    1    1    1   -1    0    1    1    1    1
org.apache.hadoop.hbase.coprocessor.example.testrowcountendpoint.org.apache.hadoop.hbase.coprocessor.example.testrowcountendpoint    0   -1    0   -1    0    0    0   -1    0    0    0    0    0    0
org.apache.hadoop.hbase.coprocessor.example.testzookeeperscanpolicyobserver.org.apache.hadoop.hbase.coprocessor.example.testzookeeperscanpolicyobserver    0   -1    0   -1    0    0    0   -1    0    0    0    0    0    0
org.apache.hadoop.hbase.master.testrollingrestart.testbasicrollingrestart    1    1    1    1   -1    0    1    1    1    1    1    1   -1    0
org.apache.hadoop.hbase.regionserver.testcompactionstate.testmajorcompaction    1    1   -1    0    1    1    1    1    1    1    1    1    1    1
org.apache.hadoop.hbase.regionserver.testcompactionstate.testminorcompaction    1    1   -1    0    1    1    1    1    1    1    1    1    1    1
org.apache.hadoop.hbase.replication.testreplication.loadtesting    1    1    1    1    1    1    1    1    1   -1    0    1    1    1
org.apache.hadoop.hbase.rest.client.testremoteadmin.org.apache.hadoop.hbase.rest.client.testremoteadmin    0    0    0    0    0    0    0    0   -1    0    0    0    0    0
org.apache.hadoop.hbase.rest.client.testremotetable.org.apache.hadoop.hbase.rest.client.testremotetable    0    0    0    0    0    0    0    0   -1    0    0    0    0    0
org.apache.hadoop.hbase.security.access.testtablepermissions.testbasicwrite    0    1    1    1    1    1    1    1    1    1    1    1    1   -1
org.apache.hadoop.hbase.testdrainingserver.testdrainingserverwithabort    1    1    1    1    1   -1    0    1    1    1    1    1   -1    0
org.apache.hadoop.hbase.util.testhbasefsck.testregionshouldnotbedeployed    1    1    1    1    1    1   -1    0   -1    0   -1   -1    0   -1


Notes: I've included jenkins-client in the source because I had a small modificatoin in the client source code.

