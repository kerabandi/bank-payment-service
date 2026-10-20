package com.bank.payment.service;

import com.bank.payment.context.RequestContext;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@ApplicationScoped
public class ScopeDemoService {

    private final UUID instanceId = UUID.randomUUID();

    @Inject
    RequestContext requestContext;

    @PostConstruct
    void init() {
        System.out.println(
                "[ScopeDemoService] CREATO: " + instanceId
        );
    }

    @PreDestroy
    void destroy() {
        System.out.println(
                "[ScopeDemoService] DISTRUTTO: " + instanceId
        );
    }

    public String getInfo() {

        return "Service instanceId: " + instanceId
               + "\nRequest requestId: "
               + requestContext.getRequestId();
    }
}
