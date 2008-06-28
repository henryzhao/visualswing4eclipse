/*
 * ComboBoxModelWrapper.java
 *
 * Created on 2007-8-28, 0:16:54
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dyno.visual.swing.types.endec;

import javax.swing.ComboBoxModel;

import org.dyno.visual.swing.plugin.spi.ExtensionRegistry;
import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.dyno.visual.swing.plugin.spi.TypeAdapter;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

/**
 * 
 * @author William Chen
 */
public class ComboBoxModelWrapper implements ICodeGen {

	public ComboBoxModelWrapper() {
	}
	@Override
	public String getJavaCode(Object value, ImportRewrite imports) {
		if(value==null)
			return "null";
		String className = imports.addImport("javax.swing.DefaultComboBoxModel");
		StringBuilder builder=new StringBuilder();
		builder.append("new "+className+"(new Object[]{\n");
		ComboBoxModel model = (ComboBoxModel)value;
		for(int i=0;i<model.getSize();i++){
			if(i!=0)
				builder.append(",");
			Object obj=model.getElementAt(i);
			if(obj==null){
				builder.append("null");
			}else{
				TypeAdapter adapter=ExtensionRegistry.getTypeAdapter(obj.getClass());
				if(adapter!=null&&adapter.getEndec()!=null){
					builder.append(adapter.getEndec().getJavaCode(obj, imports));
				}else{
					builder.append(obj.toString());
				}				
			}
		}
		builder.append("})");
		return builder.toString();
	}
	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}
}