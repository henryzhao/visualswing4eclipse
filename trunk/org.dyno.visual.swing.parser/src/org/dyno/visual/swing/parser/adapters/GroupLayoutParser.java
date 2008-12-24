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
package org.dyno.visual.swing.parser.adapters;

import java.awt.Component;

import org.dyno.visual.swing.layouts.Alignment;
import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Trailing;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;

public class GroupLayoutParser extends LayoutParser {


	@Override
	protected String getNewInstanceCode(ImportRewrite imports) {
		String name = imports
				.addImport("org.dyno.visual.swing.layouts.GroupLayout");
		return "new " + name + "()";
	}

	@Override
	protected String getChildConstraints(Component child, ImportRewrite imports) {
		GroupLayout layout = (GroupLayout) layoutAdapter.getContainer().getLayout();
		Constraints constraints = layout.getConstraints(child);
		StringBuilder builder = new StringBuilder();
		String strConstraints = imports
				.addImport("org.dyno.visual.swing.layouts.Constraints");
		builder.append("new " + strConstraints + "(");
		genAlignmentCode(builder, constraints.getHorizontal(), imports);
		builder.append(", ");
		genAlignmentCode(builder, constraints.getVertical(), imports);
		builder.append(")");
		return builder.toString();
	}

	private void genBilateralCode(StringBuilder builder, Bilateral bilateral,
			ImportRewrite imports) {
		String strBilateral = imports
				.addImport("org.dyno.visual.swing.layouts.Bilateral");
		builder.append("new " + strBilateral + "(");
		builder.append(bilateral.getLeading() + ", ");
		builder.append(bilateral.getTrailing() + ", ");
		builder.append(bilateral.getSpring().getMinimum());
		int pref = bilateral.getSpring().getPreferred();
		if (pref != Alignment.PREFERRED) {
			builder.append(", ");
			builder.append(pref);
		}
		builder.append(")");
	}

	private void genTrailingCode(StringBuilder builder, Trailing trailing,
			ImportRewrite imports) {
		String strTrailing = imports
				.addImport("org.dyno.visual.swing.layouts.Trailing");
		builder.append("new " + strTrailing + "(");
		builder.append(trailing.getTrailing() + ", ");
		int size = trailing.getSize();
		if (size != Alignment.PREFERRED) {
			builder.append(size);
			builder.append(", ");
		}
		builder.append(trailing.getSpring().getMinimum() + ", ");
		builder.append(trailing.getSpring().getPreferred());
		builder.append(")");
	}

	private void genLeadingCode(StringBuilder builder, Leading leading,
			ImportRewrite imports) {
		String strLeading = imports
				.addImport("org.dyno.visual.swing.layouts.Leading");
		builder.append("new " + strLeading + "(");
		builder.append(leading.getLeading() + ", ");
		int size = leading.getSize();
		if (size != Alignment.PREFERRED) {
			builder.append(size);
			builder.append(", ");
		}
		builder.append(leading.getSpring().getMinimum() + ", ");
		builder.append(leading.getSpring().getPreferred());
		builder.append(")");
	}


	private void genAlignmentCode(StringBuilder builder, Alignment alignment,
			ImportRewrite imports) {
		if (alignment instanceof Leading) {
			genLeadingCode(builder, (Leading) alignment, imports);
		} else if (alignment instanceof Trailing) {
			genTrailingCode(builder, (Trailing) alignment, imports);
		} else if (alignment instanceof Bilateral) {
			genBilateralCode(builder, (Bilateral) alignment, imports);
		}
	}	
}
