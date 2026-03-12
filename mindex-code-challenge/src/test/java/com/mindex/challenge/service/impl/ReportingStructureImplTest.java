package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String reportingStructureUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        reportingStructureUrl = "http://localhost:" + port + "/reportingStructure/{id}";
    }

    @Test
    public void testNoSubordinates() {
        Employee lone = restTemplate.postForEntity(employeeUrl, new Employee(), Employee.class).getBody();

        ReportingStructure rStructure = restTemplate.getForEntity(
                reportingStructureUrl, ReportingStructure.class, lone.getEmployeeId()
        ).getBody();

        assertNotNull(rStructure);
        assertEquals(0, rStructure.getNumberOfReports());
    }

    @Test
    public void testLennonReportingStructure() {
        String lennonId = "16a596ae-edd3-4847-99fe-c4518e82c86f";

        ReportingStructure rStructure = restTemplate.getForEntity(
                reportingStructureUrl, ReportingStructure.class, lennonId
        ).getBody();

        assertNotNull(rStructure);
        assertNotNull(rStructure.getEmployee());
        assertEquals(lennonId, rStructure.getEmployee().getEmployeeId());
        assertEquals(4, rStructure.getNumberOfReports());
    }

    @Test
    public void testRingoReportingStructure() {
        String RingoId = "03aa1462-ffa9-4978-901b-7c001562cf6f";

        ReportingStructure rStructure = restTemplate.getForEntity(
                reportingStructureUrl, ReportingStructure.class, RingoId
        ).getBody();

        assertNotNull(rStructure);
        assertNotNull(rStructure.getEmployee());
        assertEquals(RingoId, rStructure.getEmployee().getEmployeeId());
        assertEquals(2, rStructure.getNumberOfReports());
    }

    @Test
    public void testPaulReportingStructure() {
        String paulId = "b7839309-3348-463b-a7e3-5de1c168beb3";

        ReportingStructure rStructure = restTemplate.getForEntity(
                reportingStructureUrl, ReportingStructure.class, paulId
        ).getBody();

        assertNotNull(rStructure);
        assertNotNull(rStructure.getEmployee());
        assertEquals(paulId, rStructure.getEmployee().getEmployeeId());
        assertEquals(0, rStructure.getNumberOfReports());
    }

}
