package org.noname.designer.core.internal.editor;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.noname.designer.core.interfaces.EvaluationContext;
import org.noname.designer.core.interfaces.FrameContext;
import org.noname.designer.core.interfaces.IEvaluator;
import org.noname.designer.core.interfaces.VariableContext;
import org.noname.designer.core.internal.contenttypes.NonameContentType;

public class ASTParserJob extends Job {
	private String componentType;
	private ICompilationUnit unit;
	private CompilationUnit cunit;
	
	public ASTParserJob(IFile file) {
		super("AST Parsing Job");
		init(file);
	}

	private void init(IFile file) {
		try {
			IContentDescription desc = file.getContentDescription();
			if (desc != null) {
				componentType = (String) desc
						.getProperty(NonameContentType.COMPONENT_TYPE);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		unit = JavaCore.createCompilationUnitFrom(file);
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(unit);
		cunit = (CompilationUnit) parser.createAST(null);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		if (componentType != null) {
			EvaluationContext context = createContext();
			TypeDeclaration typeDec = getCompTypeDeclaration();
			if (typeDec != null) {
				MethodDeclaration constructor = getConstructor(typeDec);
				if (constructor != null) {
					IEvaluator evaluator = (IEvaluator) Platform
							.getAdapterManager().getAdapter(constructor,
									IEvaluator.class);
					if (evaluator != null) {
						int result = evaluator.evaluate(context);
						FrameContext consFrame = context.popup();
						if (result == IEvaluator.RETURN) {
							System.out.println("this="
									+ context.getThisObject());
							for (String fieldName : context.getFieldNames()) {
								System.out.println(fieldName + "="
										+ context.getField(fieldName));
							}
						} else if (result == IEvaluator.THROW) {
							Throwable throwable = (Throwable) consFrame.getResult();
							throwable.printStackTrace();
						}
						return Status.OK_STATUS;
					}
				}
			}
		}
		return Status.CANCEL_STATUS;
	}

	private EvaluationContext createContext() {
		Object thisObj = createObject(componentType);
		EvaluationContext context = new EvaluationContext(thisObj);
		FrameContext frame = new FrameContext();
		context.push(frame);
		VariableContext arguments = new VariableContext();
		frame.push(arguments);
		return context;
	}

	private MethodDeclaration getConstructor(TypeDeclaration typeDec) {
		MethodDeclaration constructor = null;
		for(MethodDeclaration method:typeDec.getMethods()){
			if(method.isConstructor()){
				List parameters = method.parameters();
				if(parameters==null||parameters.isEmpty()){
					constructor = method;
					break;
				}
			}
		}
		return constructor;
	}

	private TypeDeclaration getCompTypeDeclaration() {
		TypeDeclaration typeDec = null;
		List types = cunit.types();
		IType type = null;
		try {
			type = unit.getTypes()[0];
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(type == null)
			return null;
		String eName = type.getElementName();
		int dot = eName.lastIndexOf('.');
		if(dot!=-1)
			eName = eName.substring(dot+1);
		System.out.println(eName);
		for(int i=0;i<types.size();i++){
			TypeDeclaration tDec = (TypeDeclaration) types.get(i);
			SimpleName sName = tDec.getName();
			String tName = sName.getIdentifier();
			System.out.println(tName);
			if(tName.equals(eName)){
				typeDec = tDec;
				break;
			}
		}
		return typeDec;
	}
	private Object createObject(String className){
		try {
			Class clazz = Class.forName(className);
			return clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
