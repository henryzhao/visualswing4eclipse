/*
 * RectangleWrapper.java
 *
 * Created on August 14, 2007, 6:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.dyno.visual.swing.types.endec;

import javax.swing.table.TableModel;

import org.dyno.visual.swing.plugin.spi.ExtensionRegistry;
import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.dyno.visual.swing.plugin.spi.TypeAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

/**
 * 
 * @author William Chen
 */
public class TableModelWrapper implements ICodeGen {
	@Override
	public String getJavaCode(Object value, ImportRewrite imports) {
		if (value == null)
			return "null";
		TableModel model = (TableModel) value;
		String str = imports.addImport("javax.swing.table.DefaultTableModel");
		StringBuilder builder = new StringBuilder();
		builder.append("new " + str + "(\n");
		builder.append("new Object[][]{\n");
		int rowcount = model.getRowCount();
		int columncount = model.getColumnCount();
		for (int i = 0; i < rowcount; i++) {
			builder.append("{");
			for (int j = 0; j < columncount; j++) {
				Object object = model.getValueAt(i, j);
				if (object == null)
					builder.append("null");
				else {
					TypeAdapter adapter = ExtensionRegistry.getTypeAdapter(object.getClass());
					if (adapter != null) {
						ICodeGen codeGen = adapter.getEndec();
						if (codeGen != null) {
							builder.append(codeGen.getJavaCode(object, imports));
						} else {
							builder.append(object.toString());
						}
					} else {
						builder.append(object.toString());
					}
				}
				builder.append(",");
			}
			builder.append("},\n");
		}
		builder.append("},\n");
		builder.append("new String [] {\n");
		for (int i = 0; i < columncount; i++) {
			builder.append("\"" + model.getColumnName(i) + "\",");
		}
		builder.append("}\n");
		builder.append("){\n");
		builder.append("private static final long serialVersionUID = 1L;\n");
		builder.append("Class<?>[] types = new Class<?> [] {\n");
		for (int i = 0; i < columncount; i++) {
			Class<?> cc = model.getColumnClass(i);
			String className = "java.lang.String";
			if (cc != null)
				className = cc.getName();
			String cn = imports.addImport(className);
			builder.append(cn + ".class,");
		}
		builder.append("};\n");
		builder.append("public Class<?> getColumnClass(int columnIndex) {\n");
		builder.append("return types [columnIndex];\n");
		builder.append("}\n");
		builder.append("}\n");
		return builder.toString();
	}

	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}
}