package runner;

import creator.MathematicalProblemCreator;

public class Runner {

	public static void main(String[] args) {
		MathematicalProblemCreator c=new MathematicalProblemCreator();
		for(int i=0;i<50;i++) {
			String s=c.getMathmaticalProblem(0, 100);
			System.out.println(s+" "+c.getAnswer(s));
		}
	}

}
