package com.company.todolist.h2.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskItemDependency {

	@Id
	@GeneratedValue
	@Column(name = "task_item_dependency_id", nullable = false)
	private Long id;

	@ManyToOne (fetch=FetchType.LAZY , optional=false)
	@JoinColumn(name="task_item_id", referencedColumnName = "task_item_id" , nullable=false , unique=false , insertable=true, updatable=true)
	private TaskItem taskItem;

	@ManyToOne (fetch=FetchType.LAZY , optional=false)
	@JoinColumn(name="dependent_task_id", referencedColumnName = "task_item_id" , nullable=false , unique=false , insertable=true, updatable=true)
	private TaskItem dependentTask;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TaskItem getTaskItem() {
		return taskItem;
	}

	public void setTaskItem(TaskItem taskItem) {
		this.taskItem = taskItem;
	}

	public TaskItem getDependentTask() {
		return dependentTask;
	}

	public void setDependentTask(TaskItem dependentTask) {
		this.dependentTask = dependentTask;
	}
}