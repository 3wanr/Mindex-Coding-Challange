package com.mindex.challenge.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;

@Service
public class ReportingStructureImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Override
    public ReportingStructure create(Employee employee) {
        LOG.debug("Creating employee structure for [{}]", employee);

        ReportingStructure rStructure = new ReportingStructure();
        rStructure.setEmployee(employee);
        rStructure.setNumberOfReports(countReports(employee));

        return rStructure;
    }

    @Override
    public int countReports(Employee employee) {
        LOG.debug("Counting reports for [{}]", employee);

        if (employee.getDirectReports() == null) return 0; // base case for when an employee has no reportees

        // recursively find all employees that report to given employee
        int total = employee.getDirectReports().size();
        for (Employee e : employee.getDirectReports()) {
            total += countReports(e);
        }
        return total;        
    }
}
