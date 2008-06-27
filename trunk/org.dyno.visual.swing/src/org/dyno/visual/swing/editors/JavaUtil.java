package org.dyno.visual.swing.editors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.ui.CodeStyleConfiguration;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.RewriteSessionEditProcessor;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

class JavaUtil {

	public static void applyEdit(ICompilationUnit cu, TextEdit edit, boolean save, IProgressMonitor monitor) throws CoreException {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		monitor.beginTask("Applying changes", 3);

		try {
			IDocument document = null;
			try {
				document = aquireDocument(cu, new SubProgressMonitor(monitor, 1));
				if (save) {
					commitDocument(cu, document, edit, new SubProgressMonitor(monitor, 1));
				} else {
					new RewriteSessionEditProcessor(document, edit, TextEdit.UPDATE_REGIONS).performEdits();
				}
			} catch (BadLocationException e) {
				e.printStackTrace();
			} finally {
				releaseDocument(cu, document, new SubProgressMonitor(monitor, 1));
			}
		} finally {
			monitor.done();
		}
	}

	private static void releaseDocument(ICompilationUnit cu, IDocument document, IProgressMonitor monitor) throws CoreException {
		if (cu.getOwner() == null) {
			IFile file = (IFile) cu.getResource();
			if (file.exists()) {
				ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
				bufferManager.disconnect(file.getFullPath(), LocationKind.IFILE, monitor);
				return;
			}
		}
		cu.getBuffer().setContents(document.get());
		monitor.done();
	}

	private static IDocument aquireDocument(ICompilationUnit cu, IProgressMonitor monitor) throws CoreException {
		if (cu.getOwner() == null) {
			IFile file = (IFile) cu.getResource();
			if (file.exists()) {
				ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
				IPath path = cu.getPath();
				bufferManager.connect(path, LocationKind.IFILE, monitor);
				return bufferManager.getTextFileBuffer(path, LocationKind.IFILE).getDocument();
			}
		}
		monitor.done();
		return new Document(cu.getSource());
	}

	private static void commitDocument(ICompilationUnit cu, IDocument document, TextEdit edit, IProgressMonitor monitor) throws CoreException,
			MalformedTreeException, BadLocationException {
		if (cu.getOwner() == null) {
			IFile file = (IFile) cu.getResource();
			if (file.exists()) {
				IStatus status = makeCommittable(new IResource[] { file }, null);
				if (!status.isOK()) {
					throw new CoreException(status);
				}
				new RewriteSessionEditProcessor(document, edit, TextEdit.UPDATE_REGIONS).performEdits(); // apply
				// after
				// file
				// is
				// commitable

				ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
				bufferManager.getTextFileBuffer(file.getFullPath(), LocationKind.IFILE).commit(monitor, true);
				return;
			}
		}
		new RewriteSessionEditProcessor(document, edit, TextEdit.UPDATE_REGIONS).performEdits();
	}

	private static IStatus makeCommittable(IResource[] resources, Object context) {
		List<IResource> readOnlyFiles = new ArrayList<IResource>();
		for (int i = 0; i < resources.length; i++) {
			IResource resource = resources[i];
			if (resource.getType() == IResource.FILE && isReadOnly(resource))
				readOnlyFiles.add(resource);
		}
		if (readOnlyFiles.size() == 0)
			return new Status(IStatus.OK, VisualSwingPlugin.getPluginID(), IStatus.OK, "", null); //$NON-NLS-1$

		Map<IFile, Long> oldTimeStamps = createModificationStampMap(readOnlyFiles);
		IStatus status = ResourcesPlugin.getWorkspace().validateEdit(readOnlyFiles.toArray(new IFile[readOnlyFiles.size()]), context);
		if (!status.isOK())
			return status;

		IStatus modified = null;
		Map<IFile, Long> newTimeStamps = createModificationStampMap(readOnlyFiles);
		for (Iterator<IFile> iter = oldTimeStamps.keySet().iterator(); iter.hasNext();) {
			IFile file = iter.next();
			if (!oldTimeStamps.get(file).equals(newTimeStamps.get(file)))
				modified = addModified(modified, file);
		}
		if (modified != null)
			return modified;
		return new Status(IStatus.OK, VisualSwingPlugin.getPluginID(), IStatus.OK, "", null); //$NON-NLS-1$
	}

	private static final int VALIDATE_EDIT_CHANGED_CONTENT = 10003;

	private static IStatus addModified(IStatus status, IFile file) {
		String message = "File " + file.getFullPath().toString() + " has been modified since the beginning of the operation";
		IStatus entry = new Status(VALIDATE_EDIT_CHANGED_CONTENT, message, null);
		if (status == null) {
			return entry;
		} else if (status.isMultiStatus()) {
			((MultiStatus) status).add(entry);
			return status;
		} else {
			MultiStatus result = new MultiStatus(VisualSwingPlugin.getPluginID(), VALIDATE_EDIT_CHANGED_CONTENT, "There are modified resources", null);
			result.add(status);
			result.add(entry);
			return result;
		}
	}

	private static Map<IFile, Long> createModificationStampMap(List<IResource> files) {
		Map<IFile, Long> map = new HashMap<IFile, Long>();
		for (Iterator<IResource> iter = files.iterator(); iter.hasNext();) {
			IFile file = (IFile) iter.next();
			map.put(file, new Long(file.getModificationStamp()));
		}
		return map;
	}

	private static boolean isReadOnly(IResource resource) {
		ResourceAttributes resourceAttributes = resource.getResourceAttributes();
		if (resourceAttributes == null)
			return false;
		return resourceAttributes.isReadOnly();
	}
	public static ImportRewrite createImportRewrite(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(unit);
		parser.setResolveBindings(false);
		parser.setFocalPosition(0);
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		return CodeStyleConfiguration.createImportRewrite(cu, true);
	}	
}