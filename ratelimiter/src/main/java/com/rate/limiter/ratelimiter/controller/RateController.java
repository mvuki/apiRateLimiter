package com.rate.limiter.ratelimiter.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rate.limiter.ratelimiter.service.RateLimitService;



@RestController
public class RateController {
    @Autowired
    private  RateLimitService rateLimitService;

    @GetMapping("/my-endpoint")
    public String myEndpoint() {
        // Get client IP address from the request or use a custom logic
        String clientIpAddress = "127.0.0.1";

        if (rateLimitService.allowRequest(clientIpAddress)) {
            // Process the request
            return "Request processed successfully";
        } else {
            // process the request however indicate
            return "Rate limit exceeded";
        }
    }
    
}
