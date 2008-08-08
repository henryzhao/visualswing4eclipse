/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.editors;

import java.io.InputStream;

import org.dyno.visual.swing.contentTypes.VisualSwingContentDescriber;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.EditorPart;

/**
 * 
 * AbstractDesignerEditor
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public abstract class AbstractDesignerEditor extends EditorPart {
	@Override
	public boolean isSaveOnCloseNeeded() {
		return true;
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		if (shouldSwitchToJavaEditor() || !isSwingComponent()) {
			switchToJavaEditor();
		}
	}

	private boolean shouldSwitchToJavaEditor() throws PartInitException {
		IEditorInput input = getEditorInput();
		if (input instanceof IFileEditorInput) {
			IProject proj = ((IFileEditorInput) input).getFile().getProject();
			try {
				if (!proj.hasNature(NATURE_ID)) {
					return true;
				}
			} catch (CoreException e) {
				return true;
			}
		} else
			throw new PartInitException("This is not a legal file input!");
		return false;
	}

	public Display getDisplay() {
		return getShell().getDisplay();
	}

	public Shell getShell() {
		return getEditorSite().getShell();
	}

	protected boolean isSwingComponent() {
		IEditorInput input = getEditorInput();
		if (input instanceof IFileEditorInput) {
			IFileEditorInput file = (IFileEditorInput) input;
			IContentTypeManager contentTypeManager = Platform
					.getContentTypeManager();
			InputStream stream = null;
			try {
				stream = file.getFile().getContents();
				IContentType[] contentTypes = contentTypeManager
						.findContentTypesFor(stream, file.getName());
				for (IContentType contentType : contentTypes) {
					if (contentType.getId().equals(
							VisualSwingContentDescriber.CONTENT_TYPE_ID_VS)) {
						return true;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (stream != null)
					try {
						stream.close();
					} catch (Exception e) {
					}
			}
		}
		return false;
	}

	protected void switchToJavaEditor() {
		getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				IEditorInput input = getEditorInput();
				IWorkbenchPage page = getEditorSite().getWorkbenchWindow()
						.getActivePage();
				if (page != null) {
					page.closeEditor(AbstractDesignerEditor.this, false);
					try {
						page.openEditor(input, JAVA_EDITOR_ID);
						IFileEditorInput file_editor_input = (IFileEditorInput) input;
						IFile file = file_editor_input.getFile();
						IDE.setDefaultEditor(file, JAVA_EDITOR_ID);
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	protected void openRelatedView() {
		IWorkbenchPage page = getEditorSite().getPage();
		if (page != null) {
			for (String viewId : RELATED_VIEW_IDS) {
				try {
					page.showView(viewId);
				} catch (Exception e) {
				}
			}
		}
	}

	protected void closeMe() {
		getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				IWorkbenchPage page = getEditorSite().getWorkbenchWindow()
						.getActivePage();
				if (page != null) {
					page.closeEditor(AbstractDesignerEditor.this, false);
				}
			}
		});
	}

	protected void closeRelatedView() {
		IWorkbenchPage page = getEditorSite().getPage();
		if (page != null) {
			IEditorReference[] editorRef = page.getEditorReferences();
			for (IEditorReference ref : editorRef) {
				try {
					IEditorPart editor = ref.getEditor(true);
					if (editor instanceof VisualSwingEditor)
						return;
				} catch (Exception e) {
				}
			}
			for (String viewId : RELATED_VIEW_IDS) {
				try {
					IViewPart part = page.findView(viewId);
					page.hideView(part);
				} catch (Exception e) {
				}
			}
		}
	}

	public IEditorPart openSouceEditor() {
		IEditorInput input = getEditorInput();
		IWorkbenchPage page = getEditorSite().getPage();
		if (page != null) {
			try {
				return page.openEditor(input, JAVA_EDITOR_ID, true,
						IWorkbenchPage.MATCH_ID | IWorkbenchPage.MATCH_INPUT);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static final String[] RELATED_VIEW_IDS = {
			"org.dyno.visual.swing.editors.PaletteView",
			"org.eclipse.ui.views.PropertySheet",
			"org.eclipse.ui.views.ContentOutline" };
	private static final String JAVA_EDITOR_ID = "org.eclipse.jdt.ui.CompilationUnitEditor";
	private static final String NATURE_ID = "org.eclipse.jdt.core.javanature";
}
