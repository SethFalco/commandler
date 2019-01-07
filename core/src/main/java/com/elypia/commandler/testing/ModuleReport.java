package com.elypia.commandler.testing;

import java.util.*;

public class ModuleReport implements Iterable<Report> {

    private List<Report> reports;

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
