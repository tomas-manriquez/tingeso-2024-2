package com.example.demo.controller;

import com.example.demo.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

}
