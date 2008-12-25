package org.dyno.visual.swing.parser;

import org.dyno.visual.swing.parser.spi.IParser;
import org.dyno.visual.swing.plugin.spi.IAdapter;
import org.dyno.visual.swing.plugin.spi.IRenamingListener;
import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;

public class DefaultRenamingListener implements IRenamingListener {
	private IType getUnitMainType(ICompilationUnit unit){
		String unit_name = unit.getElementName();
		int dot = unit_name.lastIndexOf('.');
		if (dot != -1)
			unit_name = unit_name.substring(0, dot);
		IType type = unit.getType(unit_name);
		return type;
	}	
	@Override
	public void adapterRenamed(ICompilationUnit unit, IAdapter adapter) {
		IType type = getUnitMainType(unit);
		if(adapter instanceof WidgetAdapter || adapter instanceof InvisibleAdapter){
			IParser parser=(IParser) adapter.getAdapter(IParser.class);
			if(parser!=null){				
				parser.renameField(type, null);
			}
		}
	}
}
