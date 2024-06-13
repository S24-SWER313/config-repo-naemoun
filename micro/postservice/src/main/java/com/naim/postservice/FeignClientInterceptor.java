package com.naim.postservice;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String jwt = AuthTokenFilter.t;
        if (jwt != null) {
            requestTemplate.header("Authorization", "Bearer " + jwt);
        }
    }
}
