package org.noname.designer.core.interfaces;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class VariableContext {
	private Map<String, Object> variables;
	private Stack<Object> stack;
	public VariableContext(){
		variables = new HashMap<String, Object>();
		stack = new Stack<Object>();
	}
	public void setVariable(String name, Object value){
		variables.put(name, value);
	}
	public Object getVariable(String name){
		return variables.get(name);
	}
	public Iterable<String> getVariableNames(){
		return variables.keySet();
	}
	public Stack<Object> getStack(){
		return stack;
	}
}
