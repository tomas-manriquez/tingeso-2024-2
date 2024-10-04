package com.example.demo.service;

import com.example.demo.repository.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditService {
    @Autowired
    CreditRepository creditRepository;
}
