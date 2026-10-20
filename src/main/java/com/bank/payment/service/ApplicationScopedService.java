package com.bank.payment.service;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ApplicationScopedService {

    public ApplicationScopedService() {
        System.out.println(
                "[ApplicationScoped] COSTRUTTORE"
        );
    }

    @PostConstruct
    void init() {
        System.out.println(
                "[ApplicationScoped] POST CONSTRUCT"
        );
    }

    public String hello() {
        return "Hello from ApplicationScoped";
    }
}
