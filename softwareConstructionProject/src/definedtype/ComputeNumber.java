package definedtype;

//format 四则运算 整除 难度等级划分（正负、范围、整数小数、小数位数）
public class ComputeNumber implements Comparable<ComputeNumber>{
	private float value;
	private static int scale=1;
	private static boolean positive=false;
	private static float maxRange=Float.POSITIVE_INFINITY;
	private static float minRange=Float.NEGATIVE_INFINITY;
	private ComputeNumber(float val){
		if(positive) {
			val=Math.abs(val);
		}
		if(val>maxRange) val=maxRange;
		if(val<minRange) val=minRange;
		value=val;
		format();
	}
	public static ComputeNumber valueOf(int val) {
		return new ComputeNumber(val);
	}
	public static ComputeNumber valueOf(float val) {
		return new ComputeNumber(val);
	}
	public static ComputeNumber valueOf(double val) {
		return new ComputeNumber((float)val);
	}
	public static int praseInt(ComputeNumber c) {
		return (int)c.value;
	}
	public static float praseFloat(ComputeNumber c) {
		return c.value;
	}
	public static double praseDouble(ComputeNumber c) {
		return (double)c.value;
	}
	public static void setScale(int f) {
		if(f>7) {
			
		}
		scale=f;
	}
	private void format() {
		String strval=String.valueOf(this.value);
		String[] sliceDecimal=strval.split("\\.");
		if(sliceDecimal.length!=2) return ;
		String decimal=sliceDecimal[1];
		int decimalBit=decimal.length();
		int d=Integer.parseInt(decimal);
		if(decimalBit<=scale) return ;
		for(int i=decimalBit-1;i>=scale;i--) {
			int q=d/10;
			int r=d-((q<<3)+(q<<1));//r=d-q*10;
			if(r>=5) d+=10;
			d/=10;
		}
		//整数小数拼接
		double dd=(double)d/Math.pow(10, scale);
		this.value=Float.parseFloat(sliceDecimal[0])+(float)dd;
	}
	public ComputeNumber add(ComputeNumber c) {
		float temp=this.value+c.value;
		return new ComputeNumber(temp);
	}
	public ComputeNumber subtract(ComputeNumber c) {
		double t=Math.pow(10, ComputeNumber.scale);
		int a=(int)(this.value*t);
		int b=(int)(c.value*t);
		int re=a-b;
		float fre=(float)((float)re/t);
		return new ComputeNumber(fre);
	}
	public ComputeNumber multiply(ComputeNumber c) {
		float temp=this.value*c.value;
		return new ComputeNumber(temp);
	}
	public ComputeNumber divide(ComputeNumber c) {
		if(c.value==0) {
			if(this.value==0)
				throw new ArithmeticException("Division undefined");// 0/0
			throw new ArithmeticException("Division by zero");// x/0
		}
		float temp=this.value/c.value;
		return new ComputeNumber(temp);
	}
	
	public static boolean isPositive() {
		return positive;
	}
	public static void setPositive(boolean positive) {
		ComputeNumber.positive = positive;
	}
	
	public static float getMaxRange() {
		return maxRange;
	}
	public static void setMaxRange(float maxRange) {
		ComputeNumber.maxRange = maxRange;
	}
	public static float getMinRange() {
		return minRange;
	}
	public static void setMinRange(float minRange) {
		ComputeNumber.minRange = minRange;
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
	@Override
	public String toString() {
		int vp=(int)this.value;
		if(vp==value) 
			return String.valueOf(vp);
		return String.valueOf(this.value);
	}
	@Override
	public boolean equals(Object o) {
		if(o instanceof ComputeNumber) {
			ComputeNumber c=(ComputeNumber)o;
			if(this.value==c.value) return true;
		}
		if(o instanceof Number) {
			Float f=(Float)o;
			return f.equals(this.value);
		}
		return false;
	}
}
