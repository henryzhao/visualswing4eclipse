/************************************************************************************
 * Copyright (c) 2008 William Chen.                                                 *
 *                                                                                  *
 * All rights reserved. This program and the accompanying materials are made        *
 * available under the terms of the Eclipse Public License v1.0 which accompanies   *
 * this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html *
 *                                                                                  *
 * Use is subject to the terms of Eclipse Public License v1.0.                      *
 *                                                                                  *
 * Contributors:                                                                    * 
 *     William Chen - initial API and implementation.                               *
 ************************************************************************************/

package org.dyno.visual.swing.editors;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.base.EditorAction;
import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.ISyncUITask;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.designer.VisualDesigner;
import org.dyno.visual.swing.plugin.spi.ILookAndFeelAdapter;
import org.dyno.visual.swing.plugin.spi.ISourceParser;
import org.dyno.visual.swing.plugin.spi.ParserFactory;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.albireo.core.AwtEnvironment;
import org.eclipse.albireo.core.SwingControl;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.views.contentoutline.ContentOutline;
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
@SuppressWarnings("unchecked")
public class VisualSwingEditor extends AbstractDesignerEditor implements IResourceChangeListener, ISelectionProvider, IPartListener, ISelectionListener {
	private static final String EDITOR_IMAGE = "/icons/editor.png";
	private static final int DELAYED_TIME = 250;
	private List<ISelectionChangedListener> listeners;
	private SwingControl embedded;
	private VisualDesigner designer;
	private VisualSwingOutline outline;
	private HashMap<String, EditorAction> actions;
	private ArrayList<EditorAction> actionList;
	private IJavaProject hostProject;
	private boolean isGeneratingCode;
	private ISelection selection;
	private PropertySheetPage sheetPage;
	private ScrolledComposite scrollPane;
	private PalettePage palettePage;
	private boolean isParsing;
	private boolean isCreatingDesignerUI;
	private String lnfClassname;

	public VisualSwingEditor() {
		super();
		actions = new HashMap<String, EditorAction>();
		actionList = new ArrayList<EditorAction>();
		listeners = new ArrayList<ISelectionChangedListener>();
	}

