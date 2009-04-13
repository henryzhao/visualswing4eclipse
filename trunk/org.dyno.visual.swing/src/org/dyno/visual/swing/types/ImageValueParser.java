package org.dyno.visual.swing.types;

import java.awt.Image;
import java.util.List;

import org.dyno.visual.swing.base.ResourceImage;
import org.dyno.visual.swing.plugin.spi.IValueParser;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.StringLiteral;

@SuppressWarnings("unchecked")
public class ImageValueParser implements IValueParser {

	
	public Object parseValue(Object oldValue, List args) {
		if (oldValue != null && !(oldValue instanceof ResourceImage)) {
			Image icon = (Image)oldValue;
			Expression arg = (Expression) args.get(0);
			if (arg instanceof MethodInvocation) {
				MethodInvocation mi = (MethodInvocation) arg;
				args = mi.arguments();
				arg = (Expression) args.get(0);
				if (arg instanceof MethodInvocation) {
					mi = (MethodInvocation) arg;
					args = mi.arguments();
					arg = (Expression) args.get(0);
					if (arg instanceof StringLiteral) {
						StringLiteral sl = (StringLiteral) arg;
						String path = sl.getLiteralValue();
						return new ResourceImage(icon, path);
					}
				}
			}
		}
		return oldValue;
	}

}
