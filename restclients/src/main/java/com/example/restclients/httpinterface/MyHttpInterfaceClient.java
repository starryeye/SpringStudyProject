package com.example.restclients.httpinterface;

import org.springframework.web.service.annotation.GetExchange;

import java.util.Map;

public interface MyHttpInterfaceClient {

    @GetExchange("/v6/latest")
    Map getLatest();
}
