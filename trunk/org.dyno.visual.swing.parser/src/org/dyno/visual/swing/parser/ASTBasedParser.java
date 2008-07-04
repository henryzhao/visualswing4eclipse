/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.parser;

import java.util.List;

import org.dyno.visual.swing.plugin.spi.ISourceParser;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

class ASTBasedParser implements ISourceParser {
	private ICompilationUnit source;
	private WidgetAdapter result;

	public void setSource(ICompilationUnit source) {
		this.source = source;
	}

	@Override
	public WidgetAdapter getResult() {
		return result;
	}

	@Override
	public boolean parse() {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(source);
		CompilationUnit cunit = (CompilationUnit) parser.createAST(null);
		return processCompilationUnit(cunit);
	}
	@SuppressWarnings("unchecked")
	private boolean processCompilationUnit(CompilationUnit cunit){
		List list=cunit.types();
		for(Object object:list){
			TypeDeclaration type = (TypeDeclaration) object;
			processTypeDeclaration(type);
		}
		return true;
	}

	private void processTypeDeclaration(TypeDeclaration type) {
		FieldDeclaration[] fields = type.getFields();
		for(FieldDeclaration field:fields){
			processField(field);
		}
		MethodDeclaration[] methods = type.getMethods();
		for(MethodDeclaration method:methods){
			processMethod(method);
		}
	}	

	@SuppressWarnings("unchecked")
	private void processMethod(MethodDeclaration method) {
		if(method.isConstructor()){
			System.out.println("constructor!");
		}else{
			System.out.println("normal");
		}		
		Block body = method.getBody();
		List list = body.statements();
		for(Object element:list){
			Statement statement = (Statement)element;
			System.out.println(statement);
		}
	}

	private void processField(FieldDeclaration field) {
		System.out.println("field's name is:"+field.getType());
	}

	@Override
	public boolean genCode(IProgressMonitor monitor) {
		return false;
	}

	@Override
	public void setRootAdapter(WidgetAdapter root) {
	}

	@Override
	public void setImportWrite(ImportRewrite imports) {
	}

	@Override
	public void setLnfChanged(boolean b) {
	}
}
