package com.bank.payment.context;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.RequestScoped;

import java.util.UUID;

@RequestScoped
public class RequestContext {

    private final UUID requestId = UUID.randomUUID();

    @PostConstruct
    void init() {
        System.out.println(
                "[RequestContext] CREATO: " + requestId
        );
    }

    @PreDestroy
    void destroy() {
        System.out.println(
                "[RequestContext] DISTRUTTO: " + requestId
        );
    }

    public UUID getRequestId() {
        return requestId;
    }
}
