package com.epam.esm.utils.config;

import com.epam.esm.filter.ServiceAuthenticationFilter;
import com.epam.esm.utils.openfeign.AuthFeignClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.zalando.problem.jackson.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

@Configuration
public class CommerceApplicationConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModules(
                new ProblemModule(),
                new ConstraintViolationProblemModule(),
                new JavaTimeModule());
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ServiceAuthenticationFilter serviceAuthenticationFilter(AuthFeignClient authFeignClient){
        return new ServiceAuthenticationFilter(authFeignClient);
    }
}