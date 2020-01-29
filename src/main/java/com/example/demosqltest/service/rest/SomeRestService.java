package com.example.demosqltest.service.rest;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class SomeRestService {

    @Value("${some.service.url}")
    private String someUrl;
    RestTemplate restTemplate = new RestTemplate();
    Gson gson = new Gson();

    public String getInformationAboutStatus(String requestId) {
        HashMap<String, Map<String, String>> requestMap = new HashMap<>();
        requestMap.put("data", Map.of("request_id", requestId));
        String request = gson.toJson(requestMap);
        return restTemplate.postForObject(someUrl, request, String.class);
    }
}
