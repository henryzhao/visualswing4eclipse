package org.dyno.visual.swing.parser;

import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class NamespaceUtil {
	private static String GET = "get";
	private static String INIT = "init";
	public static String getGetMethodName(String name) {
		return GET + getCapitalName(name);
	}
	public static String getGetMethodName(WidgetAdapter adapter, String name){
		String methodName = (String) adapter.getProperty("getMethodName");
		if(methodName!=null)
			return methodName;
		return getGetMethodName(name);
	}
	public static String getFieldNameFromGetMethodName(String getMethodName) {
		String name = getMethodName.substring(GET.length());
		name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
		return name;
	}
	public static String getGetMethodName(CompilationUnit cunit, final String fieldName){
		TypeDeclaration type = (TypeDeclaration) cunit.types().get(0);
		final String[]ret=new String[1];
		type.accept(new ASTVisitor(){
			@Override
			public boolean visit(ReturnStatement node) {
				Expression exp = node.getExpression();
				if(exp!=null&&exp instanceof SimpleName){
					String retName = ((SimpleName)exp).getFullyQualifiedName();
					if(retName.equals(fieldName)){
						ASTNode current = node;
						while(current!=null&&!(current instanceof MethodDeclaration))
							current = current.getParent();
						if(current!=null){
							MethodDeclaration bingo=(MethodDeclaration) current;
							ret[0]=bingo.getName().getFullyQualifiedName();
							return false;
						}
					}
				}
				return true;
			}
		});
		return ret[0];
	}	
	public static String findReturnFieldName(MethodDeclaration method){
		final String[]ret=new String[1];
		method.accept(new ASTVisitor(){
			@Override
			public boolean visit(ReturnStatement node) {
				Expression exp=node.getExpression();
				if(exp instanceof SimpleName ){
					ret[0]=((SimpleName)exp).getFullyQualifiedName();
				}
				return false;
			}
		});
		return ret[0];
	}

	public static String getCapitalName(String name) {
		return Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}
	
	public static String getInitMethodName(String name){
		return INIT+getCapitalName(name);
	}
}
