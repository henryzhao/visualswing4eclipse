package org.dyno.visual.swing.plugin.spi;

import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.ui.IEditorPart;

public interface IEventListenerModel {
	Iterable<MethodDescriptor> methods();
	String getDisplayName(MethodDescriptor methodDesc);
	boolean hasMethod(MethodDescriptor methodDesc);
	void removeMethod(MethodDescriptor methodDesc);
	boolean isEmpty();
	void addMethod(WidgetAdapter widgetAdapter, EventSetDescriptor eventSet, MethodDescriptor methodDesc);
	void editMethod(IEditorPart editor, MethodDescriptor methodDesc);
	boolean createEventMethod(WidgetAdapter adapter, IType type, ImportRewrite imports, IProgressMonitor monitor);
	String createListenerInstance(EventSetDescriptor eventSet, ImportRewrite imports);
	boolean parse(WidgetAdapter adapter, TypeDeclaration type,	EventSetDescriptor esd);
}
