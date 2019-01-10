
/*export const signUpUri = 'http://localhost:8080/auth/signUp/';
export const signInUri = 'http://localhost:8080/auth/signIn/'
export const createTaskUri = 'http://localhost:8080/task/createTask/'
export const retrieveTasksUri = 'http://localhost:8080/task/retrieveTasks/'
export const createTaskItemUri = 'http://localhost:8080/task/createTaskItem/'
export const addDependencyUri = 'http://localhost:8080/task/addDependency/'
export const deleteDependencyUri = 'http://localhost:8080/task/deleteDependency/'
export const updateTaskItemStatusUri = 'http://localhost:8080/task/updateTaskItemStatus/'
export const deleteTaskItemUri = 'http://localhost:8080/task/deleteTaskItem/'
export const deleteTaskUri = 'http://localhost:8080/task/deleteTask/'*/


export const signUpUri = 'http://37.59.110.96:8080/todolist/auth/signUp/';
export const signInUri = 'http://37.59.110.96:8080/todolist/auth/signIn/'
export const createTaskUri = 'http://37.59.110.96:8080/todolist/task/createTask/'
export const retrieveTasksUri = 'http://37.59.110.96:8080/todolist/task/retrieveTasks/'
export const createTaskItemUri = 'http://37.59.110.96:8080/todolist/task/createTaskItem/'
export const addDependencyUri = 'http://37.59.110.96:8080/todolist/task/addDependency/'
export const deleteDependencyUri = 'http://37.59.110.96:8080/todolist/task/deleteDependency/'
export const updateTaskItemStatusUri = 'http://37.59.110.96:8080/todolist/task/updateTaskItemStatus/'
export const deleteTaskItemUri = 'http://37.59.110.96:8080/todolist/task/deleteTaskItem/'
export const deleteTaskUri = 'http://37.59.110.96:8080/todolist/task/deleteTask/'

export const generalRowLimit = 10;

export const currentUser = () => {
    let userKey = localStorage.getItem('userKey');
    let user = userKey ? userKey : undefined;
    return (
        user
    );
}