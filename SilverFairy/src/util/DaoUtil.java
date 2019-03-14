package util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.*;
import java.sql.*;
import java.util.*;

import packer.ModelCreator;
import packer.ModelPacker;




/**
 * Utility class for data access object<br>
 * It needs JDBC and {@code util.DButils.java}}<br>
 * It support almost the 'data access object' layer operation
 * 
 * @threadSafe not thread safe
 * 
 *  
 * @author DayDream
 * @version SilverFairy v1.0
 * **/
public class DaoUtil {
	
	/**
	 * util.DButils instance
	 * **/
	private static DButils dbutil = DButils.getInstance();
	
	/**
	 * 
	 * @overRide override the default constructor augment to get a new Instance
	 * 
	 * **/
	private DaoUtil(){}//工具类 禁止实例化
	
	/**
	 * get data list from database
	 * each piece of  data will be put in  model instances({@param className}) one by one
	 * this method need JDBC to connect the database
	 * 
	 * @param tableName the name of table to get the data
	 * @param className the full name of model class. You can use
	 * {@code new Object().getClass().getName}(like {@code new Student().getClass().getName})
	 * to get the {@param className}
	 * 
	 *  @return a list<?> with packaged model instances or an empty list if not get any data from database
	 *  
	 *  @exception not complete
	 * **/
	@SuppressWarnings({ "unchecked" })
	public static <T> List<T> getList(String tableName,String className) {//获取模型类列表
		String sql="select * from "+tableName;
		ResultSet re=dbutil.query(sql);
		List<T> l = new ArrayList<>();
		try {
			while(re.next()) {
				l.add((T) new ModelPacker().getModel(className, re));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DButils.close();
		}
		return l;
	}
	//获取模型类
	/**
	 * get a target data from database named {@param tableName}
	 * you can set a key and value to lock the data
	 * 
	 * @param className the full name of model class. You can use
	 * {@code new Object().getClass().getName}(like {@code new Student().getClass().getName})
	 * to get the {@param className}
	 * @param tableName the name of table which may have this target data
	 * @param key lock the target data
	 * @param value value of {@param key}
	 * 
	 * @return a model class instance if query successfully or null
	 * **/
	public static Object getInstanceByName(String className,String tableName,String key,String value) {
		String sql="select * from "+tableName+" where "+key+" = '"+value+"'";
		ResultSet re=dbutil.query(sql);
		try {
			re.next();
			return new ModelPacker().getModel(className, re);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DButils.close();
		}
		return null;
	}
	//查找是否存在该数据
	/**
	 * Query for the existence of one target data 
	 * 
	 * @param key the key to lock the target data
	 * @param value value of the {@param key}
	 * 
	 * @return return true if find the target data else return false
	 * **/
	public static boolean hasRow(String tableName,String key,String value) {
		String sql="select * from "+tableName+" where "+key+" = '"+value+"'";
		try {
			return dbutil.query(sql).next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DButils.close();
		}
		return false;
	}
	
	/**
	 * insert the data to the target table<br>
	 * <p>
	 * First you should put the data into a model class and point a primaryKey to find if this data
	 * has existed
	 * </p>
	 * 
	 * @warning your data will be overwrote if it has already existed
	 * 
	 * @param obj the model class instance with the new data
	 * @param tableName name of table
	 * @param primaryKey this method will use this key to check if the data existed
	 * 
	 * @return {@code true}} if any target table column has been effected
	 * **/
	public static boolean insert(Object obj,String tableName,String primaryKey) {
		try {
			ModelCreator mc = new ModelCreator(obj);
			String value = String.valueOf(mc.invokeGet(mc.getFieldByName(primaryKey.toLowerCase()).getName()));
			String sql=sqlInsertExpression(tableName,mc);
			if(hasRow(tableName,primaryKey,value)) {
				if(!dbutil.delete(tableName, primaryKey, value)) {
					
				}
				
			}
				return dbutil.update(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DButils.close();
		}
		return false;
	}
	
	/**
	 * this method is called by {@link #insert(Object, String, String)}}<br>
	 * It can produce a completely mySQL insert expression like <br>
	 * {@code insert into tableName (`col1`,`col2`) values('val1','val2')}}
	 * It has a sub-function {@link #setColumnsAndColValues(ModelCreator)} to recursive<br>
	 * handle the fields.<br>
	 * The fields include the not-primitive type fields and the others witch have getter and setter
	 * methods
	 * 
	 * @return the full mySQL expression by {@code String}
	 * **/
	private static String sqlInsertExpression(String tableName,ModelCreator mc) {
		String sql="insert into "+tableName+" ";
		String cols="(";
		String colVals="(";
		List<String> l;
		try {
			l = setColumnsAndColValues(mc);
			String subCols=l.get(0);
			String subColVals=l.get(1);
			cols+=subCols;
			colVals+=subColVals;
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
		
		cols+=")";
		colVals+=")";
		sql=sql+cols+" values"+colVals;
		return sql;
	}
	
	/**
	 * sub-function of {@link #sqlInsertExpression(String, ModelCreator)}<br>
	 * It can get data from model class's fields and their names <br>
	 * To put them in two subStrings<br>
	 * The two subStrings will be made up the full mySQL expression
	 * 
	 * @return An {@code ArrayList<String>} with two subStrings.First is the column names and second
	 * is the values
	 * **/
	private static List<String> setColumnsAndColValues(ModelCreator mc) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, InstantiationException{
		String cols="";
		String colVals="";
		List<Field> unprimitiveList=mc.getUnprimitiveTypeFieldsList();
		List<Field>primitiveList=mc.getPrimitiveTypeFieldsList();
		for(int i=0;i<primitiveList.size();i++) {
			String fieldName=primitiveList.get(i).getName();
			if(mc.getMethodByName("get"+fieldName.toLowerCase())==null) {
				continue;
			}
			if(i!=0) {
				cols+=",";
				colVals+=",";
			}
			cols+="`"+fieldName+"`";
			Object o=mc.invokeGet(fieldName);
			colVals+="'"+String.valueOf(o)+"'";
		}
		if(!unprimitiveList.isEmpty()) {
			for(Field x:unprimitiveList) {
				Object obj=mc.invokeGet(x.getName());
				List<String> l;
				if(obj==null) {
					l=setColumnsAndColValues(new ModelCreator(x.getType().getName()));
				}else {
					l=setColumnsAndColValues(new ModelCreator(obj));
				}
				String subCols=l.get(0);
				String subColVals=l.get(1);
				cols+=","+subCols;
				colVals+=","+subColVals;
			}
		}
		List<String> result=new ArrayList<>();
		result.add(cols);
		result.add(colVals);
		return result;
	}
}
