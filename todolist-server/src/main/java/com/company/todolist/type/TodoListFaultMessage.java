package com.company.todolist.type;

import javax.xml.ws.WebFault;

@WebFault(name = "TodoListFault")
public class TodoListFaultMessage extends Exception {

	private static final long serialVersionUID = 1L;

	private TodoListFaultType faultInfo;

	public TodoListFaultMessage(String message, TodoListFaultType faultInfo) {
		super(message);
		this.faultInfo = faultInfo;
	}

	public TodoListFaultMessage(String message, TodoListFaultType faultInfo, Throwable cause) {
		super(message, cause);
		this.faultInfo = faultInfo;
	}


	public TodoListFaultType getFaultInfo() {
		return faultInfo;
	}

}
