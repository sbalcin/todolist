package com.company.todolist;

import com.company.todolist.service.AuthService;
import com.company.todolist.service.TaskService;
import com.company.todolist.type.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskServiceTest {

	@Autowired
	TaskService taskService;

	@Autowired
	AuthService authService;


	@Test
	public void create_task_expected_behaviour() {

		SignUpRequest signUpRequest = new SignUpRequest();
		signUpRequest.setEmail("test@test.com");
		signUpRequest.setPassword("test");

		authService.signUp(signUpRequest);

		SignInRequest signInRequest = new SignInRequest();
		signInRequest.setEmail("test@test.com");
		signInRequest.setPassword("test");

		ResponseEntity<?> responseEntity = authService.signIn(signInRequest);
		String token = ((SignInResponse) responseEntity.getBody()).getToken();

		CreateTaskRequest request = new CreateTaskRequest();
		request.setToken(token);
		request.setName("Test Task");
		ResponseEntity<?> task = taskService.createTask(request);
		Assert.assertTrue(((CreateTaskResponse) task.getBody()).getResult().equals("success"));
	}

}