	public IJavaProject getHostProject() {
		return hostProject;
	}

	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		site.getWorkbenchWindow().getPartService().addPartListener(this);
		site.setSelectionProvider(this);
		site.getWorkbenchWindow().getSelectionService().addSelectionListener(this);
	}

	public Object getAdapter(Class adapter) {
		if (adapter == IContentOutlinePage.class) {
			if (outline == null && designer != null) {
				outline = new VisualSwingOutline(designer);
			}
			return outline;
		} else if (adapter == IPropertySheetPage.class) {
			if (sheetPage == null) {
				sheetPage = new WidgetPropertyPage();
				sheetPage.setPropertySourceProvider(new WidgetAdapterContentProvider());
			}
			return sheetPage;
		} else if (adapter == IPalettePage.class) {
			if (palettePage == null) {
				if (designer != null)
					palettePage = new PalettePage(designer);
				else
					palettePage = new PalettePage(this);
			}
			return palettePage;
		} else
			return super.getAdapter(adapter);
	}

	public boolean isDirty() {
		if (super.isDirty())
			return true;
		return !isGeneratingCode && designer != null && (designer.isLnfChanged() || designer.isDirty());
	}

	public boolean isSaveAsAllowed() {
		return super.isSaveAsAllowed();
	}

	public boolean isSaveOnCloseNeeded() {
		return super.isSaveOnCloseNeeded();
	}

	public ArrayList<EditorAction> getActions() {
		return actionList;
	}

	public VisualDesigner getDesigner() {
		return designer;
	}

	public void refreshActionState() {
		if (designer != null) {
			for (EditorAction action : actions.values())
				action.updateState();
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

	public IAction getEditorAction(String id){
		return actions.get(id);
	}
	public void setFocus() {
		if (embedded != null) {
			embedded.setFocus();
		}
		VisualSwingPlugin.setCurrentEditor(this);
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

	public void createPartControl(Composite parent) {
		if (!isSwingComponent()) {
			switchToJavaEditor();
		} else {
			parent.setLayout(new FillLayout());
			Splitter splitter = new Splitter(parent, SWT.VERTICAL);
			Composite design = new Composite(splitter, SWT.NONE);
			splitter.setFirstControl(design);
			design.setLayout(new FillLayout());
			scrollPane = new ScrolledComposite(design, SWT.H_SCROLL | SWT.V_SCROLL);
			scrollPane.setExpandHorizontal(true);
			scrollPane.setExpandVertical(true);
			Composite designerContainer = new Composite(scrollPane, SWT.NONE);
			scrollPane.setContent(designerContainer);
			designerContainer.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
			designerContainer.setLayout(new FillLayout());
			embedded = new EmbeddedVisualDesigner(designerContainer);
			Composite comps = new Composite(splitter, SWT.NONE);
			splitter.setSecondControl(comps);

			comps.setLayout(new FillLayout());
			super.createPartControl(comps);
			getSite().setSelectionProvider(this);
			setTitleImage(VisualSwingPlugin.getSharedImage(EDITOR_IMAGE));
		}
	}

	@Override
	protected void createActions() {
		super.createActions();
		replaceAction(ITextEditorActionConstants.CUT);
		replaceAction(ITextEditorActionConstants.COPY);
		replaceAction(ITextEditorActionConstants.PASTE);
		replaceAction(ITextEditorActionConstants.SELECT_ALL);
		replaceAction(ITextEditorActionConstants.DELETE);
	}
	private void replaceAction(String id){
		IAction action = getAction(id);
		setAction(id, null);
		setAction(id, new DelegateAction(this, action));
	}
	private void onEditorOpen() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this,
				IResourceChangeEvent.POST_CHANGE | IResourceChangeEvent.POST_BUILD | IResourceChangeEvent.PRE_CLOSE | IResourceChangeEvent.PRE_DELETE);
		invokeLater(new Runnable() {

			public void run() {
				validateContent();
			}
		});
		asyncRunnable(new Runnable() {

			public void run() {
				openRelatedView();
			}
		});
	}

	class CreateDesignerUIJob extends Job {
		private boolean isTimerAction;

		public CreateDesignerUIJob(boolean isTimer) {
			super(Messages.VisualSwingEditor_Designer_Creation_Job);
			setPriority(INTERACTIVE);
			this.isTimerAction = isTimer;
		}

		protected IStatus run(IProgressMonitor monitor) {
			monitor.setTaskName(Messages.VisualSwingEditor_Generating_Designer);
			try {
				createDesignerUI(monitor);
			} catch (Exception e) {
				designer.initRootWidget(null);
				designer.setError(e);
			}
			if (isTimerAction)
				validateContent();
			else
				onEditorOpen();
			isCreatingDesignerUI = false;
			designer.unlock();
			monitor.done();
			return Status.OK_STATUS;
		}
	}

	private void createDesignerUI(IProgressMonitor monitor) throws Exception {
		final IFileEditorInput file = (IFileEditorInput) getEditorInput();
		asyncRunnable(new Runnable() {

			public void run() {
				setPartName(file.getName());
				setTitleToolTip(file.getToolTipText());
			}
		});
		ParserFactory factory = ParserFactory.getDefaultParserFactory();
		if (factory == null)
			throw new Exception("No parser factory available!");
		ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file.getFile());
		hostProject = unit.getJavaProject();
		ISourceParser sourceParser = factory.newParser();
		isParsing = true;
		VisualSwingPlugin.setCurrentEditor(this);
		this.designer.setCompilationUnit(unit);
		try {
			WidgetAdapter adapter = sourceParser.parse(unit, monitor);
			if (adapter != null) {
				designer.initRootWidget(adapter);
				setUpLookAndFeel(adapter.getWidget().getClass());
				designer.initNamespaceWithUnit(unit);
				refreshTree();
			} else {
				throw new Exception("This class is not a valid swing class!");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			isParsing = false;
		}
	}

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
		} catch (NoSuchFieldException nsfe) {
			String className = UIManager.getCrossPlatformLookAndFeelClassName();
			setLnfClassname(className);
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
			String className = UIManager.getCrossPlatformLookAndFeelClassName();
			setLnfClassname(className);
		}
	}

	private void refreshTree() {
		asyncRunnable(new Runnable() {

			public void run() {
				if (outline != null)
					outline.refreshTree();
			}
		});
	}

	private void safeSave(final IProgressMonitor monitor) {
		getDisplay().syncExec(new Runnable() {

			public void run() {
				doSave(monitor);
			}
		});
	}

	public void saveWithProgress() {
		try {
			IRunnableWithProgress runnable = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) {
					safeSave(monitor);
				}
			};
			ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell());
			dialog.run(true, true, runnable);
		} catch (Exception e) {
			VisualSwingPlugin.getLogger().error(e);
			return;
		}
	}

	public void doSave(IProgressMonitor monitor) {
		if (super.isDirty()) {
			super.doSave(monitor);
		} else {
			isGeneratingCode = true;
			try {
				IFileEditorInput file = (IFileEditorInput) getEditorInput();
				setPartName(file.getName());
				setTitleToolTip(file.getToolTipText());
				ParserFactory factory = ParserFactory.getDefaultParserFactory();
				if (factory != null) {
					ISourceParser sourceParser = factory.newParser();
					Component root = designer.getRoot();
					if (root != null) {
						WidgetAdapter rootAdapter = WidgetAdapter.getWidgetAdapter(root);
						JavaUtil.hideMenu();
						String lnfCN = getLnfClassname();
						rootAdapter.setPreferredLookAndFeel(lnfCN); //$NON-NLS-1$
						ICompilationUnit unit = sourceParser.generate(rootAdapter, monitor);
						rootAdapter.setPreferredLookAndFeel(null); //$NON-NLS-1$
						if (unit != null) {
							designer.initNamespaceWithUnit(unit);
							designer.setLnfChanged(false);
						}
						designer.clearDirty();
						fireDirty();
					}
				}
			} catch (Exception e) {
				VisualSwingPlugin.getLogger().error(e);
			}
			isGeneratingCode = false;
		}
	}

	class EmbeddedVisualDesigner extends SwingControl {
		public EmbeddedVisualDesigner(Composite parent) {
			super(parent, SWT.NONE);
		}

		protected JComponent createSwingComponent() {
			designer = new VisualDesigner(VisualSwingEditor.this, this);
			JPanel backgroundPanel = new JPanel();
			backgroundPanel.setOpaque(true);
			backgroundPanel.setLayout(new BorderLayout());
			backgroundPanel.add(designer, BorderLayout.CENTER);
			backgroundPanel.setBackground(java.awt.Color.white);
			refreshActionState();
			if (!isCreatingDesignerUI) {
				isCreatingDesignerUI = true;
				new CreateDesignerUIJob(false).schedule();
			}
			return backgroundPanel;
		}

		public Composite getLayoutAncestor() {
			return getParent();
		}

	}

	public void setLnfClassname(String lnfClassname) {
		ILookAndFeelAdapter adapter = ExtensionRegistry.getLnfAdapter(lnfClassname);
		if (adapter != null) {
			try {
				LookAndFeel oldlnf = UIManager.getLookAndFeel();
				LookAndFeel newlnf = adapter.getLookAndFeelInstance();
				if (!isLnfEqual(oldlnf, newlnf)) {
					AwtEnvironment.runWithLnf(newlnf, new ISyncUITask() {

						public Object doTask() throws Throwable {
							if (designer != null) {
								Window window = SwingUtilities.getWindowAncestor(designer);
								if (window != null)
									SwingUtilities.updateComponentTreeUI(window);
							}
							return null;
						}
					});
				}
			} catch (Throwable e) {
				VisualSwingPlugin.getLogger().error(e);
			}
		}
		this.lnfClassname = lnfClassname;
	}

	private boolean isLnfEqual(LookAndFeel lnf1, LookAndFeel lnf2) {
		if (lnf1 == null) {
			if (lnf2 == null) {
				return true;
			} else {
				return false;
			}
		} else {
			if (lnf2 == null) {
				return false;
			} else {
				String lnfId1 = lnf1.getID();
				String lnfId2 = lnf2.getID();
				if (!lnfId1.equals(lnfId2)) {
					return false;
				} else {
					Class clazz1 = lnf1.getClass();
					Class clazz2 = lnf2.getClass();
					return clazz1.equals(clazz2);
				}
			}
		}
	}

	public String getLnfClassname() {
		return lnfClassname;
	}

	public void resourceChanged(IResourceChangeEvent event) {
		switch (event.getType()) {
		case IResourceChangeEvent.POST_BUILD:
		case IResourceChangeEvent.POST_CHANGE:
			if (!isGeneratingCode) {
				IResourceDelta delta = event.getDelta();
				checkChange(delta);
			}
			break;
		case IResourceChangeEvent.PRE_CLOSE:
		case IResourceChangeEvent.PRE_DELETE:
			if (!isGeneratingCode) {
				IResource resource = event.getResource();
				if (hostProject.getProject() == resource) {
					closeRelatedView();
					closeMe();
				}
			}
			break;
		}
	}

	private void checkChange(IResourceDelta delta) {
		switch (delta.getKind()) {
		case IResourceDelta.REMOVED:
			IPath deltaPath = delta.getFullPath();
			IPath path = ((IFileEditorInput) getEditorInput()).getFile().getFullPath();
			if (deltaPath.equals(path)) {
				closeRelatedView();
				closeMe();
			}
			break;
		case IResourceDelta.CHANGED:
			deltaPath = delta.getFullPath();
			path = ((IFileEditorInput) getEditorInput()).getFile().getFullPath();
			if (deltaPath.equals(path)) {
				if (!isCreatingDesignerUI && !designer.isLocked()) {
					isCreatingDesignerUI = true;
					new CreateDesignerUIJob(true).schedule();
				}
			} else {
				IResourceDelta[] children = delta.getAffectedChildren(IResourceDelta.CHANGED | IResourceDelta.REMOVED);
				if (children != null && children.length > 0) {
					for (IResourceDelta res : children) {
						checkChange(res);
					}
				}
			}
		}
	}

	public void doSaveAs() {
		super.doSaveAs();
	}

	public void fireDirty() {
		asyncRunnable(new Runnable() {

			public void run() {
				firePropertyChange(IWorkbenchPartConstants.PROP_DIRTY);
			}
		});
	}

	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	public ISelection getSelection() {
		if (embedded!=null&&embedded.isFocusControl())
			return selection;
		else
			return super.getSelectionProvider().getSelection();
	}

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

	public void setSelection(ISelection selection) {
		if (this.selection == null) {
			if (selection != null && !selection.isEmpty()) {
				this.selection = selection;
				fireSelectionChanged();
			} else {
				if (outline != null)
					outline.refreshTree();
			}
		} else {
			if (selection == null) {
				if (!this.selection.isEmpty()) {
					this.selection = null;
					fireSelectionChanged();
				} else {
					if (outline != null)
						outline.refreshTree();
				}
			} else {
				if (selection.isEmpty()) {
					if (!this.selection.isEmpty()) {
						this.selection = selection;
						fireSelectionChanged();
					} else {
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
		if (outline != null)
			outline.refreshTree();
	}

	public void validateContent() {
		if (designer != null && scrollPane != null) {
			final Dimension size = designer.getPreferredSize();
			asyncRunnable(new Runnable() {
				public void run() {
					scrollPane.setMinSize(size.width, size.height);
				}
			});
			designer.refreshDesigner();
		}
	}

	public void asyncRunnable(Runnable runnable) {
		getDisplay().asyncExec(runnable);
	}

	private void invokeLater(Runnable runnable) {
		SwingUtilities.invokeLater(runnable);
	}

	public void partActivated(IWorkbenchPart part) {
		if (part == this) {
			delaySwingExec(DELAYED_TIME, new ChangeLnfAction());
		}
	}

	private class ChangeLnfAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (isParsing)
				return;
			changeToLnf(getLnfClassname());
			if (designer != null)
				designer.clearCapture();
		}
	}

	private void delaySwingExec(int millies, ActionListener action) {
		Timer timer = new Timer(millies, action);
		timer.setRepeats(false);
		timer.start();
	}

	public void partBroughtToTop(IWorkbenchPart part) {
		if (part == this) {
			delaySwingExec(DELAYED_TIME, new ChangeLnfAction());
		}
	}

	public void partClosed(IWorkbenchPart part) {
	}

	public void partDeactivated(IWorkbenchPart part) {
		if (part == this) {
			if (designer != null)
				designer.capture();
		}
	}

	public void partOpened(IWorkbenchPart part) {
		if (part == this) {
			delaySwingExec(DELAYED_TIME, new ChangeLnfAction());
		}
	}

	public void clearToolSelection() {
		palettePage.clearToolSelection();
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part instanceof ContentOutline && !selection.isEmpty() && selection instanceof IStructuredSelection) {
			designer.clearSelection();
			for (Object obj : ((IStructuredSelection) selection).toList()) {
				if (obj instanceof Component) {
					Component comp = (Component) obj;
					WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(comp);
					adapter.setSelected(true);
				}
			}
			designer.refreshDesigner();
		}
	}

	public boolean isFocusOnDesigner() {
		return embedded.isFocusControl();
	}
}
