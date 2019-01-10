package com.company.todolist.h2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTaskMatrix implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "user_task_matrix_id", nullable = false)
	private Long id;

	@ManyToOne (fetch=FetchType.LAZY , optional=false)
	@JoinColumn(name="user", referencedColumnName = "user_id" , nullable=false , unique=false , insertable=true, updatable=true)
	private User user;

	@ManyToOne (fetch=FetchType.LAZY , optional=false)
	@JoinColumn(name="task", referencedColumnName = "task_id" , nullable=false , unique=false , insertable=true, updatable=true)
	private Task task;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
}