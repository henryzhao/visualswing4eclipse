package org.dyno.visual.swing.widgets;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;

import javax.swing.JApplet;
import javax.swing.JPanel;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.action.MenuManager;

public class JAppletAdapter extends CompositeAdapter {
	private JPanelAdapter contentAdapter;
	private Container rootPane; 
	public JAppletAdapter() {
		super("jApplet");
		createContentAdapter();
	}
	
	@Override
	public void setWidget(Component widget) {
		super.setWidget(widget);
		createContentAdapter();
	}

	private void createContentAdapter() {
		contentAdapter = (JPanelAdapter) ExtensionRegistry.createWidgetAdapter(JPanel.class);
		JApplet me = (JApplet)getWidget();
		rootPane = me.getContentPane();
		contentAdapter.setWidgetWithoutAttach(rootPane);
	}
	public void doLayout() {
		contentAdapter.doLayout();
	}

	protected void createPostInitCode(StringBuilder builder, ImportRewrite imports) {
		Dimension size = rootPane.getSize();
		String cName = imports.addImport("java.awt.Dimension");			
		builder.append("getContentPane().setPreferredSize(new "+cName+"("+rootPane.getWidth()+", "+rootPane.getHeight()+"));\n");
		builder.append("getContentPane().setSize("+ size.width + ", " + size.height + ");\n");
	}
	
	@Override
	public void addChildByConstraints(Component child, Object constraints) {
		contentAdapter.addChildByConstraints(child, constraints);
	}
	@Override
	public void clearSelection() {
		setSelected(false);
		contentAdapter.clearSelection();
	}	
	public Point convertToLocal(Point p) {
		return contentAdapter.convertToLocal(p);
	}
	
	@Override
	public int getCursorLocation(Point p) {
		return contentAdapter.getCursorLocation(p);
	}
	@Override
	protected Component createWidget() {
		return new JApplet();
	}
	@Override
	public Object getChildConstraints(Component child) {
		return contentAdapter.getChildConstraints(child);
	}

	@Override
	public boolean isRoot() {
		return true;
	}
	@Override
	public Component getRootPane() {
		return rootPane;
	}
	
	@Override
	public void setSelected(boolean b) {
		super.setSelected(b);
		contentAdapter.setSelected(b);
	}

	@Override
	protected Component newWidget() {
		return new JApplet();
	}

	@Override
	public Component cloneWidget() {
		return contentAdapter.cloneWidget();
	}
	public Component getComponent() {
		return contentAdapter.getWidget();
	}
	@Override
	public void addAfter(Component hovering, Component dragged) {
		contentAdapter.addAfter(hovering, dragged);
	}
	
	@Override
	public void addBefore(Component hovering, Component dragged) {
		contentAdapter.addBefore(hovering, dragged);
	}

	@Override
	public void addChild(Component widget) {
		contentAdapter.addChild(widget);
	}

	@Override
	public boolean doAlignment(String id) {
		return contentAdapter.doAlignment(id);
	}
	@Override
	public IUndoableOperation doKeyPressed(KeyEvent e) {
		return contentAdapter.doKeyPressed(e);
	}

	@Override
	public Component getChild(int index) {
		return contentAdapter.getChild(index);
	}

	@Override
	public int getChildCount() {
		return contentAdapter.getChildCount();
	}

	public String toString() {
		if (isRoot()) {
			return "[" + getWidgetName() + "]";
		} else {
			return getName() + " [" + getWidgetName() + "]";
		}
	}

	@Override
	public int getIndexOfChild(Component child) {
		return contentAdapter.getIndexOfChild(child);
	}

	@Override
	public boolean allowChildResize() {
		return contentAdapter.allowChildResize();
	}

	@Override
	public boolean dragOver(Point p) {
		return contentAdapter.dragOver(p);
	}
	public Point convertToGlobal(Point p) {
		return contentAdapter.convertToGlobal(p);
	}
	@Override
	public boolean dragEnter(Point p) {
		return contentAdapter.dragEnter(p);
	}
	public WidgetAdapter getRootAdapter() {
		return contentAdapter;
	}
	@Override
	public boolean dragExit(Point p) {
		return contentAdapter.dragExit(p);
	}

	@Override
	public boolean drop(Point p) {
		return contentAdapter.drop(p);
	}

	@Override
	public void paintFocused(Graphics clipg) {
		contentAdapter.paintFocused(clipg);
	}

	@Override
	public void paintBaselineAnchor(Graphics g) {
		contentAdapter.paintBaselineAnchor(g);
	}

	@Override
	protected String createInitCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		builder.append(super.createInitCode(imports));
		contentAdapter.createAddCode(imports, builder);
		return builder.toString();
	}
	
	public boolean removeChild(Component child) {
		return contentAdapter.removeChild(child);
	}

	@Override
	protected boolean isChildVisible(Component child) {
		return contentAdapter.isChildVisible(child);
	}

	@Override
	public void showChild(Component widget) {
		contentAdapter.showChild(widget);
	}

	@Override
	public void fillContextAction(MenuManager menu) {
		super.fillContextAction(menu);
		contentAdapter.fillSetLayoutAction(menu);
	}

	@Override
	public void fillConstraintsAction(MenuManager menu, Component child) {
		contentAdapter.fillConstraintsAction(menu, child);
	}
	@Override
	public void adjustLayout(Component widget) {
		contentAdapter.adjustLayout(widget);
	}

	@Override
	public boolean isSelectionAlignResize(String id) {
		return contentAdapter.isSelectionAlignResize(id);
	}
	protected boolean createConstructor(IType type, ImportRewrite imports, IProgressMonitor monitor) {
		IMethod cons = type.getMethod(type.getElementName(), new String[0]);
		if(!cons.exists()){
			StringBuilder builder = new StringBuilder();
			builder.append("public "+type.getElementName()+"(){\n");
			builder.append("initComponent();\n");
			builder.append("}\n");
			try {
				type.createMethod(JavaUtil.formatCode(builder.toString()), null, false, null);
			} catch (JavaModelException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}	
}
