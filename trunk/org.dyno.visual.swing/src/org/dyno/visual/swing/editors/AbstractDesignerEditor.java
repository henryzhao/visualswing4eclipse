package org.dyno.visual.swing.editors;

import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;
import java.io.InputStream;

import org.dyno.visual.swing.contentTypes.VisualSwingContentDescriber;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.actions.OrganizeImportsAction;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.EditorPart;

public abstract class AbstractDesignerEditor extends EditorPart {
	@Override
	public boolean isSaveOnCloseNeeded() {
		return true;
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		if (shouldSwitchToJavaEditor() || !isSwingComponent()) {
			switchToJavaEditor(getDisplay());
		}
	}

	private boolean shouldSwitchToJavaEditor() throws PartInitException {
		IEditorInput input = getEditorInput();
		if (input instanceof IFileEditorInput) {
			IProject proj = ((IFileEditorInput) input).getFile().getProject();
			try {
				if (!proj.hasNature(JavaCore.NATURE_ID)) {
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
		return getEditorSite().getShell().getDisplay();
	}

	protected boolean isSwingComponent() {
		IEditorInput input = getEditorInput();
		if (input instanceof IFileEditorInput) {
			IFileEditorInput file = (IFileEditorInput) input;
			IContentTypeManager contentTypeManager = Platform.getContentTypeManager();
			InputStream stream = null;
			try {
				stream = file.getFile().getContents();
				IContentType[] contentTypes = contentTypeManager.findContentTypesFor(stream, file.getName());
				for (IContentType contentType : contentTypes) {
					if (contentType.getId().equals(VisualSwingContentDescriber.CONTENT_TYPE_ID_VS)) {
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

	protected void switchToJavaEditor(Display display) {
		final IEditorInput input = getEditorInput();
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbench workbench = PlatformUI.getWorkbench();
				if (workbench != null) {
					IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
					if (window != null) {
						IWorkbenchPage page = window.getActivePage();
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
				}
			}
		});
	}

	protected void openRelatedView() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench != null) {
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			if (window != null) {
				IWorkbenchPage page = window.getActivePage();
				if (page != null) {
					for (String viewId : RELATED_VIEW_IDS) {
						try {
							page.showView(viewId);
						} catch (Exception e) {
						}
					}
				}
			}
		}
	}

	protected void closeRelatedView() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench != null) {
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			if (window != null) {
				IWorkbenchPage page = window.getActivePage();
				if (page != null) {
					IEditorReference[] editorRef = page.getEditorReferences();
					for (IEditorReference ref : editorRef) {
						IEditorPart editor = ref.getEditor(true);
						if (editor instanceof VisualSwingEditor)
							return;
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
		}
	}

	public void openSourceEditor(final WidgetAdapter widget, final EventSetDescriptor eventSet, final MethodDescriptor method) {
		final IEditorInput input = getEditorInput();
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench != null) {
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			if (window != null) {
				IWorkbenchPage page = window.getActivePage();
				if (page != null) {
					try {
						IEditorPart editor = page.openEditor(input, JAVA_EDITOR_ID, true, IWorkbenchPage.MATCH_ID|IWorkbenchPage.MATCH_INPUT);
						OrganizeImportsAction action = new OrganizeImportsAction(editor.getEditorSite());
						IFileEditorInput file = (IFileEditorInput) editor.getEditorInput();
						ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file.getFile());
						action.run(unit);
						editor.doSave(null);
						if (widget != null && eventSet != null && method != null) {
							String name = file.getName();
							int dot = name.lastIndexOf('.');
							if (dot != -1)
								name = name.substring(0, dot);
							IType type = unit.getType(name);
							String mName = (widget.isRoot() ? "" : (widget.getName() + "_")) + eventSet.getName() + "_" + method.getName();
							Class<?>[] params = method.getMethod().getParameterTypes();
							String[] parameters = new String[params == null ? 0 : params.length];
							if (params != null) {
								for (int i = 0; i < params.length; i++) {
									String pcname = params[i].getName();
									dot = pcname.lastIndexOf('.');
									if (dot != -1)
										pcname = pcname.substring(dot + 1);
									parameters[i] = Signature.createTypeSignature(pcname, false);
								}
							}
							IMember member = type.getMethod(mName, parameters);
							JavaUI.revealInEditor(editor, (IJavaElement) member);
						}
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private static final String[] RELATED_VIEW_IDS = { "org.dyno.visual.swing.editors.PaletteView", "org.eclipse.ui.views.PropertySheet",
			"org.eclipse.ui.views.ContentOutline" };
	private static final String JAVA_EDITOR_ID = "org.eclipse.jdt.ui.CompilationUnitEditor";

}
