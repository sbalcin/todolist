package com.company.todolist.type;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"taskId",
		"name",
		"creationDate",
		"taskItems"
})
public class TaskResponse {

	private Long taskId;
	private String name;
	private Date creationDate;
	private List<TaskItemResponse> taskItems;

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

	public List<TaskItemResponse> getTaskItems() {
		return taskItems;
	}

	public void setTaskItems(List<TaskItemResponse> taskItems) {
		this.taskItems = taskItems;
	}
}