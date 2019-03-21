package runner;

import java.math.BigDecimal;
import java.util.Date;

import creator.MathematicalProblemCreator;
import definedtype.ComputeNumber;

public class Runner {

	public static void main(String[] args) {
		MathematicalProblemCreator c=new MathematicalProblemCreator();
		ComputeNumber.setScale(3);
		for(int i=0;i<50;i++) {
			String s=c.getFourArithmeticOperationsProblem(0,100);
			System.out.println(s+" "+c.getAnswer(s));
		}
//		ComputeNumber.setScale(2);
//		float f=Float.parseFloat("0.99");
//		System.out.println(f);
//		System.out.println(f+10);
//		System.out.println(f+10-9);
//		double d=0.9;
//		System.out.println(d+10-9);
//		ComputeNumber c=ComputeNumber.valueOf(10.9845);
//		System.out.println(c);
//		BigDecimal b=new BigDecimal("10.4556");
//		b=b.setScale(3, BigDecimal.ROUND_HALF_UP);
//		System.out.println(b);
		
	}

}
