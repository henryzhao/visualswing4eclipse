package org.dyno.visual.swing.plugin.spi;

import org.dyno.visual.swing.designer.Event;
import org.dyno.visual.swing.designer.Listener;
import org.eclipse.jdt.core.IJavaProject;

public class WhiteBoard {
	private static IJavaProject project;
	private static Listener editorListener;
	private static WidgetAdapter selectedWidget;

	public static void setSelectedWidget(WidgetAdapter adapter) {
		selectedWidget = adapter;
	}

	public static WidgetAdapter getSelectedWidget() {
		return selectedWidget;
	}

	public static void setCurrentProject(IJavaProject proj) {
		project = proj;
	}

	public static IJavaProject getCurrentProject() {
		return project;
	}

	public static void setEditorListener(Listener l) {
		editorListener = l;
	}

	public static void sendEvent(Event e) {
		if (editorListener != null)
			editorListener.onEvent(e);
	}
}
