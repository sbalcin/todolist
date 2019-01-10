package com.company.todolist.h2.repository;

import com.company.todolist.h2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);
	User findByToken(String token);


}