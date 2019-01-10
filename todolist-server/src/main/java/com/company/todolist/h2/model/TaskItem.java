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
public class TaskItem {

	@Id
	@GeneratedValue
	@Column(name = "task_item_id", nullable = false)
	private Long taskItemId;
	private String name;
	private String description;
	private Date deadline;
	private String status;
	private Date creationDate;

	@ManyToOne (fetch=FetchType.LAZY )
	@JoinColumn(name="task", referencedColumnName = "task_id" , nullable=true , unique=false , insertable=true, updatable=true)
	private Task task;

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

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
}