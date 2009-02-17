package org.dyno.visual.swing.editors;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.WhiteBoard;
import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.designer.VisualDesigner;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.SharedScrolledComposite;
import org.eclipse.ui.part.Page;
import org.osgi.framework.Bundle;

public class PalettePage extends Page implements IPalettePage, SelectionListener  {
	private static final String ADAPTER_EXTENSION_POINT = "org.dyno.visual.swing.widgetAdapter";
	private static final String GROUP_EXTENSION_POINT = "org.dyno.visual.swing.widgetGroup";
	private HashMap<String, ToolBar> toolbars;
	private HashMap<String, ExpandableComposite> expandItems;
	private SharedScrolledComposite expandBar;
	private VisualDesigner designer;
	private VisualSwingEditor editor;
	public PalettePage(VisualDesigner designer){
		assert designer!=null;
		this.designer = designer;
	}
	public PalettePage(VisualSwingEditor editor){
		assert editor!=null;
		this.editor = editor;
	}
	@Override
	public void createControl(Composite parent) {
		expandBar = new SharedScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL) {
		};
		expandBar.setExpandHorizontal(true);
		expandBar.setExpandVertical(true);
		Composite container = new Composite(expandBar, SWT.NONE);
		expandBar.setContent(container);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		toolbars = new HashMap<String, ToolBar>();
		expandItems = new HashMap<String, ExpandableComposite>();
		parseGroupExtensions(container);
		parseWidgetExtensions();

