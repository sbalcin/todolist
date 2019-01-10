package com.company.todolist.h2.repository;

import com.company.todolist.h2.model.TaskItem;
import com.company.todolist.h2.model.TaskItemDependency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskItemDependencyRepository extends JpaRepository<TaskItemDependency, Long> {

	TaskItemDependency findById(Long id);
	List<TaskItemDependency> findByTaskItemTaskItemId(Long taskItemId);
	List<TaskItemDependency> findByDependentTaskTaskItemId(Long taskItemId);

}