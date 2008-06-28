package org.dyno.visual.swing.widgets;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class JSplitPaneAdapter extends CompositeAdapter {
	private static int VAR_INDEX = 0;
	protected static Color RED_COLOR = new Color(255, 164, 0);
	protected static Color GREEN_COLOR = new Color(164, 255, 0);
	protected static Stroke STROKE;
	static {
		STROKE = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 2 }, 0);
	}

	public JSplitPaneAdapter() {
		super("jSplitPane" + (VAR_INDEX++));
	}

	@Override
	protected JComponent createWidget() {
		JSplitPane jsp = new JSplitPane();
		Dimension size = new Dimension(100, 100);
		jsp.setSize(size);
		jsp.doLayout();
		jsp.validate();
		return jsp;
	}

	private String position;

	private Rectangle getLeftBounds() {
		JSplitPane jsp = (JSplitPane) getWidget();
		int height = jsp.getHeight();
		int divideLocation = jsp.getDividerLocation();
		Insets insets = jsp.getInsets();
		int x = 0, y = 0, w = 0, h = 0;
		x = insets.left;
		y = insets.top;
		w = divideLocation - insets.left;
		h = height - insets.top - insets.bottom;
		return new Rectangle(x, y, w, h);
	}

	private Rectangle getRightBounds() {
		JSplitPane jsp = (JSplitPane) getWidget();
		int width = jsp.getWidth();
		int height = jsp.getHeight();
		int divideLocation = jsp.getDividerLocation();
		int dividerSize = jsp.getDividerSize();
		Insets insets = jsp.getInsets();
		int x = 0, y = 0, w = 0, h = 0;
		x = divideLocation + dividerSize;
		y = insets.top;
		w = width - divideLocation - dividerSize - insets.right;
		h = height - insets.top - insets.bottom;
		return new Rectangle(x, y, w, h);
	}

	private Rectangle getTopBounds() {
		JSplitPane jsp = (JSplitPane) getWidget();
		int width = jsp.getWidth();
		int divideLocation = jsp.getDividerLocation();
		Insets insets = jsp.getInsets();
		int x = 0, y = 0, w = 0, h = 0;
		x = insets.left;
		y = insets.top;
		h = divideLocation - insets.top;
		w = width - insets.left - insets.right;
		return new Rectangle(x, y, w, h);
	}

	private Rectangle getBottomBounds() {
		JSplitPane jsp = (JSplitPane) getWidget();
		int width = jsp.getWidth();
		int height = jsp.getHeight();
		int divideLocation = jsp.getDividerLocation();
		int dividerSize = jsp.getDividerSize();
		Insets insets = jsp.getInsets();
		int x = 0, y = 0, w = 0, h = 0;
		x = insets.left + divideLocation + dividerSize;
		y = insets.top;
		h = height - divideLocation - dividerSize - insets.bottom;
		w = width - insets.left - insets.right;
		return new Rectangle(x, y, w, h);
	}

	private Rectangle getHotspotBounds() {
		if (position == null)
			return null;
		if (position.equals("left"))
			return getLeftBounds();
		if (position.equals("top"))
			return getTopBounds();
		if (position.equals("right"))
			return getRightBounds();
		if (position.equals("bottom"))
			return getBottomBounds();
		return null;
	}

	@Override
	public void paintFocused(Graphics clipg) {
		Rectangle bounds = getHotspotBounds();
		if (bounds != null) {
			Graphics2D g2d = (Graphics2D) clipg;
			g2d.setColor(forbid ? RED_COLOR : GREEN_COLOR);
			Composite oldComposite = g2d.getComposite();
			AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
			g2d.setComposite(composite);
			g2d.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
			Stroke oldStroke = g2d.getStroke();
			g2d.setColor(forbid ? GREEN_COLOR : RED_COLOR);
			g2d.setStroke(STROKE);
			g2d.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
			g2d.setStroke(oldStroke);
			g2d.setComposite(oldComposite);
		}
	}

	@Override
	public boolean dragEnter(Point p) {
		updatePosition(p);
		return true;
	}

	@Override
	public boolean dragExit(Point p) {
		updatePosition(p);
		return true;
	}

	@Override
	public boolean dragOver(Point p) {
		updatePosition(p);
		setMascotLocation(p);
		return true;
	}

	private boolean existsAndDesigning(JComponent comp) {
		return comp != null && WidgetAdapter.getWidgetAdapter(comp) != null;
	}

	private boolean forbid;

	private void updatePosition(Point p) {
		JSplitPane jsp = (JSplitPane) getWidget();
		int orientation = jsp.getOrientation();
		if (orientation == JSplitPane.HORIZONTAL_SPLIT) {
			if (getLeftBounds().contains(p)) {
				JComponent left = (JComponent) jsp.getLeftComponent();
				position = "left";
				forbid = existsAndDesigning(left);
			} else if (getRightBounds().contains(p)) {
				JComponent right = (JComponent) jsp.getRightComponent();
				position = "right";
				forbid = existsAndDesigning(right);
			} else {
				position = null;
				forbid = false;
			}
		} else {
			if (getTopBounds().contains(p)) {
				JComponent top = (JComponent) jsp.getTopComponent();
				position = "top";
				forbid = existsAndDesigning(top);
			} else if (getBottomBounds().contains(p)) {
				JComponent bottom = (JComponent) jsp.getBottomComponent();
				position = "bottom";
				forbid = existsAndDesigning(bottom);
			} else {
				position = null;
				forbid = false;
			}
		}
	}

	@Override
	public boolean drop(Point p) {
		if (position != null) {
			if (forbid) {
				Toolkit.getDefaultToolkit().beep();
				forbid = false;
			} else {
				WidgetAdapter adapter = getDropWidget();
				JComponent child = adapter.getComponent();
				JSplitPane jtp = (JSplitPane) getWidget();
				if (position.equals("left"))
					jtp.setLeftComponent(child);
				else if (position.equals("right"))
					jtp.setRightComponent(child);
				else if (position.equals("top"))
					jtp.setTopComponent(child);
				else if (position.equals("bottom"))
					jtp.setBottomComponent(child);
				clearAllSelected();
				adapter.setSelected(true);
			}
			position = null;
		}
		return true;
	}

	@Override
	public JComponent getChild(int index) {
		JSplitPane jtp = (JSplitPane) getWidget();
		JComponent comp = null;
		JComponent left, right;
		if (jtp.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
			left = (JComponent) jtp.getLeftComponent();
			right = (JComponent) jtp.getRightComponent();
		} else {
			left = (JComponent) jtp.getTopComponent();
			right = (JComponent) jtp.getBottomComponent();
		}
		if (existsAndDesigning(left)) {
			if (existsAndDesigning(right)) {
				if (index == 0)
					comp = left;
				else if (index == 1)
					comp = right;
			} else {
				if (index == 0)
					comp = left;
			}
		} else {
			if (existsAndDesigning(right)) {
				if (index == 0)
					comp = right;
			}
		}
		return (JComponent) comp;
	}

	@Override
	public int getChildCount() {
		JSplitPane jtp = (JSplitPane) getWidget();
		JComponent left, right;
		if (jtp.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
			left = (JComponent) jtp.getLeftComponent();
			right = (JComponent) jtp.getRightComponent();
		} else {
			left = (JComponent) jtp.getTopComponent();
			right = (JComponent) jtp.getBottomComponent();
		}
		return (existsAndDesigning(left) ? 1 : 0) + (existsAndDesigning(right) ? 1 : 0);
	}

	@Override
	public int getIndexOfChild(JComponent child) {
		JSplitPane jtp = (JSplitPane) getWidget();
		JComponent left, right;
		if (jtp.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
			left = (JComponent) jtp.getLeftComponent();
			right = (JComponent) jtp.getRightComponent();
		} else {
			left = (JComponent) jtp.getTopComponent();
			right = (JComponent) jtp.getBottomComponent();
		}
		if (existsAndDesigning(left)) {
			if (existsAndDesigning(right)) {
				if (child == left)
					return 0;
				if (child == right)
					return 1;
				return -1;
			} else {
				if (child == left)
					return 0;
				return -1;
			}
		} else {
			if (existsAndDesigning(right)) {
				if (child == right)
					return 0;
				return -1;
			} else
				return -1;
		}
	}

	@Override
	public JComponent cloneWidget() {
		JSplitPane jsp = (JSplitPane) getWidget();
		JSplitPane copy = (JSplitPane) super.cloneWidget();
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			JComponent child = getChild(i);
			WidgetAdapter cAdapter = WidgetAdapter.getWidgetAdapter(child);
			JComponent cloneChild = cAdapter.cloneWidget();
			int orientation = jsp.getOrientation();
			if (orientation == JSplitPane.HORIZONTAL_SPLIT) {
				if (child == jsp.getLeftComponent()) {
					copy.setLeftComponent(cloneChild);
				} else if (child == jsp.getRightComponent()) {
					copy.setRightComponent(cloneChild);
				}
			} else {
				if (child == jsp.getTopComponent()) {
					copy.setTopComponent(cloneChild);
				} else if (child == jsp.getBottomComponent()) {
					copy.setBottomComponent(cloneChild);
				}
			}
		}
		return copy;
	}

	@Override
	protected String createGetCode(ImportRewrite imports) {
		StringBuilder builder = new StringBuilder();
		builder.append(super.createGetCode(imports));
		JSplitPane jsp = (JSplitPane) getWidget();
		int oritentation = jsp.getOrientation();
		String fieldName = getFieldName(getName());
		if (oritentation == JSplitPane.HORIZONTAL_SPLIT) {
			Component left = jsp.getLeftComponent();
			if (left != null) {
				JComponent leftComponent = (JComponent) left;
				WidgetAdapter leftAdapter = WidgetAdapter.getWidgetAdapter(leftComponent);
				if (leftAdapter != null) {
					String leftGetName = getGetMethodName(leftAdapter.getName());
					builder.append(fieldName + ".setLeftComponent(" + leftGetName + "());\n");
				}
			}
			Component right = jsp.getRightComponent();
			if (right != null) {
				JComponent rightComponent = (JComponent) right;
				WidgetAdapter rightAdapter = WidgetAdapter.getWidgetAdapter(rightComponent);
				if (rightAdapter != null) {
					String rightGetName = getGetMethodName(rightAdapter.getName());
					builder.append(fieldName + ".setRightComponent(" + rightGetName + "());\n");
				}
			}
		} else {
			Component top = jsp.getTopComponent();
			if (top != null) {
				JComponent topComponent = (JComponent) top;
				WidgetAdapter topAdapter = WidgetAdapter.getWidgetAdapter(topComponent);
				if (topAdapter != null) {
					String topGetName = getGetMethodName(topAdapter.getName());
					builder.append(fieldName + ".setTopComponent(" + topGetName + "());\n");
				}
			}
			Component bottom = jsp.getBottomComponent();
			if (bottom != null) {
				JComponent bottomComponent = (JComponent) bottom;
				WidgetAdapter bottomAdapter = WidgetAdapter.getWidgetAdapter(bottomComponent);
				if (bottomAdapter != null) {
					String bottomGetName = getGetMethodName(bottomAdapter.getName());
					builder.append(fieldName + ".setBottomComponent(" + bottomGetName + "());\n");
				}
			}

		}
		return builder.toString();
	}
	public boolean isEnclosingContainer() {
		return true;
	}
	@Override
	public boolean interceptPoint(Point p, int ad) {
		JSplitPane comp = (JSplitPane) getWidget();
		if(p.x >= -ad && p.y >= -ad && p.x < comp.getWidth() + ad && p.y < comp.getHeight() + ad
				&& !(p.x >= ad && p.y >= ad && p.x < comp.getWidth() - ad && p.y < comp.getHeight() - ad))
			return true;
		int location = comp.getDividerLocation();
		int div = comp.getDividerSize();
		if(comp.getOrientation()==JSplitPane.HORIZONTAL_SPLIT){
			if(p.x>=location&&p.x<=location+div)
				return true;
			else
				return false;
		}else{
			if(p.y>=location&&p.y<=location+div)
				return true;
			else
				return false;
		}
	}	
}
