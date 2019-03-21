package creator;

import java.util.*;

import definedtype.ComputeNumber;

public class MathematicalProblemCreator extends RandomCreator{
	private List<String> problemBook=new ArrayList<>();
	private Map<String,ComputeNumber> referenceAnswer=new HashMap<>(); 
	public String getFourArithmeticOperationsProblem(int leftRange,int rightRange) {
		ComputeNumber leftR=ComputeNumber.valueOf(leftRange);
		ComputeNumber rightR=ComputeNumber.valueOf(rightRange);
		ComputeNumber a,b;
		ComputeNumber result;
		String problemExpression="",operator;
		do {
			 a=this.getRandomInteger(leftRange,rightRange);
			 b=this.getRandomInteger(leftRange,rightRange);
			 operator=this.getRandomOperator();
			 result=doOperate(a,b,operator);
		}while(result.compareTo(rightR)>0||result.compareTo(leftR)<0);
		problemExpression=a+" "+operator+" "+b+" "+"=";
		if(checkExistence(problemExpression)) {
			problemExpression=getFourArithmeticOperationsProblem(leftRange,rightRange);
		}
		problemBook.add(problemExpression);
		referenceAnswer.put(problemExpression, result);
		return problemExpression;
	}
	public String getPolynomialOperationProblem(int leftRange,int rightRange) {
		String problemExpression="";
		
		return problemExpression;
	}
	private ComputeNumber doOperate(ComputeNumber a,ComputeNumber b,String o) {
		if(o.equals("+")) {
			return a.add(b);
		}else if(o.equals("-")) {
			return a.subtract(b);
		}else if(o.equals("¡Á")) {
			return a.multiply(b);
		}else if(o.equals("¡Â")&&!b.equals(ComputeNumber.valueOf(0))) {
			return a.divide(b);
		}else {
			return null;
		}
	}
	public boolean checkExistence(String problemExpression) {
		String[] slice=problemExpression.split(" ");
		String sameExpression=slice[2]+" "+slice[1]+" "+slice[0]+" "+"=";
		return problemBook.contains(problemExpression)||problemBook.contains(sameExpression);
	}
	public ComputeNumber getAnswer(String problemExpression) {
		return this.referenceAnswer.get(problemExpression);
	}
	public Map<String,ComputeNumber> getProblemAndAnswer(){
		return this.referenceAnswer;
	}
}
