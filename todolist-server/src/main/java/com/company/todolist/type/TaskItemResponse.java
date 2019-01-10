package com.company.todolist.type;

import java.util.Date;
import java.util.List;

public class TaskItemResponse {

	private Long taskItemId;
	private String name;
	private String description;
	private Date deadline;
	private String status;
	private Date creationDate;
	private List<TaskItemDependencyReponse> taskItemDependencyReponses;

	public Long getTaskItemId() {
		return taskItemId;
	}

	public void setTaskItemId(Long taskItemId) {
		this.taskItemId = taskItemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public List<TaskItemDependencyReponse> getTaskItemDependencyReponses() {
		return taskItemDependencyReponses;
	}

	public void setTaskItemDependencyReponses(List<TaskItemDependencyReponse> taskItemDependencyReponses) {
		this.taskItemDependencyReponses = taskItemDependencyReponses;
	}
}