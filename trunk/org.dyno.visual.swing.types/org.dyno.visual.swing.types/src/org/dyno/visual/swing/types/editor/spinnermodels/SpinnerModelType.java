/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/

package org.dyno.visual.swing.types.editor.spinnermodels;

import java.util.Comparator;

import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import org.dyno.visual.swing.plugin.spi.ICloner;
import org.dyno.visual.swing.plugin.spi.ICodeGen;
import org.eclipse.swt.widgets.Composite;

public abstract class SpinnerModelType implements Comparator<SpinnerModel>, ICodeGen, ICloner{
	private static SpinnerModelType[] model_types={
		new SpinnerNumberModelType(),new SpinnerDateModelType(),new SpinnerListModelType()
	};
	public static SpinnerModelType[] getModelTypes(){
		return model_types;
	}
	protected String name;
	protected SpinnerModelType(String name){
		this.name = name;
	}
	public String toString(){
		return name;
	}
	public static SpinnerModelType getSpinnerModelType(Object value){
		if(value==null)
			return null;
		if(value instanceof SpinnerNumberModel)
			return model_types[0];
		if(value instanceof SpinnerDateModel)
			return model_types[1];
		if(value instanceof SpinnerListModel)
			return model_types[2];
		return null;
	}
	public abstract AccessibleUI createEditPane(Composite parent);
}
