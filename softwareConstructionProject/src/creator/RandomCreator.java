package creator;

import java.util.Random;

public class RandomCreator {
	private String[] operatorIndex= {"+","-","¡Á","¡Â"};
	public int getRandomInteger(int leftRange,int rightRange) {
		Random r=new Random();
		return r.nextInt(rightRange)-leftRange;
	}
	public String getRandomOperator() {
		Random r=new Random();
		int i=r.nextInt(4);
		return this.operatorIndex[i];
	}
	
}
