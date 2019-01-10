package com.company.todolist.controller;

import com.company.todolist.service.AuthService;
import com.company.todolist.type.SignInRequest;
import com.company.todolist.type.SignUpRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {


    private AuthService service;

    @PostMapping("/signIn/")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request) {

        return service.signIn(request);
    }

    @PostMapping("/signUp/")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {

        return service.signUp(request);
    }
}