package org.dyno.visual.swing.types;

import java.util.ArrayList;
import java.util.List;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.dyno.visual.swing.WhiteBoard;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class ImageSelectionDialog extends Dialog {
	private TreeViewer view;
	private Label label;

	private class ProjectTreeContent implements ITreeContentProvider {
		@Override
		public Object[] getChildren(Object parentElement) {
			try {
				if (parentElement instanceof IJavaProject) {
					IJavaProject prj = (IJavaProject) parentElement;
					IJavaElement[] children = prj.getChildren();
					List list = new ArrayList();
					for (IJavaElement jElement : children) {
						if (jElement instanceof IPackageFragmentRoot) {
							IPackageFragmentRoot pkgRoot = (IPackageFragmentRoot) jElement;
							if (!pkgRoot.isArchive())
								list.add(jElement);
						}
					}
					return list.toArray();
				} else if (parentElement instanceof IPackageFragmentRoot) {
					IPackageFragmentRoot pkgRoot = (IPackageFragmentRoot) parentElement;
					return pkgRoot.getChildren();
				} else if (parentElement instanceof IPackageFragment) {
					IPackageFragment pkg = (IPackageFragment) parentElement;
					Object[] nonJavaResources = pkg.getNonJavaResources();
					List list = new ArrayList();
					for (Object resource : nonJavaResources) {
						if (resource instanceof IFile) {
							IFile file = (IFile) resource;
							String name = file.getName();
							if (name != null) {
								name = name.toLowerCase();
								if (name.endsWith(".gif")
										|| name.endsWith(".png")
										|| name.endsWith(".jpg")) {
									list.add(file);
								}
							}
						}
					}
					return list.toArray();
				} else {
					System.out.println();
				}
			} catch (Exception e) {
				VisualSwingPlugin.getLogger().error(e);
			}
			return new Object[0];
		}

		@Override
		public Object getParent(Object element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			try {
				IJavaElement[] children = null;
				if (element instanceof IPackageFragmentRoot) {
					IPackageFragmentRoot packageRoot = (IPackageFragmentRoot) element;
					children = packageRoot.getChildren();
				} else if (element instanceof IPackageFragment) {
					IPackageFragment pkg = (IPackageFragment) element;
					Object[] nonJavaResources = pkg.getNonJavaResources();
					for (Object resource : nonJavaResources) {
						if (resource instanceof IFile) {
							IFile file = (IFile) resource;
							String name = file.getName();
							if (name != null) {
								name = name.toLowerCase();
								if (name.endsWith(".gif")
										|| name.endsWith(".png")
										|| name.endsWith(".jpg")) {
									return true;
								}
							}
						}
					}
					return false;
				}
				return children != null && children.length > 0;
			} catch (Exception e) {
				VisualSwingPlugin.getLogger().error(e);
			}
			return false;
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	class ProjectLabelProvider extends LabelProvider implements ILabelProvider {
		@Override
		public Image getImage(Object element) {
			if (element == null)
				return null;
			if (element instanceof IPackageFragmentRoot) {
				return JavaUI.getSharedImages().getImage(
						ISharedImages.IMG_OBJS_PACKFRAG_ROOT);
			} else if (element instanceof IPackageFragment) {
				return JavaUI.getSharedImages().getImage(
						ISharedImages.IMG_OBJS_PACKAGE);
			} else if (element instanceof IFile) {
				return PlatformUI.getWorkbench().getSharedImages().getImage(
						org.eclipse.ui.ISharedImages.IMG_OBJ_FILE);
			}
			return null;
		}

		@Override
		public String getText(Object element) {
			if (element == null)
				return "";
			if (element instanceof IJavaElement) {
				IJavaElement java = (IJavaElement) element;
				String name = java.getElementName();
				if (name == null || name.trim().length() == 0) {
					if (java instanceof IPackageFragment) {
						return "(default package)";
					}
				}
				return name;
			} else if (element instanceof IFile) {
				IFile file = (IFile) element;
				String name = file.getName();
				return name;
			}
			return element.toString();
		}

		@Override
		public void dispose() {
		}
	}

	protected ImageSelectionDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		area.setLayout(layout);
		view = new TreeViewer(area, SWT.BORDER);
		GridData data = new GridData();
		data.widthHint = 350;
		data.heightHint = 200;
		view.getTree().setLayoutData(data);
		view.setContentProvider(new ProjectTreeContent());
		view.setLabelProvider(new ProjectLabelProvider());
		view.setInput(WhiteBoard.getCurrentProject());
		view.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				view_selectionChanged(event);
			}
		});
		if(imgFile!=null){
			view.setSelection(new StructuredSelection(imgFile));
		}
		label = new Label(area, SWT.BORDER);
		data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		data.heightHint = 50;
		label.setLayoutData(data);
		return parent;
	}

	private void view_selectionChanged(SelectionChangedEvent event) {
		TreeSelection sel = (TreeSelection) event.getSelection();
		Object selected = sel.getFirstElement();
		if (selected != null) {
			if (selected instanceof IFile) {
				IFile file = (IFile) selected;
				String name = file.getName();
				name = name.toLowerCase();
				if (name.endsWith(".png") || name.endsWith(".gif")
						|| name.endsWith(".jpg")) {
					getButton(IDialogConstants.OK_ID).setEnabled(true);
					this.imgFile = file;
					updatePicture();
					return;
				}
			}
		}
		getButton(IDialogConstants.OK_ID).setEnabled(false);
	}
	private Image image;
	private void updatePicture() {
		if (imgFile != null) {
			try {
				ImageDescriptor d = ImageDescriptor.createFromURL(imgFile.getLocationURI().toURL());
				if(image!=null)
					image.dispose();
				image = d.createImage();
				label.setImage(image);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean close() {
		if(image!=null)
			image.dispose();
		return super.close();
	}

	@Override
	protected void cancelPressed() {
		imgFile = null;
		super.cancelPressed();
	}

	public IFile getImageFile() {
		return imgFile;
	}

	public void setImageFile(IFile file) {
		this.imgFile = file;
	}

	private IFile imgFile;
}
