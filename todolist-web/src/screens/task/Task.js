import React from "react";
import "./Task.css";
import {
    createTaskUri,
    createTaskItemUri,
    retrieveTasksUri,
    addDependencyUri,
    deleteDependencyUri,
    updateTaskItemStatusUri, deleteTaskItemUri, deleteTaskUri
} from "../../config/constants";
import Responsive from "react-responsive";
import NotificationSystem from "react-notification-system";
import axios from "axios";
import LaddaButton from "react-ladda";
import Popup from 'reactjs-popup'
import moment from "moment/moment";
import CSSTransitionGroup from "react-transition-group/CSSTransitionGroup";


const Mobile = props => <Responsive {...props} maxWidth={767}/>;
const Default = props => <Responsive {...props} minWidth={768}/>;

var token;
var taskResponse;
var taskItemResponse;

var notiStyle = {
    NotificationItem: {
        DefaultStyle: {
            margin: '2px 2px 2px 2px'
        }
    }
}

export default class Task extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            popUpOpen: false,
            popUpConfirmButtonLoading: false,
            createTaskData: {name: ''},
            ctiData: {name: '', description: '', deadline: ''},
            taskResponses: [],
            currentTaskItems: [],
            taskItemDependencies: [],
            ctiPopUpOpen: false,
            dpPopUpOpen: false,
            selectedDependency: ''
        };
        token = localStorage.getItem('token');

        this.openModal = this.openModal.bind(this)
        this.closeModal = this.closeModal.bind(this)
        this.ctiCloseModal = this.ctiCloseModal.bind(this)
        this.dpCloseModal = this.dpCloseModal.bind(this)

        this.retrieveTasks();
    }

    addToNotifications(mess, level) {
        this._notificationSystem.addNotification({message: mess, level: level, position: 'tc'});
    }

    openModal() {
        this.setState({popUpOpen: true})
        this.setState({popUpConfirmButtonLoading: false})

    }

    closeModal() {
        this.setState({popUpOpen: false})
        this.setState({createTaskLoading: false})
    }

    ctiCloseModal() {
        this.setState({ctiPopUpOpen: false})
    }

    dpCloseModal() {
        this.setState({dpPopUpOpen: false})
    }

    componentWillMount() {

    }

    componentDidMount() {
        this._notificationSystem = this.refs.notificationSystem;
    }

    componentDidUpdate(prevProps, prevState) {

    }

    addToNotifications(mess, level) {
        this._notificationSystem.addNotification({message: mess, level: level, position: 'tc'});
    }

    createTaskNameChange = (e) => {
        this.state.createTaskData.name = e.target.value;
        window.fixMaterializeTextOverlap();
        this.forceUpdate();
    }

    ctiNameChange = (e) => {
        this.state.ctiData.name = e.target.value;
        window.fixMaterializeTextOverlap();
        this.forceUpdate();
    }

    ctiDescChange = (e) => {
        this.state.ctiData.description = e.target.value;
        window.fixMaterializeTextOverlap();
        this.forceUpdate();
    }

    ctiDeadlineChange = (e) => {
        this.state.ctiData.deadline = e.target.value;
        window.fixMaterializeTextOverlap();
        this.forceUpdate();
    }

    createTaskClicked = (e) => {
        try {
            e.preventDefault();
            this.setState({createTaskLoading: true})
            this.openModal();
        } catch (err) {
            console.log('err.. ' + err);
        }
    }

    createTaskItemClicked = (e, taskResponse) => {
        try {
            e.preventDefault();
            this.setState({ctiPopUpOpen: true})
            this.taskResponse = taskResponse;
        } catch (err) {
            console.log('err.. ' + err);
        }
    }

    ctiConfirmClicked = (e) => {
        e.preventDefault();
        var data = {
            token: token,
            taskId: this.taskResponse.taskId,
            name: this.state.ctiData.name,
            description: this.state.ctiData.description,
            deadline: new Date(this.state.ctiData.deadline),
            status: 'nc'
        };
        return axios.post(createTaskItemUri, data).then(response => {
            if (response.data.result == 'success') {
                this.addToNotifications('Task item creation completed successfully', 'success');
                this.setState({ctiPopUpOpen: false})
            }
            this.retrieveTasks();
        }).catch(err => {
            this.addToNotifications(err.toString(), 'error');
        });

    }


    popUpConfirmClicked = (e) => {
        e.preventDefault();
        this.setState({popUpConfirmButtonLoading: true})

        var data = {
            name: this.state.createTaskData.name,
            token: token
        };
        return axios.post(createTaskUri, data).then(response => {
            if (response.data.result == 'success') {
                this.addToNotifications('Task creation completed successfully', 'success');
                this.closeModal();
            }
            this.retrieveTasks();
            setTimeout(() => {
                this.setState({createTaskLoading: false})
            }, 100);
        }).catch(err => {
            this.addToNotifications(err.toString(), 'error');
            setTimeout(() => {
                this.setState({createTaskLoading: false})
            }, 100);
            throw err;
        });

    }

    popUpCancelClicked = (e) => {
        this.closeModal();
    }

    retrieveTasks() {
        var data = {
            token: token
        };
        return axios.post(retrieveTasksUri, data).then(response => {
            if (response.data.result == 'success') {
                this.setState({taskResponses: response.data.taskResponses})
            }
        }).catch(err => {
            this.addToNotifications(err.toString(), 'error');
        })
    }

    addDependency = (e, taskItem, taskResponse) => {
        try {
            e.preventDefault();
            this.taskResponse = taskResponse;
            this.taskItemResponse = taskItem;
            this.setState({
                dpPopUpOpen: true,
                currentTaskItems: taskResponse.taskItems,
                taskItemDependencies: taskItem.taskItemDependencyReponses
            })
        } catch (error) {
            console.log(error);
        }
    }

    deleteDependency = (e, taskItemDependency) => {
        e.preventDefault();
        var data = {
            token: token,
            taskId: this.taskResponse.taskId,
            taskItemId: this.taskItemResponse.taskItemId,
            dependentTaskItemId: taskItemDependency.id
        };
        return axios.put(deleteDependencyUri, data).then(response => {
            if (response.data.result == 'success') {
                this.addToNotifications('Dependency deletion completed successfully', 'success');
                this.setState({dpPopUpOpen: false})
            }
            this.retrieveTasks();
        }).catch(err => {
            this.addToNotifications(err.toString(), 'error');
        });
    }

    addDependencyConfirm = (e) => {
        e.preventDefault();
        var data = {
            token: token,
            taskId: this.taskResponse.taskId,
            taskItemId: this.taskItemResponse.taskItemId,
            dependentTaskItemId: this.state.selectedDependency
        };
        return axios.post(addDependencyUri, data).then(response => {
            if (response.data.result == 'success') {
                this.addToNotifications('Task dependency creation completed successfully', 'success');
                this.setState({dpPopUpOpen: false})
            }
            this.retrieveTasks();
        }).catch(err => {
            this.addToNotifications(err.toString(), 'error');
        });
    }

    markAsCompleted = (e, taskItem, taskResponse) => {
        e.preventDefault();
        var data = {
            token: token,
            taskId: taskResponse.taskId,
            taskItemId: taskItem.taskItemId,
            newStatus: 'c'
        };
        return axios.put(updateTaskItemStatusUri, data).then(response => {
            if (response.data.result == 'success') {
                this.addToNotifications('Operation completed successfully', 'success');
            } else
                this.addToNotifications(response.data.result, 'error');
            this.retrieveTasks();
        }).catch(err => {
            this.addToNotifications(err.toString(), 'error');
        });
    }

    deleteTaskItem = (e, taskItem, taskResponse) => {
        e.preventDefault();
        var data = {
            token: token,
            taskId: taskResponse.taskId,
            taskItemId: taskItem.taskItemId
        };
        return axios.put(deleteTaskItemUri, data).then(response => {
            if (response.data.result == 'success') {
                this.addToNotifications('Operation completed successfully', 'success');
            } else
                this.addToNotifications(response.data.result, 'error');
            this.retrieveTasks();
        }).catch(err => {
            this.addToNotifications(err.toString(), 'error');
        });
    }

    deleteTask = (e, taskResponse) => {
        e.preventDefault();
        var data = {
            token: token,
            taskId: taskResponse.taskId,
        };
        return axios.put(deleteTaskUri, data).then(response => {
            if (response.data.result == 'success') {
                this.addToNotifications('Operation completed successfully', 'success');
            } else
                this.addToNotifications(response.data.result, 'error');
            this.retrieveTasks();
        }).catch(err => {
            this.addToNotifications(err.toString(), 'error');
            this.retrieveTasks();
        });
    }


    render() {


        return (
            <div className="task-main-container">
                <NotificationSystem ref="notificationSystem" style={notiStyle} allowHTML={true}/>

                <div>
                    <LaddaButton loading={this.state.createTaskLoading} onClick={(e) => {
                        this.createTaskClicked(e)
                    }} className="btn btn-warning btn-rounded btn-block" data-style='zoom-out' style={{width: '20%'}}>
                        Create Task
                    </LaddaButton>

                </div>

                <br/>
                <br/>
                <div>

                    {this.state.taskResponses.map(taskResponse => (
                        <div className="flex-container">
                            <div className="card">
                                <div className="card-header list-group-item-success tabHeader">{taskResponse.name}</div>
                                <div className="card-body cardBody">
                                    <form>
                                        <div className="md-form form-sm">
                                            <table className="table table-hover table-sm">
                                                <thead className="mTableHeader">
                                                <tr>
                                                    <td>Name</td>
                                                    <td>Description</td>
                                                    <td>Deadline</td>
                                                    <td>Statu</td>
                                                    <td>Action</td>
                                                </tr>
                                                </thead>
                                                <CSSTransitionGroup transitionName="fade"
                                                                    transitionEnterTimeout={1000}
                                                                    transitionLeaveTimeout={1000}
                                                                    component="tbody">


                                                    {taskResponse.taskItems.map(taskItem => (
                                                        <tr key={taskItem.taskItemId}>
                                                            <td>{taskItem.name}</td>
                                                            <td>{taskItem.description}</td>
                                                            <td><span
                                                                className="date timeago"> {moment(taskItem.deadline).fromNow()}</span>
                                                            </td>
                                                            <td>{taskItem.status == 'c' ? 'Completed' : 'Not Completed'}</td>
                                                            <td>
                                                                <div style={{width: '150px', display: 'table'}}>
                                                                    <div style={{
                                                                        textAlign: 'left',
                                                                        display: 'table-cell'
                                                                    }}><i
                                                                        className="fa fa-check-circle tableActionAccept"
                                                                        onClick={(e) => {
                                                                            this.addDependency(e, taskItem, taskResponse)
                                                                        }} title="Add Dependency"/></div>
                                                                    <div style={{
                                                                        textAlign: 'left',
                                                                        display: 'table-cell'
                                                                    }}><i
                                                                        className="fa fa-map-marker tableActionAccept"
                                                                        onClick={(e) => {
                                                                            this.markAsCompleted(e, taskItem, taskResponse)
                                                                        }} title="Mark as completed"/></div>
                                                                    <div style={{
                                                                        textAlign: 'right',
                                                                        display: 'table-cell'
                                                                    }}><i
                                                                        className="fa fa-minus-circle tableActionReject"
                                                                        onClick={(e) => {
                                                                            this.deleteTaskItem(e, taskItem, taskResponse)
                                                                        }} title="Delete task Item"/></div>
                                                                </div>
                                                            </td>

                                                        </tr>
                                                    ))}
                                                </CSSTransitionGroup>
                                            </table>
                                        </div>

                                    </form>
                                </div>
                                <br/>
                                <br/>
                                <div className="actions">
                                    <LaddaButton style={{fontSize: 'small', float: 'left'}} onClick={(e) => {
                                        this.createTaskItemClicked(e, taskResponse)
                                    }} className="btn btn-default waves-effect transaction-tl-button"
                                                 data-style='zoom-out'>
                                        Add Task Item</LaddaButton>
                                    <LaddaButton style={{fontSize: 'small', float: 'right'}} onClick={(e) => {
                                        this.deleteTask(e, taskResponse)
                                    }} className="btn btn-default waves-effect transaction-tl-button"
                                                 data-style='zoom-out'>
                                        Delete Task</LaddaButton>
                                </div>
                            </div>
                            <br/>
                            <br/>
                        </div>

                    ))}


                </div>

                <Popup
                    open={this.state.popUpOpen}
                    closeOnDocumentClick
                    onClose={this.closeModal}>

                    <div className="conversionDetails">
                        <a className="close" onClick={this.closeModal}>
                            &times;
                        </a>
                        <br/>
                        <br/>
                        <div className="card">
                            <div className="card-header list-group-item-success tabHeader">Task Details</div>
                            <div className="card-body cardBody">
                                <form>
                                    <div className="md-form form-sm">
                                        <input type="text" className="form-control" id="taskName"
                                               value={this.state.createTaskData.taskName}
                                               onChange={this.createTaskNameChange}/>
                                        <label>Task Name</label>
                                    </div>

                                </form>
                            </div>
                            <br/>
                            <br/>
                            <div className="actions">
                                <LaddaButton style={{fontSize: 'small'}}
                                             loading={this.state.popUpConfirmButtonLoading} onClick={(e) => {
                                    this.popUpConfirmClicked(e)
                                }} className="btn btn-default waves-effect transaction-tl-button" data-style='zoom-out'>
                                    Confirm</LaddaButton>
                                <LaddaButton style={{fontSize: 'small'}} onClick={(e) => {
                                    this.popUpCancelClicked(e)
                                }} className="btn btn-default waves-effect transaction-tl-button" data-style='zoom-out'>
                                    Cancel</LaddaButton>
                            </div>
                        </div>
                        <br/>
                        <br/>
                    </div>
                </Popup>

                <Popup
                    open={this.state.ctiPopUpOpen}
                    closeOnDocumentClick
                    onClose={this.ctiCloseModal}>

                    <div className="conversionDetails">
                        <a className="close" onClick={this.ctiCloseModal}>
                            &times;
                        </a>
                        <br/>
                        <br/>
                        <div className="card">
                            <div className="card-header list-group-item-success tabHeader">Add Task Item Details</div>
                            <div className="card-body cardBody">
                                <form>
                                    <div className="md-form form-sm">
                                        <input type="text" className="form-control"
                                               value={this.state.ctiData.name}
                                               onChange={this.ctiNameChange}/>
                                        <label>Task Item Name</label>
                                    </div>
                                    <div className="md-form form-sm">
                                        <input type="text" className="form-control"
                                               value={this.state.ctiData.description}
                                               onChange={this.ctiDescChange}/>
                                        <label>Task Item Description</label>
                                    </div>
                                    <div className="md-form form-sm">
                                        <input type="text" className="form-control"
                                               value={this.state.ctiData.deadline}
                                               onChange={this.ctiDeadlineChange}/>
                                        <label>Task Item Deadline (MM/DD/YYYY)</label>
                                    </div>

                                </form>
                            </div>
                            <br/>
                            <br/>
                            <div className="actions">
                                <LaddaButton style={{fontSize: 'small'}}
                                             onClick={(e) => {
                                                 this.ctiConfirmClicked(e)
                                             }} className="btn btn-default waves-effect transaction-tl-button"
                                             data-style='zoom-out'>
                                    Confirm</LaddaButton>
                                <LaddaButton style={{fontSize: 'small'}} onClick={(e) => {
                                    this.setState({ctiPopUpOpen: false})
                                }} className="btn btn-default waves-effect transaction-tl-button" data-style='zoom-out'>
                                    Cancel</LaddaButton>
                            </div>
                        </div>
                        <br/>
                        <br/>
                    </div>
                </Popup>

                <Popup
                    open={this.state.dpPopUpOpen}
                    closeOnDocumentClick
                    onClose={this.dpCloseModal}>

                    <div className="conversionDetails">
                        <a className="close" onClick={this.dpCloseModal}>
                            &times;
                        </a>
                        <br/>
                        <br/>
                        <div className="card">
                            <div className="card-header list-group-item-success tabHeader">Add Dependency</div>
                            <div className="card-body cardBody">
                                <form>
                                    <div className="md-form form-sm">

                                        <label htmlFor="sel1">Dependent Task Item:</label>
                                        <select value={this.state.selectedDependency} className="dep-option"
                                                onChange={(e) => this.setState({selectedDependency: e.target.value})}>
                                            {this.state.currentTaskItems.map((team) => <option key={team.taskItemId}
                                                                                               value={team.taskItemId}>{team.name}</option>)}
                                        </select>

                                    </div>


                                </form>
                            </div>
                            <br/>
                            <br/>
                            <div className="actions">
                                <LaddaButton style={{fontSize: 'small'}}
                                             onClick={(e) => {
                                                 this.addDependencyConfirm(e)
                                             }} className="btn btn-default waves-effect transaction-tl-button"
                                             data-style='zoom-out'>
                                    Confirm</LaddaButton>
                                <LaddaButton style={{fontSize: 'small'}} onClick={(e) => {
                                    this.setState({dpPopUpOpen: false})
                                }} className="btn btn-default waves-effect transaction-tl-button" data-style='zoom-out'>
                                    Cancel</LaddaButton>
                            </div>
                        </div>
                        <br/>
                        <br/>
                        <div className="card">
                            <div className="card-header list-group-item-success tabHeader">Existing Dependencies</div>
                            <div className="card-body cardBody">
                                <form>

                                    <table className="table table-hover table-sm">
                                        <thead className="mTableHeader">
                                        <tr>
                                            <td>Name</td>
                                            <td>Action</td>
                                        </tr>
                                        </thead>

                                        {this.state.taskItemDependencies.map(taskItemDependency => (
                                            <tr key={taskItemDependency.id}>
                                                <td>{taskItemDependency.relatedTaskItemName}</td>

                                                <td>
                                                    <div style={{width: '75px', display: 'table'}}>
                                                        <div style={{
                                                            textAlign: 'left',
                                                            display: 'table-cell'
                                                        }}><i
                                                            className="fa fa-check-circle tableActionReject"
                                                            onClick={(e) => {
                                                                this.deleteDependency(e, taskItemDependency)
                                                            }} title="Delete Dependency"/></div>
                                                    </div>
                                                </td>

                                            </tr>
                                        ))}
                                    </table>

                                </form>
                            </div>
                            <br/>
                            <br/>
                        </div>
                        <br/>
                        <br/>
                    </div>
                </Popup>

            </div>
        )
    }
}
