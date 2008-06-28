package org.dyno.visual.swing.types.editor;

import java.text.MessageFormat;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;

public class FontCellEditor extends DialogCellEditor {
	private static final int GAP = 6;
	private Composite composite;
	private Text fontText;
	private ModifyListener modifyListener;

	private class FontCellLayout extends Layout {
		public Point computeSize(Composite editor, int wHint, int hHint, boolean force) {
			if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
				return new Point(wHint, hHint);
			}
			Point rgbSize = fontText.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			return new Point(GAP + rgbSize.x, rgbSize.y);
		}

		public void layout(Composite editor, boolean force) {
			Rectangle bounds = editor.getClientArea();
			Point rgbSize = fontText.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			int ty = (bounds.height - rgbSize.y) / 2;
			if (ty < 0) {
				ty = 0;
			}
			fontText.setBounds(GAP - 1, ty, bounds.width - GAP, bounds.height);
		}
	}

	public FontCellEditor(Composite parent) {
		this(parent, SWT.NONE);
	}

	public FontCellEditor(Composite parent, int style) {
		super(parent, style);
		setValidator(new FontCellEditorValidator());
	}

	@Override
	protected void doSetFocus() {
		fontText.setFocus();
		fontText.selectAll();
	}

	protected Control createContents(Composite cell) {
		Color bg = cell.getBackground();
		composite = new Composite(cell, getStyle());
		composite.setBackground(bg);
		composite.setLayout(new FontCellLayout());
		fontText = new Text(composite, SWT.SINGLE);
		fontText.setBackground(bg);
		fontText.setFont(cell.getFont());
		fontText.addModifyListener(getModifyListener());
		fontText.addSelectionListener(new SelectionAdapter() {
            @Override
			public void widgetDefaultSelected(SelectionEvent e) {
            	fontTextChanged();
			}
        });		
		return composite;
	}
	private void fontTextChanged(){
		String text = fontText.getText();
            boolean newValidState = isCorrect(text);
            if (newValidState) {
                markDirty();  
                doSetValue(decodeFontData(text));
            } else {
                setErrorMessage(MessageFormat.format(getErrorMessage(),
                        new Object[] {text}));
            }
            fireApplyEditorValue();
	}
    private ModifyListener getModifyListener() {
        if (modifyListener == null) {
            modifyListener = new ModifyListener() {
                public void modifyText(ModifyEvent e) {
                    editOccured(e);
                }
            };
        }
        return modifyListener;
    }
    
    protected void editOccured(ModifyEvent e) {
        String value = fontText.getText();
        if (value == null) {
			value = "";//$NON-NLS-1$
		}
        Object typedValue = value;
        boolean oldValidState = isValueValid();
        boolean newValidState = isCorrect(typedValue);
        if (typedValue == null && newValidState) {
			Assert.isTrue(false,
                    "Validator isn't limiting the cell editor's type range");//$NON-NLS-1$
		}
        if (!newValidState) {
            // try to insert the current value into the error message.
            setErrorMessage(MessageFormat.format(getErrorMessage(),
                    new Object[] { value }));
        }
        valueChanged(oldValidState, newValidState);
    }    
	protected Object openDialogBox(Control cellEditorWindow) {
		FontDialog dialog = new FontDialog(cellEditorWindow.getShell());
		Object value = getValue();
		if (value != null) {
			dialog.setFontList(new FontData[] { (FontData) value });
		}
		value = dialog.open();
		FontData[] list = dialog.getFontList();
		return list == null ? null : list.length > 0 ? list[0] : null;
	}

	protected Button createButton(Composite parent) {
		Button result = super.createButton(parent);
		result.setText(".");
		return result;
	}

	protected void updateContents(Object value) {
		FontData font = (FontData) value;
		String text;
		if (font == null) {
			text = "null";
		} else {
			text = encodeFontData(font);
		}
		fontText.setText(text);
	}

	private static String encodeFontData(FontData font) {
		int style = font.getStyle();
		String styleString;
		if ((style & SWT.BOLD) != 0) {
			if ((style & SWT.ITALIC) != 0) {
				styleString = "BOLDITALIC";
			} else {
				styleString = "BOLD";
			}
		} else {
			if ((style & SWT.ITALIC) != 0) {
				styleString = "ITALIC";
			} else {
				styleString = "REGULAR";
			}
		}
		return "(" + font.getName() + ", " + styleString + ", " + font.getHeight() + ")";
	}
	private static FontData decodeFontData(String string) {
		string = string.trim();
		string = string.substring(1, string.length() - 1);
		StringTokenizer tokenizer = new StringTokenizer(string, ",");
		String name = tokenizer.nextToken();
		String style = tokenizer.nextToken().trim();
		int stl=0;
		if(style.equals("REGULAR")){
			stl = 0;
		}else if(style.equals("BOLD")){
			stl=SWT.BOLD;
		}else if(style.equals("ITALIC")){
			stl=SWT.ITALIC;
		}else if(style.equals("BOLDITALIC")){
			stl=SWT.BOLD|SWT.ITALIC;
		}
		String size = tokenizer.nextToken().trim();
		int s=11;
		try {
			s=Integer.parseInt(size);
		} catch (NumberFormatException e) {
		}
		return new FontData(name, s, stl);
	}	
}
