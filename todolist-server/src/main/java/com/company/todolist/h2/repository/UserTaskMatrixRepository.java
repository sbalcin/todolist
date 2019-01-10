package com.company.todolist.h2.repository;

import com.company.todolist.h2.model.Task;
import com.company.todolist.h2.model.User;
import com.company.todolist.h2.model.UserTaskMatrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTaskMatrixRepository extends JpaRepository<UserTaskMatrix, Long> {

	List<UserTaskMatrix> findByUserUserId(Long userId);
	List<UserTaskMatrix> findByTaskTaskId(Long taskId);

}