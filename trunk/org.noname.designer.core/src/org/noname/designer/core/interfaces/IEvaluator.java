package org.noname.designer.core.interfaces;

public interface IEvaluator {
	int OK=0;
	int CONTINUE=1;
	int BREAK=2;
	//Return
	int RETURN=3;
	//Return with value
	int RETURN_VALUE=4;
	int THROW=5;	
	int evaluate(EvaluationContext context);
}
