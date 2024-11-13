package com.webapp.madrasati.util;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@Lazy
public class HttpUtils {

    private final RestTemplate restTemplate;

    public HttpUtils(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> T get(String url, Class<T> responseType) throws RestClientException {
        return restTemplate.getForObject(url, responseType);
    }

    public <T> T post(String url, Object requestBody, Class<T> responseType) throws RestClientException {
        return restTemplate.postForObject(url, requestBody, responseType);
    }

    public void put(String url, Object requestBody) throws RestClientException {
         restTemplate.put(url, requestBody);
    }

    public void delete(String url) throws RestClientException {
         restTemplate.delete(url);
    }

}