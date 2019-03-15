package definedtype;
//format 四则运算 整除 难度等级划分（正负、范围、整数小数、小数位数）
public class ComputeNumber implements Comparable<ComputeNumber>{
	private float value;
	private ComputeNumber(float val){
		value=val;
	}
	static ComputeNumber valueOf(int val) {
		return new ComputeNumber(val);
	}
	static ComputeNumber valueOf(float val) {
		return new ComputeNumber(val);
	}
	static int praseInt(ComputeNumber c) {
		return (int)c.value;
	}
	static float praseFloat(ComputeNumber c) {
		return c.value;
	}
	
	@Override
	public int compareTo(ComputeNumber o) {
		if(this.value>o.value) {
			return 1;
		}else if(this.value<o.value) {
			return -1;
		}else {
			return 0;
		}
	}
	
}
