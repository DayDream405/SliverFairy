package creator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

//import definedtype.ComputeNumber;
import model.MathematicalProblem;
import model.Report;

public class ReportCreator {
	public static Report getReport(List<MathematicalProblem> l,String user) {
		int count=0,rightcount=0;;
		for(MathematicalProblem x:l) {
			if(!x.getUser().equals(user)) continue;
			count++;
			if(x.isRight()) rightcount++;
		}
		float rate=(float)rightcount/(float)count;
		Report re=new Report();
		re.setUserAccount(user);
		re.setProblemCount(count);
		re.setRate(rate);
		Date d=new Date();
		SimpleDateFormat s=new SimpleDateFormat("yyyy.MM.dd  hh.mm");
		re.setDate(s.format(d));
		return null;
	}
	public static String reportOutput(Report report) {
		String re="";
		Class<? extends Report> c=report.getClass();
		Field[] f=c.getDeclaredFields();
		for(Field x:f) {
			try {
				String fn=x.getName();
				String up=fn.substring(0, 1).toUpperCase();
				fn=up+fn.substring(1);
				Method m=c.getDeclaredMethod("get"+fn);
				Object o=m.invoke(report);
				re+=fn+" :"+o.toString()+"\\n";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return re;
	}
}
