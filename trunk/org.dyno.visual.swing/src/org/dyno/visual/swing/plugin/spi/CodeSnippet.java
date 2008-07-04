package org.dyno.visual.swing.plugin.spi;

import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;

import org.dyno.visual.swing.designer.Event;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class CodeSnippet implements IEventMethod {
	private WidgetAdapter adapter;
	private EventSetDescriptor eventSet;
	private MethodDescriptor methodDesc;
	private String code;

	public CodeSnippet(WidgetAdapter adapter, EventSetDescriptor eventSet,
			MethodDescriptor methodDesc, String code) {
		this.adapter = adapter;
		this.eventSet = eventSet;
		this.methodDesc = methodDesc;
		this.code = code;
	}

	@Override
	public void editCode() {
		WhiteBoard.sendEvent(new Event(this, Event.EVENT_SHOW_SOURCE,
				new Object[] { adapter, false, eventSet, methodDesc }));
	}

	@Override
	public String getDisplayName() {
		return methodDesc.getDisplayName();
	}

	@Override
	public String createEventMethod(IType type, ImportRewrite imports) {
		return null;
	}

	@Override
	public String createAddListenerCode() {
		return code;
	}
}
