package com.example.demo.controller;

import com.example.demo.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/credits")
@CrossOrigin("*")
public class CreditController {
    @Autowired
    CreditService creditService;
}
