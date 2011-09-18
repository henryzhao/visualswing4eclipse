package org.noname.designer.core.interfaces;

import java.util.Deque;
import java.util.LinkedList;

public class FrameContext {
	private Object result;
	private Deque<VariableContext> variables;
	public FrameContext(){
		variables = new LinkedList<VariableContext>();
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public void push(VariableContext context){
		variables.addLast(context);
	}
	public VariableContext popup(){
		return variables.removeLast();
	}
	public VariableContext peek(){
		return variables.peekLast();
	}
	public boolean isEmpty(){
		return variables.isEmpty();
	}

	public Iterable<VariableContext> getVariables() {
		return variables;
	}	
}
