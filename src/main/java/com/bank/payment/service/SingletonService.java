package com.bank.payment.service;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;

@Singleton
public class SingletonService {

    public SingletonService() {
        System.out.println(
                "[Singleton] COSTRUTTORE"
        );
    }

    @PostConstruct
    void init() {
        System.out.println(
                "[Singleton] POST CONSTRUCT"
        );
    }

    public String hello() {
        return "Hello from Singleton";
    }
}

