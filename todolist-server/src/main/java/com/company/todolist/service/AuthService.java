package com.company.todolist.service;

import com.company.todolist.h2.model.User;
import com.company.todolist.h2.repository.AuthRepository;
import com.company.todolist.type.SignInRequest;
import com.company.todolist.type.SignInResponse;
import com.company.todolist.type.SignUpRequest;
import com.company.todolist.type.SignUpResponse;
import com.company.todolist.util.DateUtil;
import com.company.todolist.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;
import java.util.UUID;


@Service("authService")
public class AuthService {

    @Autowired
    AuthRepository repository;

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);


    public ResponseEntity<?> signIn(SignInRequest request) {
        try {
            SignInResponse signInResponse = new SignInResponse();

            User byEmail = repository.findByEmail(request.getEmail());
            if (byEmail == null) {
                signInResponse.setResult("user not found");
            } else if (PasswordUtil.decodeString(byEmail.getPassword()).equals(request.getPassword())) {
                signInResponse.setResult("success");

                String token = UUID.randomUUID().toString();
                byEmail.setToken(token);
                byEmail.setTokenExpire(DateUtil.addHourToDate(new Date(), 1));
                repository.save(byEmail);
                signInResponse.setToken(token);
            } else{
                signInResponse.setResult("incorrect password");
            }
            return new ResponseEntity<SignInResponse>(signInResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("signIn err => " + e.getMessage());

            if (e instanceof HttpClientErrorException) {
                final String responseStr = ((HttpClientErrorException) e).getResponseBodyAsString();
                return new ResponseEntity<String>(responseStr, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<String>("Unexpected error" + e.getMessage(), HttpStatus.BAD_REQUEST);
        } finally {
        }
    }

    public ResponseEntity<?> signUp(SignUpRequest request) {
        try {
            SignUpResponse signUpResponse = new SignUpResponse();

            User byEmail = repository.findByEmail(request.getEmail());
            if (byEmail != null) {
                signUpResponse.setResult("user already exist");

            } else {
                final User user = new User();
                user.setNameSurname(request.getNameSurname());
                user.setEmail(request.getEmail());
                user.setPassword(PasswordUtil.encodeString(request.getPassword()));
                user.setCreationDate(new Date());
                repository.save(user);

                signUpResponse.setResult("success");
            }

            return new ResponseEntity<SignUpResponse>(signUpResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("signUp err => " + e.getMessage());

            if (e instanceof HttpClientErrorException) {
                final String responseStr = ((HttpClientErrorException) e).getResponseBodyAsString();
                return new ResponseEntity<String>(responseStr, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<String>("Unexpected error" + e.getMessage(), HttpStatus.BAD_REQUEST);
        } finally {
        }
    }


}
