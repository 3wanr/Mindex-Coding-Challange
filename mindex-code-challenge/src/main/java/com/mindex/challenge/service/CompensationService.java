package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;

public interface CompensationService {
    Compensation create(String employeeId);
    Compensation read(String id);
    double calcSalary(Employee employee);
}
