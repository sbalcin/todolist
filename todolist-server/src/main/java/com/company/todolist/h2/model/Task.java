package com.company.todolist.h2.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

	@Id
	@GeneratedValue
	@Column(name = "task_id", nullable = false)
	private Long taskId;

	private String name;
	private Date creationDate;

	@OneToMany (targetEntity=TaskItem.class, fetch=FetchType.LAZY, mappedBy="task", cascade=CascadeType.DETACH)
	private Set<TaskItem> taskItems;


	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Set<TaskItem> getTaskItems() {
		return taskItems;
	}

	public void setTaskItems(Set<TaskItem> taskItems) {
		this.taskItems = taskItems;
	}
}