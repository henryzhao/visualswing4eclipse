package org.dyno.visual.swing.widgets.menucontext;

import org.dyno.visual.swing.base.ImageSelectionDialog;
import org.dyno.visual.swing.widgets.WidgetPlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class TabPropertiesDialog extends Dialog {

	private Text txtTitle;
	private Text txtTooltip;
	private Label lblPreview;

	private IFile imgFile;
	private String title;
	private String tooltip;

	public TabPropertiesDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public boolean close() {
		if (this.lblImage != null)
			this.lblImage.dispose();
		return super.close();
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public void setIcon(IFile imgFile) {
		this.imgFile = imgFile;
	}

	public String getTitle() {
		return title;
	}

	public String getTooltip() {
		return tooltip;
	}

	public IFile getIcon() {
		return imgFile;
	}

	@Override
	protected void okPressed() {
		title = txtTitle.getText();
		if(title.trim().length()==0)
			title=null;
		tooltip = txtTooltip.getText();
		if(tooltip.trim().length()==0)
			tooltip=null;
		super.okPressed();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText("Customize Tab Properties");
		Composite area = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		area.setLayout(layout);
		Label label = new Label(area, SWT.NONE);
		label.setAlignment(SWT.RIGHT);
		label.setText("Tab Title:");
		GridData data = new GridData();
		txtTitle = new Text(area, SWT.SINGLE | SWT.BORDER);
		if (title != null)
			txtTitle.setText(title);
		data.widthHint = 200;
		data.horizontalSpan = 2;
		txtTitle.setLayoutData(data);
		label = new Label(area, SWT.NONE);
		label.setAlignment(SWT.RIGHT);
		label.setText("Tab Tooltips:");
		txtTooltip = new Text(area, SWT.SINGLE | SWT.BORDER);
		if (tooltip != null)
			txtTooltip.setText(tooltip);
		data = new GridData();
		data.widthHint = 200;
		data.horizontalSpan = 2;
		txtTooltip.setLayoutData(data);
		label = new Label(area, SWT.NONE);
		label.setText("Tab Icon:");
		data = new GridData();
		data.verticalAlignment = SWT.TOP;
		label.setLayoutData(data);
		lblPreview = new Label(area, SWT.NONE | SWT.BORDER|SWT.CENTER);
		setImageFile(imgFile);
		data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		data.heightHint = 80;
		data.verticalAlignment = SWT.TOP;
		lblPreview.setLayoutData(data);
		Button button = new Button(area, SWT.PUSH);
		button.setText("Change ...");
		button.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				selectIcon();
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				selectIcon();
			}
		});
		data = new GridData();
		data.horizontalAlignment = SWT.RIGHT;
		data.verticalAlignment = SWT.TOP;
		button.setLayoutData(data);
		return parent;
	}

	private void setImageFile(IFile imgFile) {
		if (imgFile == null)
			lblPreview.setText("[No Icon]");
		else {
			lblPreview.setText("");
			if (lblImage != null)
				lblImage.dispose();
			try {
				ImageDescriptor d = ImageDescriptor.createFromURL(imgFile
						.getLocationURI().toURL());
				lblImage = d.createImage();
				lblPreview.setImage(lblImage);
			} catch (Exception e) {
				WidgetPlugin.getLogger().error(e);
			}
		}
	}

	private Image lblImage;

	private void selectIcon() {
		ImageSelectionDialog isd = new ImageSelectionDialog(getShell());
		if (imgFile != null)
			isd.setImageFile(imgFile);
		int ret = isd.open();
		if (ret == Window.OK) {
			IFile file = isd.getImageFile();
			setIcon(file);
			setImageFile(file);
		}
	}
}
