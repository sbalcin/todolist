package com.company.todolist.type;

import com.company.todolist.h2.model.UserTaskMatrix;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RetrieveTaskResponse {

    private String result;

    private List<TaskResponse> taskResponses;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<TaskResponse> getTaskResponses() {
        return taskResponses;
    }

    public void setTaskResponses(List<TaskResponse> taskResponses) {
        this.taskResponses = taskResponses;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}