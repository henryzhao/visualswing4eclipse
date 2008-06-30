package org.dyno.visual.swing.parser;

import org.dyno.visual.swing.plugin.spi.ISourceParser;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
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
		ASTVisitor visitor = new VisualVisitor();
		cunit.accept(visitor);
		return false;
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
