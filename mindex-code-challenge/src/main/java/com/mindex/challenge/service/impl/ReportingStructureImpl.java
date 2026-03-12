package com.mindex.challenge.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;

@Service
public class ReportingStructureImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure create(String employeeId) {
        LOG.debug("Creating employee reporting structure with employee id [{}]", employeeId);

        Employee employee = employeeRepository.findByEmployeeId(employeeId);

        if (employee == null) throw new RuntimeException("Invalid employeeId: " + employeeId);

        ReportingStructure rStructure = new ReportingStructure();
        rStructure.setEmployee(employee);
        rStructure.setNumberOfReports(countReports(employee));
        return rStructure;
    }

    @Override
    public int countReports(Employee employee) {
        if (employee.getDirectReports() == null) return 0; // Base case if there is no subordinates for the employee

        // Recursively search entire report tree
        int total = employee.getDirectReports().size();
        for (Employee e : employee.getDirectReports()) {
            Employee filled = employeeRepository.findByEmployeeId(e.getEmployeeId()); // need to grab the subordinate employee from the db since direct reports only stores ids 
            total += countReports(filled);
        }
        return total;
    }
}
