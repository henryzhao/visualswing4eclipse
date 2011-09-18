package org.noname.designer.core.interfaces;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class EvaluationContext {
	private Object thisObject;
	private Map<String, Object> fields;
	private Deque<FrameContext> frames;
	public EvaluationContext(Object thisObj){
		thisObject = thisObj;
		fields = new HashMap<String, Object>();
		frames = new LinkedList<FrameContext>();
	}
	public Object getThisObject() {
		return thisObject;
	}
	public void setField(String name, Object value){
		fields.put(name, value);
	}
	public Object getField(String name){
		return fields.get(name);
	}
	public void push(FrameContext context){
		frames.addLast(context);
	}
	public FrameContext popup(){
		return frames.removeLast();
	}
	public FrameContext peek(){
		return frames.peekLast();
	}
	public boolean isEmpty(){
		return frames.isEmpty();
	}
	public Iterable<String> getFieldNames() {
		return fields.keySet();
	}
	public Iterable<FrameContext> getFrames() {
		return frames;
	}
}
