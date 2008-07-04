package org.dyno.visual.swing.plugin.spi;

import java.beans.MethodDescriptor;
import java.lang.reflect.Method;

import org.dyno.visual.swing.designer.Event;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class EventDelegation implements IEventMethod {
	private WidgetAdapter adapter;
	private MethodDescriptor methodDesc;
	private String methodName;

	public EventDelegation(WidgetAdapter adapter, MethodDescriptor methodDesc,
			String methodName) {
		this.adapter = adapter;
		this.methodDesc = methodDesc;
		this.methodName = methodName;
	}

	@Override
	public String getDisplayName() {
		return methodName;
	}

	@Override
	public void editCode() {
		Class<?>[] pd = methodDesc.getMethod().getParameterTypes();
		if (pd.length > 0) {
			String pname = pd[0].getName();
			int dot = pname.lastIndexOf('.');
			if (dot != -1)
				pname = pname.substring(dot + 1);
			String typeSig = Signature.createTypeSignature(pname, false);
			WhiteBoard.sendEvent(new Event(this, Event.EVENT_SHOW_SOURCE,
					new Object[] { adapter, true, methodName, typeSig }));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String createEventMethod(IType type, ImportRewrite imports) {
		Method mEvent = methodDesc.getMethod();
		Class[] pTypes = mEvent.getParameterTypes();
		String pcName = pTypes[0].getName();
		pcName = imports.addImport(pcName);
		String[] paras = new String[] { Signature
				.createTypeSignature(pcName, false) };
		IMethod eventMethod = type.getMethod(methodName, paras);
		if (!eventMethod.exists()) {
			StringBuilder builder = new StringBuilder(0);
			builder.append("private void ");
			builder.append(methodName + "(");
			builder.append(pcName);
			builder.append(" event");
			builder.append("){\n");
			builder.append("}\n");
			return builder.toString();
		}
		return null;
	}

	@Override
	public String createAddListenerCode() {
		return methodName + "(event);\n";
	}
}
