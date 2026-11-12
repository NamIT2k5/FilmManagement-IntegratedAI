package org.film.management.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.film.management.entity.Account;
import org.film.management.service.AccountService;

import java.util.Map;
import java.util.Objects;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    AccountService accountService;

    @POST
    @Path("/login")
    public Response login(LoginRequest credentials) {
        try {
            LoginResponse loginResponse = accountService.authenticate(credentials);

            return Response.ok(Objects.requireNonNullElseGet(loginResponse, () -> Map.of(
                    "success", false,
                    "error", "Email hoặc mật khẩu không chính xác"
            ))).build();

        } catch (Exception e) {
            return Response.ok(Map.of(
                    "success", false,
                    "error", e.getMessage()
            )).build();
        }
    }

    @POST
    @Path("/register")
    public Response register(Account newAccount) {
        String errorMessage = accountService.register(newAccount);

        if (errorMessage != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", errorMessage))
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity(Map.of("message", "Đăng ký thành công"))
                .build();
    }
}