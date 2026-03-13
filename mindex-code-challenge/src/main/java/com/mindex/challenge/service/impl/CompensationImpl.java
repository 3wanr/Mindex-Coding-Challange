package com.mindex.challenge.service.impl;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;

@Service
public class CompensationImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationImpl.class);
    private static final double BASE_SALARY = 95000.00;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompensationRepository compensationRepository;

    @Override
    public Compensation create(String employeeId) {
        LOG.debug("Creating employee compensation with employee [{}]", employeeId);

        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        
        Compensation compensation = new Compensation();
        compensation.setEmployee(employee);
        compensation.setSalary(calcSalary(employee));
        compensation.setEffectiveDate(LocalDate.now());

        compensationRepository.insert(compensation);
        return compensation;
    }

    @Override
    public Compensation read(String id) {
        LOG.debug("Creating employee compensation with employee id [{}]", id);

        Compensation compensation = compensationRepository.findByEmployee_EmployeeId(id);

        if (compensation == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return compensation;
    }

    @Override
    public double calcSalary(Employee employee) {
        int totalSubordinates = getTotalReports(employee);
        return BASE_SALARY + (2500.00 * totalSubordinates);
    }

    private int getTotalReports(Employee employee) {
        if (employee.getDirectReports() == null) return 0; // Base case if there is no subordinates for the employee

        // Recursively search entire report tree
        int total = employee.getDirectReports().size();
        for (Employee e : employee.getDirectReports()) {
            Employee filled = employeeRepository.findByEmployeeId(e.getEmployeeId()); // need to grab the subordinate employee from the db since direct reports only stores ids 
            total += getTotalReports(filled);
        }
        return total;
    }
}
