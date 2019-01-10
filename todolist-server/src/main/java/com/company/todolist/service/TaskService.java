package com.company.todolist.service;

import com.company.todolist.h2.model.*;
import com.company.todolist.h2.repository.*;
import com.company.todolist.type.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;


@Service("taskService")
public class TaskService {

    @Autowired
    AuthRepository authRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskItemRepository taskItemRepository;

    @Autowired
    UserTaskMatrixRepository userTaskMatrixRepository;

    @Autowired
    TaskItemDependencyRepository taskItemDependencyRepository;

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);


    @Transactional
    public ResponseEntity<?> createTask(CreateTaskRequest request) {
        try {
            User user = checkTokenIsActive(request.getToken());

            Task task = new Task();
            task.setName(request.getName());
            task.setCreationDate(new Date());
            Task savedTask = taskRepository.save(task);

            UserTaskMatrix userTaskMatrix = new UserTaskMatrix();
            userTaskMatrix.setTask(savedTask);
            userTaskMatrix.setUser(user);
            userTaskMatrixRepository.save(userTaskMatrix);

            CreateTaskResponse response = new CreateTaskResponse();
            response.setResult("success");
            return new ResponseEntity<CreateTaskResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("createTask err => " + e.getMessage());

            if (e instanceof HttpClientErrorException) {
                final String responseStr = ((HttpClientErrorException) e).getResponseBodyAsString();
                return new ResponseEntity<String>(responseStr, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<String>("Unexpected error" + e.getMessage(), HttpStatus.BAD_REQUEST);
        } finally {
        }
    }

    private User checkTokenIsActive(String token) {
        User user = authRepository.findByToken(token);

        //TODO check expire date

        return user;
    }

    @Transactional
    public ResponseEntity<?> deleteTask(DeleteTaskRequest request) {
        try {
            User user = checkTokenIsActive(request.getToken());
            Task task = taskRepository.findByTaskId(request.getTaskId());

            List<UserTaskMatrix> userTaskMatrices = userTaskMatrixRepository.findByTaskTaskId(request.getTaskId());
            for (UserTaskMatrix userTaskMatrix : userTaskMatrices) {
                userTaskMatrixRepository.delete(userTaskMatrix);
            }


            Set<TaskItem> taskItems = task.getTaskItems();
            for (TaskItem taskItem : taskItems) {
                final List<TaskItemDependency> taskItemDependencies = new ArrayList<>();
                taskItemDependencyRepository.findByTaskItemTaskItemId(taskItem.getTaskItemId()).forEach(taskItemDependency -> {
                    taskItemDependencies.add(taskItemDependency);
                });
                for (TaskItemDependency taskItemDependency : taskItemDependencies) {
                    taskItemDependencyRepository.delete(taskItemDependency);
                }
                taskItemDependencyRepository.findByDependentTaskTaskItemId(taskItem.getTaskItemId()).forEach(taskItemDependency -> {
                    taskItemDependencies.add(taskItemDependency);
                });
                for (TaskItemDependency taskItemDependency : taskItemDependencies) {
                    taskItemDependencyRepository.delete(taskItemDependency);
                }
                taskItemRepository.delete(taskItem);
            }
            Task byTaskId = taskRepository.findByTaskId(task.getTaskId());
            taskRepository.delete(byTaskId);

            DeleteTaskResponse response = new DeleteTaskResponse();
            response.setResult("success");
            return new ResponseEntity<DeleteTaskResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("deleteTask err => " + e.getMessage());

            if (e instanceof HttpClientErrorException) {
                final String responseStr = ((HttpClientErrorException) e).getResponseBodyAsString();
                return new ResponseEntity<String>(responseStr, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<String>("Unexpected error" + e.getMessage(), HttpStatus.BAD_REQUEST);
        } finally {
        }
    }

    public ResponseEntity<?> retrieveTasks(RetrieveTaskRequest request) {
        try {
            User user = checkTokenIsActive(request.getToken());
            if(user == null)
                return new ResponseEntity<String>("User does not exist", HttpStatus.BAD_REQUEST);


            final List<UserTaskMatrix> userTaskMatrices = new ArrayList<>();
            userTaskMatrixRepository.findByUserUserId(user.getUserId()).forEach(userTaskMatrice -> {
                userTaskMatrices.add(userTaskMatrice);
            });

            userTaskMatrices.sort(Comparator.comparing(UserTaskMatrix::getId));

            RetrieveTaskResponse response = new RetrieveTaskResponse();
            List<TaskResponse> taskResponses = new ArrayList<>();
            for (UserTaskMatrix userTaskMatrix : userTaskMatrices) {
                Task task = userTaskMatrix.getTask();
                TaskResponse taskResponse = new TaskResponse();
                taskResponse.setTaskId(task.getTaskId());
                taskResponse.setName(task.getName());
                taskResponse.setCreationDate(task.getCreationDate());

                List<TaskItemResponse> taskItemResponses = new ArrayList<>();
                List<TaskItem> taskItems = task.getTaskItems().stream().collect(Collectors.toList());
                taskItems.sort(Comparator.comparing(TaskItem::getCreationDate));

                for (TaskItem taskItem : taskItems) {
                    TaskItemResponse taskItemResponse = new TaskItemResponse();
                    taskItemResponse.setTaskItemId(taskItem.getTaskItemId());
                    taskItemResponse.setName(taskItem.getName());
                    taskItemResponse.setDescription(taskItem.getDescription());
                    taskItemResponse.setDeadline(taskItem.getDeadline());
                    taskItemResponse.setStatus(taskItem.getStatus());
                    taskItemResponse.setCreationDate(taskItem.getCreationDate());

                    List<TaskItemDependencyReponse> taskItemDependencyReponses = new ArrayList<>();
                    List<TaskItemDependency> taskItemDependencies = taskItemDependencyRepository.findByTaskItemTaskItemId(taskItem.getTaskItemId());
                    for (TaskItemDependency taskItemDependency : taskItemDependencies) {
                        TaskItemDependencyReponse taskItemDependencyReponse = new TaskItemDependencyReponse();
                        taskItemDependencyReponse.setId(taskItemDependency.getId());
                        taskItemDependencyReponse.setRelatedTaskItemId(taskItemDependency.getDependentTask().getTaskItemId());
                        taskItemDependencyReponse.setRelatedTaskItemName(taskItemDependency.getDependentTask().getName());
                        taskItemDependencyReponses.add(taskItemDependencyReponse);
                    }
                    taskItemResponse.setTaskItemDependencyReponses(taskItemDependencyReponses);
                    taskItemResponses.add(taskItemResponse);
                }

                taskResponse.setTaskItems(taskItemResponses);
                taskResponses.add(taskResponse);
            }
            response.setTaskResponses(taskResponses);
            response.setResult("success");
            return new ResponseEntity<RetrieveTaskResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("retrieveTasks err => " + e.getMessage());

            if (e instanceof HttpClientErrorException) {
                final String responseStr = ((HttpClientErrorException) e).getResponseBodyAsString();
                return new ResponseEntity<String>(responseStr, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<String>("Unexpected error" + e.getMessage(), HttpStatus.BAD_REQUEST);
        } finally {
        }
    }

    @Transactional
    public ResponseEntity<?> createTaskItem(CreateTaskItemRequest request) {
        try {
            User user = checkTokenIsActive(request.getToken());
            Task task = taskRepository.findByTaskId(request.getTaskId());

            TaskItem taskItem = new TaskItem();
            taskItem.setTask(task);
            taskItem.setName(request.getName());
            taskItem.setDescription(request.getDescription());
            taskItem.setDeadline(request.getDeadline());
            taskItem.setStatus(request.getStatus());
            taskItem.setCreationDate(new Date());
            taskItemRepository.save(taskItem);

            CreateTaskItemResponse response = new CreateTaskItemResponse();
            response.setResult("success");
            return new ResponseEntity<CreateTaskItemResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("createTaskItem err => " + e.getMessage());

            if (e instanceof HttpClientErrorException) {
                final String responseStr = ((HttpClientErrorException) e).getResponseBodyAsString();
                return new ResponseEntity<String>(responseStr, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<String>("Unexpected error" + e.getMessage(), HttpStatus.BAD_REQUEST);
        } finally {
        }
    }

    public ResponseEntity<?> updateTaskItemStatus(UpdateTaskItemStatusRequest request) {
        try {
            UpdateTaskItemStatusResponse response = new UpdateTaskItemStatusResponse();

            User user = checkTokenIsActive(request.getToken());
            Task task = taskRepository.findByTaskId(request.getTaskId());
            TaskItem taskItem = taskItemRepository.findByTaskItemId(request.getTaskItemId());

            final List<TaskItemDependency> taskItemDependencies = new ArrayList<>();
            taskItemDependencyRepository.findByTaskItemTaskItemId(request.getTaskItemId()).forEach(taskItemDependency -> {
                taskItemDependencies.add(taskItemDependency);
            });
            for (TaskItemDependency taskItemDependency : taskItemDependencies) {
                if(!"c".equals(taskItemDependency.getDependentTask().getStatus()))  {
                    response.setResult("complete dependent task items firstly");
                    return new ResponseEntity<UpdateTaskItemStatusResponse>(response, HttpStatus.OK);
                }
            }

            taskItem.setStatus(request.getNewStatus());
            taskItemRepository.save(taskItem);
            response.setResult("success");

            return new ResponseEntity<UpdateTaskItemStatusResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("updateTaskItemStatus err => " + e.getMessage());

            if (e instanceof HttpClientErrorException) {
                final String responseStr = ((HttpClientErrorException) e).getResponseBodyAsString();
                return new ResponseEntity<String>(responseStr, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<String>("Unexpected error" + e.getMessage(), HttpStatus.BAD_REQUEST);
        } finally {
        }
    }

    @Transactional
    public ResponseEntity<?> deleteTaskItem(DeleteTaskItemRequest request) {
        try {
            User user = checkTokenIsActive(request.getToken());
            Task task = taskRepository.findByTaskId(request.getTaskId());
            TaskItem taskItem = taskItemRepository.findByTaskItemId(request.getTaskItemId());

            List<TaskItemDependency> taskItemDependencies = taskItemDependencyRepository.findByDependentTaskTaskItemId(request.getTaskItemId());
            for (TaskItemDependency taskItemDependency : taskItemDependencies) {
                taskItemDependencyRepository.delete(taskItemDependency);
            }
            taskItemDependencies = taskItemDependencyRepository.findByTaskItemTaskItemId(request.getTaskItemId());
            for (TaskItemDependency taskItemDependency : taskItemDependencies) {
                taskItemDependencyRepository.delete(taskItemDependency);
            }

            taskItemRepository.delete(taskItem);

            DeleteTaskItemResponse response = new DeleteTaskItemResponse();
            response.setResult("success");
            return new ResponseEntity<DeleteTaskItemResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("deleteTaskItem err => " + e.getMessage());

            if (e instanceof HttpClientErrorException) {
                final String responseStr = ((HttpClientErrorException) e).getResponseBodyAsString();
                return new ResponseEntity<String>(responseStr, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<String>("Unexpected error" + e.getMessage(), HttpStatus.BAD_REQUEST);
        } finally {
        }
    }

    public ResponseEntity<?> addDependency(AddDependencyRequest request) {
        try {
            User user = checkTokenIsActive(request.getToken());
            Task task = taskRepository.findByTaskId(request.getTaskId());
            TaskItem taskItem = taskItemRepository.findByTaskItemId(request.getTaskItemId());
            TaskItem dependentTaskItem = taskItemRepository.findByTaskItemId(request.getDependentTaskItemId());


            TaskItemDependency taskItemDependency = new TaskItemDependency();
            taskItemDependency.setTaskItem(taskItem);
            taskItemDependency.setDependentTask(dependentTaskItem);

            taskItemDependencyRepository.save(taskItemDependency);



            AddDependencyResponse response = new AddDependencyResponse();
            response.setResult("success");
            return new ResponseEntity<AddDependencyResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("addDependency err => " + e.getMessage());

            if (e instanceof HttpClientErrorException) {
                final String responseStr = ((HttpClientErrorException) e).getResponseBodyAsString();
                return new ResponseEntity<String>(responseStr, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<String>("Unexpected error" + e.getMessage(), HttpStatus.BAD_REQUEST);
        } finally {
        }
    }

    @Transactional
    public ResponseEntity<?> deleteDependency(DeleteDependencyRequest request) {
        try {
            User user = checkTokenIsActive(request.getToken());
            Task task = taskRepository.findByTaskId(request.getTaskId());
            TaskItem taskItem = taskItemRepository.findByTaskItemId(request.getTaskItemId());
            TaskItem dependentTaskItem = taskItemRepository.findByTaskItemId(request.getDependentTaskItemId());
            TaskItemDependency taskItemDependency = taskItemDependencyRepository.findById(request.getDependentTaskItemId());

            taskItemDependencyRepository.delete(taskItemDependency);

            DeleteDependencyResponse response = new DeleteDependencyResponse();
            response.setResult("success");
            return new ResponseEntity<DeleteDependencyResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("deleteDependency err => " + e.getMessage());

            if (e instanceof HttpClientErrorException) {
                final String responseStr = ((HttpClientErrorException) e).getResponseBodyAsString();
                return new ResponseEntity<String>(responseStr, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<String>("Unexpected error" + e.getMessage(), HttpStatus.BAD_REQUEST);
        } finally {
        }
    }


}
