package org.dyno.visual.swing.types.editor.spinnermodels;

import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.swt.widgets.Composite;

public class SpinnerNumberModelType extends SpinnerModelType {

	protected SpinnerNumberModelType() {
		super("number");
	}

	@Override
	public AccessibleUI createEditPane(Composite parent) {
		return new NumberAccessible(parent);
	}

	@Override
	public String getJavaCode(Object value, ImportRewrite imports) {
		String className = imports.addImport("javax.swing.SpinnerNumberModel");
		SpinnerNumberModel snm = (SpinnerNumberModel) value;
		Number init = snm.getNumber();
		Comparable<?> max = snm.getMaximum();
		Comparable<?> min = snm.getMinimum();
		Number step = snm.getStepSize();
		if (min == null && max == null && init instanceof Integer && step instanceof Integer) {
			return "new " + className + "()";
		}
		if (init instanceof Integer && step instanceof Integer) {
			return "new " + className + "(" + init + ", " + min + ", " + max + ", " + step + ")";
		}
		if (init instanceof Double && step instanceof Double) {
			return "new " + className + "(" + init + ", " + min + ", " + max + ", " + step + ")";
		}
		String typeName = init.getClass().getName();
		int dot = typeName.lastIndexOf('.');
		if (dot != -1)
			typeName = typeName.substring(dot + 1);
		return getSpinnerModelCode(className, typeName, init, min, max, step);
	}

	@SuppressWarnings("unchecked")
	private String getSpinnerModelCode(String className, String typeCode, Number init, Comparable min, Comparable max, Number step) {
		return "new " + className + "(" + typeCode + ".valueOf(" + init + "), " + typeCode + ".valueOf(" + min + "), " + typeCode + ".valueOf(" + max + "), "
				+ typeCode + ".valueOf(" + step + "))";
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compare(SpinnerModel o1, SpinnerModel o2) {
		SpinnerNumberModel snm1 = (SpinnerNumberModel) o1;
		SpinnerNumberModel snm2 = (SpinnerNumberModel) o2;
		Number value1 = snm1.getNumber();
		Number value2 = snm2.getNumber();
		if(!value1.equals(value2))
			return 1;
		Comparable min1 = snm1.getMinimum();
		Comparable min2 = snm2.getMinimum();
		if(!equals(min1, min2))
			return 1;
		Comparable max1 = snm1.getMaximum();
		Comparable max2 = snm2.getMaximum();
		if(!equals(max1, max2))
			return 1;
		Number step1 = snm1.getStepSize();
		Number step2 = snm2.getStepSize();
		if(!step1.equals(step2))
			return 1;
		return 0;
	}
	@SuppressWarnings("unchecked")
	private boolean equals(Comparable o1, Comparable o2){
		if(o1==null){
			if(o2==null){
				return true;
			}else{
				return false;
			}
		}else{
			if(o2==null){
				return false;
			}else{
				return o1.compareTo(o2)==0;
			}
		}
	}

	@Override
	public String getInitJavaCode(Object value, ImportRewrite imports) {
		return null;
	}
}
