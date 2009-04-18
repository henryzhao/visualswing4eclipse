package org.dyno.visual.swing.parser.listener;

import java.awt.Component;

import javax.swing.SwingUtilities;

import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.plugin.spi.IConstants;
import org.dyno.visual.swing.plugin.spi.ISelectionListener;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;

public class SyncEditorListener implements ISelectionListener, IConstants {

	private void sync_widget(IStructuredSelection selection) {
		if (selection.size() == 1) {
			WidgetAdapter adapter = (WidgetAdapter) selection.getFirstElement();
			IEditorPart editor = adapter.getSourceEditor();
			if (editor != null && (adapter.isRoot() || adapter.getID() != null))
				revealInEditor(editor, adapter);
		} else if (!selection.isEmpty()) {
			WidgetAdapter parent = (WidgetAdapter) selection.getFirstElement();
			for (Object object : selection.toArray()) {
				WidgetAdapter adapter = (WidgetAdapter) object;
				parent = getCommonParent(parent, adapter);
			}
			if (parent != null && (parent.isRoot() || parent.getID() != null)) {
				IEditorPart editor = parent.getSourceEditor();
				if (editor != null)
					revealInEditor(editor, parent);
			}
		}
	}

	public void widgetSelected(final IStructuredSelection selection) {
		JavaUtil.getEclipseDisplay().asyncExec(new Runnable() {

			public void run() {
				sync_widget(selection);
			}
		});
	}

	private WidgetAdapter getCommonParent(WidgetAdapter a1, WidgetAdapter a2) {
		if (a1 == null)
			return a2;
		if (a2 == null)
			return a1;
		Component comp1 = a1.getWidget();
		Component comp2 = a2.getWidget();
		if (comp1 == comp2)
			return a1;
		if (SwingUtilities.isDescendingFrom(comp1, comp2)) {
			return a2;
		} else if (SwingUtilities.isDescendingFrom(comp2, comp1)) {
			return a1;
		} else if (a1.isRoot()) {
			return a1;
		} else if (a2.isRoot()) {
			return a2;
		} else {
			return getCommonParent(a1.getParentAdapter(), a2.getParentAdapter());
		}
	}

	private String getGetMethodName(WidgetAdapter adapter, String name) {
		String methodName = (String) adapter.getProperty("getMethodName");
		if (methodName != null)
			return methodName;
		return "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}

	private void revealInEditor(final IEditorPart editor, WidgetAdapter adapter) {
		ICompilationUnit unit = adapter.getCompilationUnit();
		String methodName = adapter.isRoot() ? INIT_METHOD_NAME : getGetMethodName(adapter, adapter.getID());
		String unitname = unit.getElementName();
		int dot = unitname.indexOf('.');
		if (dot != -1)
			unitname = unitname.substring(0, dot);
		IType type = unit.getType(unitname);
		IMethod method = type.getMethod(methodName, new String[0]);
		JavaUI.revealInEditor(editor, (IJavaElement) method);
		editor.setFocus();
	}
}
