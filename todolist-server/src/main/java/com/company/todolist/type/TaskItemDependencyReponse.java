package com.company.todolist.type;

public class TaskItemDependencyReponse {

	private Long id;
	private Long relatedTaskItemId;
	private String relatedTaskItemName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRelatedTaskItemId() {
		return relatedTaskItemId;
	}

	public void setRelatedTaskItemId(Long relatedTaskItemId) {
		this.relatedTaskItemId = relatedTaskItemId;
	}

	public String getRelatedTaskItemName() {
		return relatedTaskItemName;
	}

	public void setRelatedTaskItemName(String relatedTaskItemName) {
		this.relatedTaskItemName = relatedTaskItemName;
	}
}