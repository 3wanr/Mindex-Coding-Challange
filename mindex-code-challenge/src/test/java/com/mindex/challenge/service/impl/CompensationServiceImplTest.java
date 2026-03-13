package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
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

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String compensationIdServiceUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationIdServiceUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    @Test
    public void testCreateReadLennonCompensation() {
        // Lennon has a total of 4 subordinates so salary = 95,000 + (2500 * 4) = 105,000
        String lennonId = "16a596ae-edd3-4847-99fe-c4518e82c86f";

        Compensation createdCompensation = restTemplate.postForEntity(
                compensationIdServiceUrl,
                null,
                Compensation.class,
                lennonId).getBody();

        // create checks
        assertNotNull(createdCompensation);
        assertEquals(105000.00, createdCompensation.getSalary(), 0);
        assertEquals(LocalDate.now(), createdCompensation.getEffectiveDate());
        assertNotNull(createdCompensation.getEmployee());
        assertEquals(lennonId, createdCompensation.getEmployee().getEmployeeId());

        Compensation readCompensation = restTemplate.getForEntity(
                compensationIdServiceUrl,
                Compensation.class,
                lennonId).getBody();

        // read checks
        assertNotNull(readCompensation);
        assertEquals(105000.00, readCompensation.getSalary(), 0);
        assertEquals(LocalDate.now(), readCompensation.getEffectiveDate());
        assertNotNull(readCompensation.getEmployee());
        assertEquals(lennonId, readCompensation.getEmployee().getEmployeeId());
    }
}
