package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompensationController {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

    @Autowired
    private CompensationService CompensationService;

    @PostMapping("/Compensation/{id}")
    public Compensation create(@PathVariable String id) {
        LOG.debug("Received Compensation create request for id [{}]", id);

        return CompensationService.create(id);
    }

    @GetMapping("/Compensation/{id}")
    public Compensation read(@PathVariable String id) {
        LOG.debug("Received Compensation create request for id [{}]", id);

        return CompensationService.read(id);
    }
}
