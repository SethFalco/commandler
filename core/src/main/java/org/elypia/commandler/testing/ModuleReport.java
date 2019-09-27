/*
 * Copyright 2019-2019 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.commandler.testing;

import java.util.*;

/**
 * @author seth@elypia.org (Syed Shah)
 */
public class ModuleReport implements Iterable<Report> {

    private List<Report> reports;

    public ModuleReport() {
        reports = new ArrayList<>();
    }

    public void add(Report report) {
        reports.add(report);
    }

    public List<Report> getReports() {
        return reports;
    }

    @Override
    public Iterator<Report> iterator() {
        return reports.iterator();
    }
}
