/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.editors;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.Timer;
import javax.swing.UIManager;

import org.dyno.visual.swing.WhiteBoard;
import org.dyno.visual.swing.base.EditorAction;
import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.designer.Event;
import org.dyno.visual.swing.designer.Listener;
import org.dyno.visual.swing.designer.VisualDesigner;
import org.dyno.visual.swing.plugin.spi.ILookAndFeelAdapter;
import org.dyno.visual.swing.plugin.spi.ISourceParser;
import org.dyno.visual.swing.plugin.spi.ParserFactory;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.dyno.visual.swing.swt_awt.EmbeddedSwingComposite;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;

/**
 * 
 * VisualSwingEditor
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public class VisualSwingEditor extends AbstractDesignerEditor implements Listener, IResourceChangeListener, ActionListener, ISelectionProvider, IPartListener {
	private List<ISelectionChangedListener> listeners;
	private EmbeddedSwingComposite embedded;
	private VisualDesigner designer;
	private VisualSwingOutline outline;
	private HashMap<String, EditorAction> actions;
	private ArrayList<EditorAction> actionList;
	private IJavaProject hostProject;
	private Timer timer;
	private boolean isGeneratingCode;
	private ISelection selection;
	private PropertySheetPage sheetPage;
	private ScrolledComposite scrollPane;

	public VisualSwingEditor() {
		super();
		actions = new HashMap<String, EditorAction>();
		actionList = new ArrayList<EditorAction>();
		listeners = new ArrayList<ISelectionChangedListener>();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		site.setSelectionProvider(this);
		site.getWorkbenchWindow().getPartService().addPartListener(this);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		if (adapter == IContentOutlinePage.class) {
			if (outline == null && designer != null) {
				outline = new VisualSwingOutline(designer);
				addSelectionChangedListener(outline);
			}
			return outline;
		} else if (adapter == IPropertySheetPage.class) {
			if (sheetPage == null) {
				sheetPage = new WidgetPropertyPage();
				sheetPage.setPropertySourceProvider(new WidgetAdapterContentProvider());
			}
			return sheetPage;
		} else
			return super.getAdapter(adapter);
	}

	@Override
	public boolean isDirty() {
		return designer != null && (designer.isLnfChanged() || designer.isDirty());
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public boolean isSaveOnCloseNeeded() {
		return true;
	}

	public ArrayList<EditorAction> getActions() {
		return actionList;
	}

	public VisualDesigner getDesigner() {
		return designer;
	}

	public void refreshActionState() {
		if (designer != null) {
			for (IAction action : actions.values())
				designer.setActionState(action);
		}
	}

	public void dispose() {
		super.dispose();
		closeRelatedView();
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
	}

	public void addAction(EditorAction action) {
		if (action == null) {
			actionList.add(null);
		} else if (actions.get(action.getId()) == null) {
			actions.put(action.getId(), action);
			actionList.add(action);
		}
	}

	public void setFocus() {
		if (embedded != null) {
			embedded.setFocus();
		}
		if (designer != null)
			designer.setFocus();
		WhiteBoard.setCurrentProject(hostProject);
		WhiteBoard.setEditorListener(this);
	}

	public void changeToLnf(String className) {
		LookAndFeel lnf = UIManager.getLookAndFeel();
		if (lnf == null) {
			if (className != null) {
				setLnfClassname(className);
			}
		} else {
			String lnfName = lnf.getClass().getName();
			if (className == null) {
				className = UIManager.getCrossPlatformLookAndFeelClassName();
				if (!lnfName.equals(className))
					setLnfClassname(className);
			} else if (!lnfName.equals(className)) {
				setLnfClassname(className);
			}
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		if (!isSwingComponent()) {
			switchToJavaEditor(getDisplay());
		} else {
			parent.setLayout(new FillLayout());
			scrollPane = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
			scrollPane.setExpandHorizontal(true);
			scrollPane.setExpandVertical(true);
			Composite designerContainer = new Composite(scrollPane, SWT.NONE);
			scrollPane.setContent(designerContainer);
			designerContainer.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
			designerContainer.setLayout(new FillLayout());
			embedded = new EmbeddedVisualDesigner(designerContainer);
			embedded.populate();
			if (!createDesignerUI()) {
				switchToJavaEditor(getDisplay());
			} else {
				openRelatedView();
				ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);
				validateContent();
			}
		}
	}

	private void delayedRefresh(final int time) {
		timer = new Timer(time, this);
		timer.setRepeats(false);
		timer.start();
	}

	public void actionPerformed(ActionEvent e) {
		if (createDesignerUI())
			designer.repaint();
		if (timer != null)
			timer.stop();
	}

	private boolean createDesignerUI() {
		IFileEditorInput file = (IFileEditorInput) getEditorInput();
		setPartName(file.getName());
		setTitleToolTip(file.getToolTipText());
		ParserFactory factory = ParserFactory.getDefaultParserFactory();
		if (factory == null)
			return false;
		ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file.getFile());
		hostProject = unit.getJavaProject();
		ISourceParser sourceParser = factory.newParser();
		sourceParser.setSource(unit);
		boolean success = sourceParser.parse(getShell());
		if (!success)
			return false;
		WidgetAdapter rootAdapter = sourceParser.getResult();
		setUpLookAndFeel(rootAdapter.getWidget().getClass());
		if (designer != null) {
			designer.initRootWidget(rootAdapter);
			return true;
		} else
			return false;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		isGeneratingCode = true;
		IFileEditorInput file = (IFileEditorInput) getEditorInput();
		setPartName(file.getName());
		setTitleToolTip(file.getToolTipText());
		ParserFactory factory = ParserFactory.getDefaultParserFactory();
		if (factory != null) {
			try {
				ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file.getFile());
				hostProject = unit.getJavaProject();
				WorkingCopyOwner owner = new WorkingCopyOwner() {
				};
				ICompilationUnit copy = unit.getWorkingCopy(owner, monitor);
				ISourceParser sourceParser = factory.newParser();
				sourceParser.setSource(copy);
				sourceParser.setLnfChanged(designer.isLnfChanged());
				ImportRewrite imports = JavaUtil.createImportRewrite(copy);
				sourceParser.setImportWrite(imports);
				WidgetAdapter rootAdapter = WidgetAdapter.getWidgetAdapter(designer.getRoot());
				sourceParser.setRootAdapter(rootAdapter);
				boolean success = sourceParser.genCode(monitor);
				if (success) {
					try {
						TextEdit edit = imports.rewriteImports(monitor);
						JavaUtil.applyEdit(copy, edit, true, monitor);
						designer.setLnfChanged(false);
						fireDirty();
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (copy.isWorkingCopy()) {
						copy.commitWorkingCopy(true, monitor);
						copy.discardWorkingCopy();
					}
					if (unit.isWorkingCopy()) {
						unit.commitWorkingCopy(true, monitor);
					}
					designer.clearDirty();
				}
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}
		isGeneratingCode = false;
	}

	class EmbeddedVisualDesigner extends EmbeddedSwingComposite {
		public EmbeddedVisualDesigner(Composite parent) {
			super(parent, SWT.NONE);
		}

		@Override
		protected JComponent createSwingComponent() {
			designer = new VisualDesigner(VisualSwingEditor.this, this);
			JComponent backgroundPanel = new JPanel();
			backgroundPanel.setOpaque(true);
			backgroundPanel.setLayout(new BorderLayout());
			backgroundPanel.add(designer, BorderLayout.CENTER);
			backgroundPanel.setBackground(java.awt.Color.white);
			refreshActionState();
			return backgroundPanel;
		}
	}

	@Override
	public void onEvent(final Event event) {
		getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				int id = event.getId();
				switch (id) {
				case Event.EVENT_SELECTION:
					setSelection((ISelection) event.getParameter());
					refreshActionState();
					break;
				case Event.EVENT_SHOW_POPUP:
					designer.showPopup(event);
					break;
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void setUpLookAndFeel(Class rootClass) {
		try {
			Field field = rootClass.getDeclaredField("PREFERRED_LOOK_AND_FEEL");
			if (field.getType() == String.class) {
				field.setAccessible(true);
				String lnf = (String) field.get(null);
				String className = UIManager.getCrossPlatformLookAndFeelClassName();
				if (lnf == null) {
					lnf = className;
				}
				setLnfClassname(lnf);
			}
		} catch (Exception e) {
			String className = UIManager.getCrossPlatformLookAndFeelClassName();
			setLnfClassname(className);
		}
	}

	public void setLnfClassname(String lnfClassname) {
		ILookAndFeelAdapter adapter = ExtensionRegistry.getLnfAdapter(lnfClassname);
		if (adapter != null) {
			try {
				UIManager.setLookAndFeel(adapter.getLookAndFeelInstance());
			} catch (Exception e) {
			}
		}
		this.lnfClassname = lnfClassname;
	}

	private String lnfClassname;

	public String getLnfClassname() {
		return lnfClassname;
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		switch (event.getType()) {
		case IResourceChangeEvent.POST_CHANGE:
			if (!isGeneratingCode) {
				IResourceDelta delta = event.getDelta();
				checkChange(delta);
			}
			break;
		}
	}

	private void checkChange(IResourceDelta delta) {
		IPath deltaPath = delta.getFullPath();
		IPath path = ((IFileEditorInput) getEditorInput()).getFile().getFullPath();
		if (deltaPath.equals(path)) {
			delayedRefresh(100);
		} else {
			IResourceDelta[] children = delta.getAffectedChildren(IResourceDelta.CHANGED);
			if (children != null && children.length > 0) {
				for (IResourceDelta res : children) {
					checkChange(res);
				}
			}
		}
	}

	@Override
	public void doSaveAs() {
	}

	public void fireDirty() {
		getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				firePropertyChange(IWorkbenchPartConstants.PROP_DIRTY);
			}
		});
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	@Override
	public ISelection getSelection() {
		return selection;
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		if (listeners.contains(listener))
			listeners.remove(listener);
	}

	private void fireSelectionChanged() {
		SelectionChangedEvent evt = new SelectionChangedEvent(this, selection);
		for (ISelectionChangedListener listener : listeners) {
			listener.selectionChanged(evt);
		}
	}

	@Override
	public void setSelection(ISelection selection) {
		if (this.selection == null) {
			if (selection != null && !selection.isEmpty()) {
				this.selection = selection;
				fireSelectionChanged();
			}
		} else {
			if (selection == null) {
				if (!this.selection.isEmpty()) {
					this.selection = null;
					fireSelectionChanged();
				}
			} else {
				if (selection.isEmpty()) {
					if (!this.selection.isEmpty()) {
						this.selection = selection;
						fireSelectionChanged();
					}
				} else {
					if (this.selection.isEmpty()) {
						this.selection = selection;
						fireSelectionChanged();
					} else if (!this.selection.equals(selection)) {
						this.selection = selection;
						fireSelectionChanged();
					}
				}
			}
		}
	}

	public void validateContent() {
		if (designer != null && scrollPane != null) {
			designer.validate();
			final Dimension size = designer.getPreferredSize();
			getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					scrollPane.setMinSize(size.width, size.height);
				}
			});
		}
	}

	@Override
	public void partActivated(IWorkbenchPart part) {
		if (part == this)
			changeToLnf(getLnfClassname());
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
		if (part == this)
			changeToLnf(getLnfClassname());
	}

	@Override
	public void partClosed(IWorkbenchPart part) {
	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
		if (part == this)
			changeToLnf(getLnfClassname());
	}
}