		ExpandableComposite expandItem = new ExpandableComposite(container, SWT.NONE);
		expandItem.setExpanded(false);
		expandItem.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DIALOG_FONT));
		expandItem.setText("Custom Component");
		expandItem.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				updateScrolledComposite();
			}
		});
		ToolBar toolBar = new ToolBar(expandItem, SWT.VERTICAL | SWT.RIGHT | SWT.FLAT);
		expandItem.setClient(toolBar);
		ToolItem toolItem = new ToolItem(toolBar, SWT.CHECK);
		toolItem.setText("Custom Swing Component");
		toolItem.setToolTipText("Choose custom component");
		String iconUrl = "icons/beans.png";
		Image img = VisualSwingPlugin.getSharedImage(VisualSwingPlugin.PLUGIN_ID, iconUrl);
		toolItem.setImage(img);
		toolItem.addSelectionListener(this);
		new ToolItem(toolBar, SWT.SEPARATOR);

		updateScrolledComposite();
	}

	@Override
	public Control getControl() {
		return expandBar;
	}

	@Override
	public void setFocus() {
		expandBar.setFocus();
	}

	private void parseGroupExtensions(Composite bar) {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry()
				.getExtensionPoint(GROUP_EXTENSION_POINT);
		if (extensionPoint != null) {
			IExtension[] extensions = extensionPoint.getExtensions();
			if (extensions != null && extensions.length > 0) {
				for (int i = 0; i < extensions.length; i++) {
					parseGroupExtension(extensions[i], bar);
				}
			}
		}
	}

	private void parseGroupExtension(IExtension extension, Composite bar) {
		IContributor contributor = extension.getContributor();
		String pluginId = contributor.getName();
		Bundle bundle = Platform.getBundle(pluginId);
		IConfigurationElement[] configs = extension.getConfigurationElements();
		if (configs != null && configs.length > 0) {
			for (int i = 0; i < configs.length; i++) {
				String name = configs[i].getName();
				if (name.equals("group")) {
					addGroup(configs[i], bundle, bar);
				}
			}
		}
	}

	private void parseWidgetExtensions() {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry()
				.getExtensionPoint(ADAPTER_EXTENSION_POINT);
		if (extensionPoint != null) {
			IExtension[] extensions = extensionPoint.getExtensions();
			if (extensions != null && extensions.length > 0) {
				for (int i = 0; i < extensions.length; i++) {
					parseWidgetExtension(extensions[i]);
				}
			}
		}
	}

	private void parseWidgetExtension(IExtension extension) {
		IContributor contributor = extension.getContributor();
		String pluginId = contributor.getName();
		IConfigurationElement[] configs = extension.getConfigurationElements();
		if (configs != null && configs.length > 0) {
			for (int i = 0; i < configs.length; i++) {
				String name = configs[i].getName();
				if (name.equals("widget")) {
					addWidget(configs[i], pluginId);
				}
			}
		}
	}

	private void addGroup(IConfigurationElement config, Bundle bundle,
			Composite bar) {
		String id = config.getAttribute("id");
		String displayName = config.getAttribute("displayName");
		if (displayName == null || displayName.equals("")) {
			displayName = id;
		}
		ToolBar toolbar = toolbars.get(id);
		if (toolbar == null) {
			ExpandableComposite expandItem = new ExpandableComposite(bar,
					SWT.NONE);
			String sExpanded = config.getAttribute("expanded");
			if (sExpanded == null || sExpanded.trim().length() == 0)
				sExpanded = "true";
			expandItem.setExpanded(sExpanded.equals("true"));
			expandItem.setFont(JFaceResources.getFontRegistry().getBold(
					JFaceResources.DIALOG_FONT));
			expandItem.setText(displayName);
			expandItem.addExpansionListener(new ExpansionAdapter() {
				@Override
				public void expansionStateChanged(ExpansionEvent e) {
					updateScrolledComposite();
				}
			});
			toolbar = new ToolBar(expandItem, SWT.VERTICAL | SWT.RIGHT
					| SWT.FLAT);
			expandItem.setClient(toolbar);
			toolbars.put(id, toolbar);
			expandItems.put(id, expandItem);
		}
	}

	private void updateScrolledComposite() {
		Composite container = (Composite) expandBar.getContent();
		container.layout();
		expandBar.layout();
		expandBar.reflow(true);
	}

	private void addWidget(IConfigurationElement config, String pluginId) {
		String abs = config.getAttribute("abstract");
		if (abs != null && abs.equals("true"))
			return;
		String sRootPaneContainer = config.getAttribute("rootPaneContainer");
		if (sRootPaneContainer != null
				&& sRootPaneContainer.trim().length() > 0
				&& sRootPaneContainer.equals("true"))
			return;
		String sShown = config.getAttribute("shown");
		if (sShown != null && sShown.trim().equals("false"))
			return;
		String groupId = config.getAttribute("groupId");
		if (groupId != null && groupId.trim().length() > 0) {
			ToolBar toolbar = toolbars.get(groupId);
			if (toolbar != null) {
				String widgetClassname = config.getAttribute("widgetClass");
				String widgetName = config.getAttribute("widgetName");
				if (widgetName == null || widgetName.trim().length() == 0)
					widgetName = widgetClassname;
				ToolItem toolItem = new ToolItem(toolbar, SWT.CHECK);
				toolItem.setText(" " + widgetName);
				toolItem.setToolTipText(widgetClassname);
				String iconUrl = config.getAttribute("icon");
				Image img = VisualSwingPlugin.getSharedImage(pluginId, iconUrl);
				toolItem.setImage(img);
				toolItem.addSelectionListener(this);
				toolItem.setData(widgetClassname);
			}
		}
	}

	class ClearRunnable implements Runnable {
		@Override
		public void run() {
			if (selectedItem != null && selectedItem.getSelection()) {
				selectedItem.setSelection(false);
				selectedItem = null;
			}
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		ToolItem item = (ToolItem) e.getSource();
		setSelectedItem(item);
	}
	
	private void setSelectedItem(ToolItem item) {
		if(designer==null&&editor!=null){
			designer = editor.getDesigner();
		}		
		if (item.getSelection()) {
			String beanClassname = (String) item.getData();
			if (beanClassname != null) {
				WidgetAdapter adapter = ExtensionRegistry.createWidgetAdapter(beanClassname);
				List<WidgetAdapter> list = new ArrayList<WidgetAdapter>();
				list.add(adapter);
				if (designer != null)
					designer.setSelectedWidget(list);
			}else{
				Shell parent = expandBar.getShell();
				try {
					SelectionDialog typeDialog = JavaUI.createTypeDialog(parent, new ProgressMonitorDialog(parent), WhiteBoard.getCurrentProject().getProject(), IJavaElementSearchConstants.CONSIDER_CLASSES, false);
					if(typeDialog.open()==Window.OK){
						Object[] result = typeDialog.getResult();
						if(result!=null&&result.length>0){
							IType type = (IType) result[0];
							String className = type.getElementName();
							IPackageFragment packageFragment = type.getPackageFragment();
							String pkg=packageFragment.getElementName();
							if(pkg!=null&&pkg.trim().length()>0)
								className=pkg+"."+className;
							final Class<?> beanClass = JavaUtil.getProjectClassLoader(WhiteBoard.getCurrentProject()).loadClass(className);
							if(!JComponent.class.isAssignableFrom(beanClass)){
								MessageDialog.openError(parent, "Error", "Chosen class must be a javax.swing.JComponent derived class!");
								item.setSelection(false);
								return;
							}
							Component bean = (Component)beanClass.newInstance();
							Dimension dim = bean.getSize();
							if(dim.width<10||dim.height<4)
								dim = bean.getPreferredSize();
							if(dim.width<10||dim.height<4)
								dim = new Dimension(100,22);
							bean.setSize(dim);
							if(bean instanceof Container){
								layoutContainer((Container)bean);
							}
							WidgetAdapter beanAdapter = ExtensionRegistry.createWidgetAdapter(bean);
							List<WidgetAdapter> list = new ArrayList<WidgetAdapter>();
							list.add(beanAdapter);
							if (designer != null)
								designer.setSelectedWidget(list);
						}
					}
				} catch (Exception e) {
					VisualSwingPlugin.getLogger().error(e);
				}
			}
		} else {
			if (designer != null)
				designer.setSelectedWidget(null);
		}
		if (selectedItem != null && selectedItem != item
				&& selectedItem.getSelection()) {
			selectedItem.setSelection(false);
		}
		selectedItem = item.getSelection()?item:null;
	}

	private void layoutContainer(Container container) {
		container.doLayout();
		int count = container.getComponentCount();
		for(int i=0;i<count;i++){
			Component child = container.getComponent(i);
			if(child instanceof Container)
				layoutContainer((Container)child);
		}
	}

	private ToolItem selectedItem;

	public void clearToolSelection() {
		expandBar.getDisplay().asyncExec(new ClearRunnable());
	}
}
