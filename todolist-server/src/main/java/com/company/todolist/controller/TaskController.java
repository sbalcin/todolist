package com.company.todolist.controller;

import com.company.todolist.service.AuthService;
import com.company.todolist.service.TaskService;
import com.company.todolist.type.*;
import lombok.AllArgsConstructor;
import org.hibernate.sql.Delete;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
@AllArgsConstructor
public class TaskController {


    private TaskService service;

    @PostMapping("/createTask/")
    public ResponseEntity<?> createTask(@RequestBody CreateTaskRequest request) {

        return service.createTask(request);
    }

    @PutMapping("/deleteTask/")
    public ResponseEntity<?> deleteTask(@RequestBody DeleteTaskRequest request) {

        return service.deleteTask(request);
    }

    @PostMapping("/retrieveTasks/")
    public ResponseEntity<?> retrieveTasks(@RequestBody RetrieveTaskRequest request) {

        return service.retrieveTasks(request);
    }

    @PostMapping("/createTaskItem/")
    public ResponseEntity<?> createTaskItem(@RequestBody CreateTaskItemRequest request) {

        return service.createTaskItem(request);
    }

    @PutMapping("/updateTaskItemStatus/")
    public ResponseEntity<?> updateTaskItemStatus(@RequestBody UpdateTaskItemStatusRequest request) {

        return service.updateTaskItemStatus(request);
    }

    @PutMapping("/deleteTaskItem/")
    public ResponseEntity<?> deleteTaskItem(@RequestBody DeleteTaskItemRequest request) {

        return service.deleteTaskItem(request);
    }


    @PostMapping("/addDependency/")
    public ResponseEntity<?> addDependency(@RequestBody AddDependencyRequest request) {

        return service.addDependency(request);
    }


    @PutMapping("/deleteDependency/")
    public ResponseEntity<?> deleteDependency(@RequestBody DeleteDependencyRequest request) {

        return service.deleteDependency(request);
    }

}