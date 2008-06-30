package org.dyno.visual.swing.widgets;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

import javax.swing.JComponent;

import org.dyno.visual.swing.base.LabelEditor;
import org.dyno.visual.swing.plugin.spi.Editor;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;

public abstract class TextWidgetAdapter extends WidgetAdapter {
	private static int VAR_INDEX = 0;

	public TextWidgetAdapter() {
		setName(getVarName() + (VAR_INDEX++));
		this.widget = createWidget();
		this.hotspotPoint = new Point(widget.getWidth() / 2, widget.getHeight() / 2);
		this.widget.putClientProperty(ADAPTER_PROPERTY, this);
	}

	private String getVarName() {
		String className = getWidgetClass().getName();
		int dot = className.lastIndexOf('.');
		if (dot != -1)
			className = className.substring(dot + 1);
		return Character.toLowerCase(className.charAt(0)) + className.substring(1);
	}

	private PropertyDescriptor getTextProperty() {
		try {
			return new PropertyDescriptor("text", getWidgetClass());
		} catch (IntrospectionException e) {
			e.printStackTrace();
			return null;
		}
	}

	private JComponent createWidgetByClass() {
		try {
			return (JComponent) getWidgetClass().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	protected JComponent createWidget() {
		JComponent jc = createWidgetByClass();
		setText(jc, name);
		Dimension size = jc.getPreferredSize();
		jc.setSize(size);
		jc.doLayout();
		jc.validate();
		return jc;
	}

	private String getText(JComponent jc) {
		PropertyDescriptor textProperty = getTextProperty();
		try {
			return (String) textProperty.getReadMethod().invoke(jc);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void setText(JComponent jc, String text) {
		PropertyDescriptor textProperty = getTextProperty();
		try {
			textProperty.getWriteMethod().invoke(jc, text);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract Class<? extends JComponent> getWidgetClass();

	private LabelEditor editor;

	@Override
	public Editor getEditorAt(int x, int y) {
		if (editor == null) {
			editor = new LabelEditor();
		}
		return editor;
	}

	@Override
	public Object getWidgetValue() {
		return getText(getWidget());
	}

	@Override
	public void setWidgetValue(Object value) {
		setText(getWidget(), value == null ? "" : value.toString());
	}

	@Override
	public Rectangle getEditorBounds(int x, int y) {
		int w = getWidget().getWidth();
		int h = getWidget().getHeight();
		JComponent widget = getWidget();
		FontMetrics fm = widget.getFontMetrics(widget.getFont());
		int fh = fm.getHeight() + VER_TEXT_PAD;
		int fw = fm.stringWidth(getText(getWidget())) + HOR_TEXT_PAD;
		int fx = (w - fw) / 2;
		int fy = (h - fh) / 2;
		return new Rectangle(fx, fy, fw, fh);
	}

	@Override
	public int getBaseline() {
		return getBaseline(getWidget().getHeight());
	}

	private Font getButtonFont() {
		Font f = getWidget().getFont();
		if (f == null)
			f = new Font("Dialog", 0, 12);
		return f;
	}

	@Override
	public int getHeightByBaseline(int baseline) {
		FontMetrics fm = getWidget().getFontMetrics(getButtonFont());
		return 2 * (baseline - fm.getAscent()) + fm.getHeight();
	}

	@Override
	public int getBaseline(int h) {
		FontMetrics fm = getWidget().getFontMetrics(getButtonFont());
		return (h - fm.getHeight()) / 2 + fm.getAscent();
	}

	@Override
	public int getHeightByDescent(int descent) {
		FontMetrics fm = getWidget().getFontMetrics(getButtonFont());
		return 2 * descent + fm.getHeight();
	}

	@Override
	public void fillContextAction(MenuManager menu) {
		super.fillContextAction(menu);
		menu.add(new TextEditingAction());
	}

	class TextEditingAction extends Action {
		public TextEditingAction() {
			setText("Edit text ...");
			setId("EditingTextId");
		}

		public void run() {
			editValue();
		}
	}
	private static final int HOR_TEXT_PAD = 20;
	private static final int VER_TEXT_PAD = 4;
}
