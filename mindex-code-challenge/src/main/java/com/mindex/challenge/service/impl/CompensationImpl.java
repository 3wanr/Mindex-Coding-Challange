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
        compensation.setEffectiveDate(LocalDate.now()); // sets to today's date

        compensationRepository.insert(compensation); // persist 
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

    /**
     * Calculates the salary for an employee based on a base salary plus an
     * additional pay for each subordinate in their reporting tree. Each subordinate
     * adds $2,500 to the base salary of $95,000.
     * 
     * @param employee the employee to calculate salary for
     * @return the total salary
     */
    private double calcSalary(Employee employee) {
        // Recursively count all subordinates in the reporting tree
        int totalSubordinates = getTotalReports(employee);

        // $2,500 bonus per subordinate added to base salary
        return BASE_SALARY + (2500.00 * totalSubordinates);
    }

    /**
     * Calculates the total number of subordinates under an employee by recursively
     * searching the reporting tree.
     * 
     * @param employee the employee to calculate the number of subordinates for
     * @return the number of subordinates for the employee
     */
    private int getTotalReports(Employee employee) {
        // Base case if there are no subordinates for the employee
        if (employee.getDirectReports() == null)
            return 0;

        // Recursively search entire report tree to find all subordinates
        int total = employee.getDirectReports().size();
        for (Employee e : employee.getDirectReports()) {
            Employee filled = employeeRepository.findByEmployeeId(e.getEmployeeId()); // need to grab the subordinate
                                                                                      // employee from the database
                                                                                      // since direct reports only
                                                                                      // stores employee ids
            total += getTotalReports(filled);
        }
        return total;
    }
}
