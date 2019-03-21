package creator;

import java.util.Random;

import definedtype.ComputeNumber;

public class RandomCreator {
	private String[] operatorIndex= {"+","-","¡Á","¡Â"};
	public ComputeNumber getRandomInteger(int leftRange,int rightRange) {
		Random r=new Random();
		int n= r.nextInt(rightRange)-leftRange;
		return ComputeNumber.valueOf(n);
	}
	public String getRandomOperator() {
		Random r=new Random();
		int i=r.nextInt(4);
		return this.operatorIndex[i];
	}
	
}
