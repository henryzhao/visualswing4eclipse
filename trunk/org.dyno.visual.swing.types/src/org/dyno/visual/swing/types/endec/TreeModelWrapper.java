
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

package org.dyno.visual.swing.types.endec;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.TypeAdapter;
import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

/**
 * 
 * @author William Chen
 */
public class TreeModelWrapper implements ICodeGen {
	@Override
	public String getJavaCode(Object value, ImportRewrite imports) {
		if (value == null)
			return "null";
		return "treeModel";
	}

	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		if (value == null)
			return null;
		StringBuilder builder = new StringBuilder();
		String str = imports.addImport("javax.swing.tree.DefaultTreeModel");
		builder.append(str + " treeModel = null;\n");
		TreeNode node = (TreeNode) ((TreeModel) value).getRoot();
		genCode(node, builder, 0, imports);
		return builder.toString();
	}

	@SuppressWarnings("unchecked")
	private void genCode(TreeNode node, StringBuilder builder, int depth, ImportRewrite imports) {
		builder.append("{\n");
		DefaultMutableTreeNode mtn = (DefaultMutableTreeNode) node;
		Object object = mtn.getUserObject();
		String str = imports.addImport("javax.swing.tree.DefaultMutableTreeNode");
		Class type = object.getClass();
		TypeAdapter adapter = ExtensionRegistry.getTypeAdapter(type);
		builder.append(str + " node" + depth + " = new " + str + "(");
		if(adapter!=null&&adapter.getEndec()!=null){
			String encodedCode = adapter.getEndec().getJavaCode(object, imports);
			builder.append(encodedCode);
		}else{
			builder.append("\"");
			builder.append(object);
			builder.append("\"");
		}
		builder.append(");\n");
		if (!node.isLeaf()) {
			int count = node.getChildCount();
			for (int i = 0; i < count; i++) {
				TreeNode childNode = node.getChildAt(i);
				genCode(childNode, builder, depth + 1, imports);
			}
		}
		if (depth == 0) {
			String dtm = imports.addImport("javax.swing.tree.DefaultTreeModel");
			builder.append("treeModel = new " + dtm + "(node" + depth + ");\n");
		} else {
			builder.append("node" + (depth - 1) + ".add(node" + depth + ");\n");
		}
		builder.append("}\n");
	}
}

