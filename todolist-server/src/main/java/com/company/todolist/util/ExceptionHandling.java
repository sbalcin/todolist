package com.company.todolist.util;

import com.company.todolist.enumeration.StatusCodes;
import com.company.todolist.type.TodoListFaultMessage;
import com.company.todolist.type.TodoListFaultType;
import org.apache.log4j.Logger;

public class ExceptionHandling {

	private static Logger logger = Logger.getLogger(ExceptionHandling.class);

	public static TodoListFaultMessage handleTicketException(StatusCodes statusCode) {
		logger.error(statusCode.toString());

		TodoListFaultType processingFault = new TodoListFaultType();

		processingFault.setCode(statusCode.getCode());
		processingFault.setMessage(statusCode.getMessage());
		return new TodoListFaultMessage(statusCode.getMessage(), processingFault);
	}

	public static TodoListFaultMessage handleTicketException(StatusCodes statusCode, Throwable e) {
		logger.error(statusCode.toString(), e);

		TodoListFaultType processingFault = new TodoListFaultType();

		processingFault.setCode(statusCode.getCode());
		processingFault.setMessage(statusCode.getMessage());
		return new TodoListFaultMessage(statusCode.getMessage(), processingFault);
	}

	public static TodoListFaultMessage handleTicketException(String message, Throwable e) {
		logger.error(message, e);

		TodoListFaultType processingFault = new TodoListFaultType();
		return new TodoListFaultMessage(message, processingFault);
	}

	public static Throwable handleException(Throwable e) {
		logger.error("Error !", e);
		e.printStackTrace();
		return e;
	}

	public static Throwable handleException(Throwable e, String message) {
		logger.error("Error !" + message, e);
		e.printStackTrace();
		return e;
	}

}
