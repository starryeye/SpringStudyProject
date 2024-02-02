package com.example.restclients.resttemplate;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class UsingRestTemplate {

    public void run() {

        //1. RestTemplate
        // 동시성 이슈 없음. 쓰레드간 공유 가능
        RestTemplate restTemplate = new RestTemplate();

//        String res = rt.getForObject("https://open.er-api.com/v6/latest", String.class);
//        System.out.println("res = " + res);
//
//        Map<String, Object> res = rt.getForObject("https://open.er-api.com/v6/latest", Map.class);
//        System.out.println(res.get("rates"));

        Map<String, Map<String, Double>> response = restTemplate.getForObject("https://open.er-api.com/v6/latest", Map.class);
        System.out.println("Using RestTemplate, KRW rates = " + response.get("rates").get("KRW"));
    }
}
