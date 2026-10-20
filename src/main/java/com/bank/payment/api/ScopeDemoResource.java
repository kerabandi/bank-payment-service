package com.bank.payment.api;

import com.bank.payment.service.ScopeDemoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/demo/scope")
public class ScopeDemoResource {

    @Inject
    ScopeDemoService service;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String testScope() {
        return service.getInfo();
    }
}
