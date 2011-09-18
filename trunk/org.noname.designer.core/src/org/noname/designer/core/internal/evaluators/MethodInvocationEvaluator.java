package org.noname.designer.core.internal.evaluators;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.PrimitiveType.Code;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.noname.designer.core.interfaces.EvaluationContext;
import org.noname.designer.core.interfaces.FrameContext;
import org.noname.designer.core.interfaces.IEvaluator;
import org.noname.designer.core.interfaces.ITypeInferer;
import org.noname.designer.core.interfaces.VariableContext;

public class MethodInvocationEvaluator implements IEvaluator {
	private MethodInvocation invocation;

	public MethodInvocationEvaluator(MethodInvocation mi) {
		invocation = mi;
	}

	/**
	 * @return OK | THROW
	 */
	@Override
	public int evaluate(EvaluationContext context) {
		Expression expression = invocation.getExpression();
		FrameContext frame = context.peek();
		VariableContext ctx = frame.peek();
		Stack<Object> stack = ctx.getStack();
		if (expression != null && !(expression instanceof ThisExpression)) {
			IEvaluator evaluator = (IEvaluator) Platform.getAdapterManager()
					.getAdapter(expression, IEvaluator.class);
			int result = evaluator.evaluate(context);
			switch (result) {
			case THROW:
				return THROW;
			}
		} else {
			MethodDeclaration mi = getMethodDeclaration();
			if (mi != null) {
				return evaluateMethodInvocation(context, mi);
			} else {
				stack.push(context.getThisObject());
			}
		}
		int result = evaluateArguments(context);
		switch (result) {
		case THROW:
			return THROW;
		}
		List args = invocation.arguments();
		int size = args == null ? 0 : args.size();
		List<Object> arguments = new ArrayList<Object>();
		for (int i = 0; i < size; i++) {
			args.add(0, stack.pop());
		}
		Object[] params = args.toArray();
		Object obj = stack.pop();

		return OK;
	}

	private int evaluateArguments(EvaluationContext context) {
		// TODO Auto-generated method stub
		return 0;
	}

	private int evaluateMethodInvocation(EvaluationContext context,
			MethodDeclaration mi) {
		FrameContext frame = new FrameContext();
		context.push(frame);
		VariableContext arguments = new VariableContext();
		frame.push(arguments);
		IEvaluator evaluator = (IEvaluator) Platform.getAdapterManager()
				.getAdapter(mi, IEvaluator.class);
		int ret = evaluator.evaluate(context);
		frame = context.popup();
		Object result = frame.getResult();
		switch (ret) {
		case THROW:
			context.peek().setResult(result);
			return THROW;
		case RETURN:
			return OK;
		}
		frame = context.peek();
		VariableContext varCtx = frame.peek();
		Stack<Object> stack = varCtx.getStack();
		stack.addElement(result);
		return OK;
	}

	private MethodDeclaration getMethodDeclaration() {
		String name = invocation.getName().getIdentifier();
		ASTNode root = invocation.getRoot();
		CompilationUnit unit = (CompilationUnit) root;
		TypeDeclaration typeDec = (TypeDeclaration) unit.types().get(0);
		MethodDeclaration[] methods = typeDec.getMethods();
		MethodDeclaration mi = null;
con:		for (MethodDeclaration method : methods) {
			String methodName = method.getName().getIdentifier();
			if (name.equals(methodName)) {
				List<Type> mParamTypes = getMethodParamTypes(method);
				List<Type> argTypes = inferArgumentTypes();
				if(mParamTypes.size()!=argTypes.size())
					continue;
				for(int i=0;i<mParamTypes.size();i++){
					Type mType = mParamTypes.get(i);
					Type aType = argTypes.get(i);
					if(!isAssignable(mType, aType)){
						continue con;
					}
				}
				mi = method;
				break;
			}
		}
		return mi;
	}

	private boolean isAssignable(Type left, Type right) {
		if (left.isArrayType()) {
			if (right.isArrayType()) {
				int dimLeft = ((ArrayType) left).getDimensions();
				int dimRight = ((ArrayType) right).getDimensions();
				if (dimLeft == dimRight) {
					Type leftElement = ((ArrayType) left).getElementType();
					Type rightElment = ((ArrayType) right).getElementType();
					return isAssignable(leftElement, rightElment);
				}
			}
		} else if (left.isPrimitiveType()) {
			if (right.isPrimitiveType()) {
				PrimitiveType leftType = (PrimitiveType) left;
				PrimitiveType rightType = (PrimitiveType) right;
				Code leftCode = leftType.getPrimitiveTypeCode();
				Code rightCode = leftType.getPrimitiveTypeCode();
				if (leftCode.toString().equals(rightCode.toString()))
					return true;
			}
		} else if (left.isParameterizedType()) {
			if (right.isParameterizedType()) {
				ParameterizedType leftType = (ParameterizedType) left;
				ParameterizedType rightType = (ParameterizedType) right;
				Type leftBaseType = leftType.getType();
				Type rightBaseType = rightType.getType();
				boolean baseAssignable = isAssignable(leftBaseType,
						rightBaseType);
				if (baseAssignable) {
					List leftArgs = leftType.typeArguments();
					List rightArgs = rightType.typeArguments();
					if (leftArgs.size() != rightArgs.size())
						return false;
					for (int i = 0; i < leftArgs.size(); i++) {
						Type leftArgType = (Type) leftArgs.get(i);
						Type rightArgType = (Type) rightArgs.get(i);
						if (!isAssignable(leftArgType, rightArgType)) {
							return false;
						}
					}
					return true;
				}
			}
		} else if (left.isQualifiedType()) {
			if(right.isQualifiedType()){
				QualifiedType leftType = (QualifiedType) left;
				QualifiedType rightType = (QualifiedType) right;
				Type leftQualifier = leftType.getQualifier();
				Type rightQualifier = rightType.getQualifier();
				SimpleName leftName = leftType.getName();
				SimpleName rightName = rightType.getName();
				boolean assignable = isAssignable(leftQualifier, rightQualifier);
				return assignable && leftName.getIdentifier().equals(rightName.getIdentifier());
			}
		} else if(left.isSimpleType()){
			if(right.isSimpleType()){
				SimpleType leftType = (SimpleType) left;
				SimpleType rightType = (SimpleType) right;
				if(leftType.getName().equals(rightType.getName()))
					return true;
			}
		}
		return false;
	}

	private List<Type> getMethodParamTypes(MethodDeclaration method) {
		List params = method.parameters();
		List<Type> types = new ArrayList<Type>();
		for (int i = 0; i < params.size(); i++) {
			SingleVariableDeclaration svd = (SingleVariableDeclaration) params
					.get(i);
			types.add(svd.getType());
		}
		return types;
	}

	private List<Type> inferArgumentTypes() {
		List arguments = invocation.arguments();
		List<Type> types = new ArrayList<Type>();
		for (int i = 0; i < arguments.size(); i++) {
			Expression expression = (Expression) arguments.get(i);
			ITypeInferer inferer = (ITypeInferer) Platform.getAdapterManager()
					.getAdapter(expression, ITypeInferer.class);
			types.add(inferer.inferType());
		}
		return types;
	}
}
