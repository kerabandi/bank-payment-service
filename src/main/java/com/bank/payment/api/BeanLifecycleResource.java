package com.bank.payment.api;

import com.bank.payment.service.ApplicationScopedService;
import com.bank.payment.service.SingletonService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/api/demo/lifecycle")
public class BeanLifecycleResource {

    @Inject
    ApplicationScopedService applicationService;

    @Inject
    SingletonService singletonService;

    @GET
    @Path("/application")
    public String application() {
        return applicationService.hello();
    }

    @GET
    @Path("/singleton")
    public String singleton() {
        return singletonService.hello();
    }
}
