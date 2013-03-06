/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.hbase.devtools.buildstats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
