package com.company.todolist.h2.repository;

import com.company.todolist.h2.model.Task;
import com.company.todolist.h2.model.TaskItem;
import com.company.todolist.h2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskItemRepository extends JpaRepository<TaskItem, Long> {

	TaskItem findByTaskItemId(Long taskItemId);

}