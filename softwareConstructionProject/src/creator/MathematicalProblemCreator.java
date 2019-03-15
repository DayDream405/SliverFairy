package creator;

import java.util.*;

public class MathematicalProblemCreator extends RandomCreator{
	private List<String> problemBook=new ArrayList<>();
	private Map<String,Number> referenceAnswer=new HashMap<>(); 
	public String getMathmaticalProblem(int leftRange,int rightRange) {
		int a,b;
		Number result;
		String problemExpression="",operator;
		do {
			 a=this.getRandomInteger(leftRange,rightRange);
			 b=this.getRandomInteger(leftRange,rightRange);
			 operator=this.getRandomOperator();
			 result=doOperate(a,b,operator);
		}while(result.floatValue()>=rightRange||result.floatValue()<=leftRange);
		problemExpression=a+" "+operator+" "+b+" "+"=";
		if(checkExistence(problemExpression)) {
			problemExpression=getMathmaticalProblem(leftRange,rightRange);
		}
		problemBook.add(problemExpression);
		referenceAnswer.put(problemExpression, result);
		return problemExpression;
	}
	private Number doOperate(int a,int b,String o) {
		if(o.equals("+")) {
			return a+b;
		}else if(o.equals("-")) {
			return a-b;
		}else if(o.equals("¡Á")) {
			return a*b;
		}else if(o.equals("¡Â")) {
			float fa=Float.intBitsToFloat(a);
			float fb=Float.intBitsToFloat(b);
			return fb==0?Float.MAX_VALUE:fa/fb;
		}else {
			return null;
		}
	}
	public boolean checkExistence(String problemExpression) {
		String[] slice=problemExpression.split(" ");
		String sameExpression=slice[2]+" "+slice[1]+" "+slice[0]+" "+"=";
		return problemBook.contains(problemExpression)||problemBook.contains(sameExpression);
	}
	public Number getAnswer(String problemExpression) {
		return this.referenceAnswer.get(problemExpression);
	}
}
