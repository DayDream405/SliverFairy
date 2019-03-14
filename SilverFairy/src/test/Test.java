package test;

import java.lang.reflect.*;
import java.util.List;

import util.DButils;
import util.DaoUtil;

public class Test {

	public static void main(String[] args) {
		Student stu = new Student();
		stu.setUserName("LLL");
		stu.setPassword("123456");
		Address a=new Address();
		a.setCity("shandong");
		a.setPhoneNum(123456);
		stu.setAddress(a);
//		stu.setCity("shandong");
//		stu.setPhoneNum("123456");
//		DButils.getInstance().delete("users", "username", "LLL");
		System.out.println(DaoUtil.insert(stu, "users", "username"));
//		List<Student> l=DaoUtil.getList("users", "test.Student");		
//		System.out.println(l.get(0).getUserId());
//		try {
//			Class c= Class.forName("test.Address");
//			Field[] f=c.getDeclaredFields();
//			for(Field x:f) {
//				System.out.println(x.getType().getSuperclass());
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
	}

}
