package packer;

import java.lang.reflect.*;
import java.util.*;

/**
 * 
 * This class can help you to create a model class instance<br>
 * you can get the instance or opera this instance by ModelCreator<br>
 * One creator instance can hold one model class instance
 * 
 * @warning this class aim to create a model class<br>so don't try any class else
 * 
 * @author DayDream
 * @version SF v1.0
 * 
 * **/
public class ModelCreator {
	
	/**
	 * the new instance which is created by constructor<br>
	 * this object is private,you can get it by {@link #getModel()}
	 * **/
	private Object obj;//生成的模型类
	
	/**
	 * the full class name of the instance<br>
	 * it same as {}{@code obj.class.getName()}<br>
	 * call {@link #getClassName()} to get it
	 * **/
	private String className;//模型类名
	
	/**
	 * the array of the methods
	 * **/
	private Method[] m;//模型类方法
	
	/**
	 * the array of the fields
	 * **/
	private Field[] f;//模型类成员
	
	/**
	 * the primitive type fields list<br>
	 * it include the 8 basic types and them packing class<br>
	 * but any field in java package will also be included this list
	 * **/
	private List<Field> primitiveTypeFields;
	
	/**
	 * not primitive fields list<br>
	 * the fields which are excluded {@link #primitiveTypeFields}
	 * **/
	private List<Field> unprimitiveTypeFields;//非原生类成员
	
	/**
	 * the name index of all the declared fields<br>
	 * <p>This includes public, protected, default(package) access, 
	 * and private fields, but excludes inherited fields.</p>
	 * The field's name has one primitive version and one lower-case version<br>
	 * The two names target the same field
	 * **/ 
	private HashMap<String,Field> fBook;//模型类成员索引
	
	/**
	 * the name index of all the declared methods<br>
	 * <p>This includes public, protected, default(package) access, 
	 * and private methods, but excludes inherited methods.</p>
	 * The method's name has one primitive version and one lower-case version<br>
	 * The two names target the same method
	 * **/
	private HashMap<String,Method> mBook;//模型类方法索引
	
	/**
	 * A map of the primitive type name to packaging class name<br>
	 * This map not allow to alert<br>
	 * It initializes in the static block 
	 * **/
	private final static Map<String,String> packagingClassIndex=new HashMap<>();
	static {
		ModelCreator.packagingClassIndex.put("int", "java.lang.Integer");
		ModelCreator.packagingClassIndex.put("double", "java.lang.Double");
		ModelCreator.packagingClassIndex.put("float", "java.lang.Float");
		ModelCreator.packagingClassIndex.put("long", "java.lang.Long");
		ModelCreator.packagingClassIndex.put("byte", "java.lang.Byte");
		ModelCreator.packagingClassIndex.put("short", "java.lang.Short");
		ModelCreator.packagingClassIndex.put("char", "java.lang.Character");
	}
	
	/**
	 * This constructor need a full class name and create a new instance by this name<br>
	 * This constructor can also finish some initializations about this class's field
	 * 
	 * 
	 * **/
	public ModelCreator(String className) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Class<?> c=Class.forName(className);
		this.className=className;
		Constructor<?> con=c.getConstructor();
		obj=con.newInstance();
		f=c.getDeclaredFields();
		m=c.getDeclaredMethods();
		unprimitiveTypeFields=new ArrayList<>();
		primitiveTypeFields=new ArrayList<>();
		for(Field x:f) {
			String[] s=x.getType().getName().split("\\.");
			if(!s[0].equals("java")&&!ModelCreator.packagingClassIndex.containsKey(s[0])) {
				unprimitiveTypeFields.add(x);
			}else {
				primitiveTypeFields.add(x);
			}
		}
		produceBooks();
	}
	
	/**
	 * Almost same as the {@link #ModelCreator(String)}<br>
	 * But you can create the instance by yourself
	 * **/
	public ModelCreator(Object obj) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		this(obj.getClass().getName());
		this.obj=obj;
	}
	
	/**
	 * get the {@link #obj}
	 * 
	 * @return the instance of a {code Object} class
	 * **/
	public Object getModel() {
		return obj;
	}
	
	/**
	 * get the {@link #className}
	 * 
	 * @return The full {@code String} name of class
	 * **/
	public String getClassName() {
		return className;
	}
	
	/**
	 * The {@link #fBook} and {@link #mBook} initialization method<br>
	 * Called by {@link #ModelCreator(String)}
	 * 
	 * @note the {@code hashMap} keys have a primitive version and a lower case version
	 * **/
	private void produceBooks() {
		if(this.mBook==null)
		this.mBook=new HashMap<>();
		if(this.fBook==null)
		this.fBook=new HashMap<>();
		for(Field x:f) {
			fBook.put(x.getName(), x);
			fBook.put(x.getName().toLowerCase(), x);
		}
		for(Method x:m) {
			mBook.put(x.getName(), x);
			mBook.put(x.getName().toLowerCase(), x);
		}
	}
	/**
	 * @return the target field instance or null
	 * **/
	public Field getFieldByName(String name) {
		return fBook.containsKey(name)? fBook.get(name):null;
	}
	/**
	 * @return the target method instance or null
	 * **/
	public Method getMethodByName(String name) {
		return mBook.containsKey(name)? mBook.get(name):null;
	}
	
	/**
	 * 
	 * use the {@code java.reflect} to invoke the setter<br>
	 * the {@code FieldName} will become the MethodName.Then call the {@link #invoke(String, Object...)}
	 * 
	 * @param FieldName the name of field.Nomattar about the case,it will become the method name
	 * @param para the parameter of the target method,also can be null
	 * 
	 * @throws throws many many exceptions they will be integrated future
	 * **/
	public void invokeSet(String FieldName,Object para) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, SecurityException {
//		Type t1=para.getClass().getComponentType();
//		Type t2=fBook.get(FieldName).getType();
//		if(t1.getTypeName()!=t2.getTypeName()) {
////			throw new Exception();
//		}
		FieldName=FieldName.toLowerCase();
		FieldName=fBook.get(FieldName).getName();
		char c=FieldName.charAt(0);
		c=Character.toUpperCase(c);
		String methodName="set"+c+FieldName.substring(1);
		invoke(methodName,stringParseNumber(para,mBook.get(methodName).getParameterTypes()[0]));
	}
	
	/**
	 * 
	 * use the {@code java.reflect} to invoke the target getter<br>
	 * 
	 * @param FieldName name of field.It is the target to find the getter method to invoke 
	 * 
	 * @return return the Object what the invoked method has returned,it can be null
	 * 
	 * @throws throws many many exceptions they will be integrated future
	 * 
	 * **/
	public Object invokeGet(String FieldName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		FieldName=FieldName.toLowerCase();
		FieldName=fBook.get(FieldName).getName();
		char c=FieldName.charAt(0);
		c=Character.toUpperCase(c);
		String methodName="get"+c+FieldName.substring(1);
		return invoke(methodName);
	}
	
	/**
	 * 
	 * It use the {@code java.reflect} to invoke the {@code object} the underlying method which the creator is holding by the {@param methodName}
	 * 
	 * @param args the varying parameter of {@code Object}.
	 * 
	 * @return return what the target method witch is invoked returned.It can be null
	 * 
	 * @throws throws many many exceptions they will be integrated future
	 * 
	 * **/
	private Object invoke(String methodName,Object...args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return mBook.get(methodName).invoke(obj, args);
	}
	public List<String> getFieldNames() {
		List<String> l=new ArrayList<>();
		for(Field x:f) {
			l.add(x.getName());
		}
		return l;
	}
	
	/**
	 * 
	 * Check if this instance has unprimitive type fields
	 * 
	 * @return the unprimitive type fields list is null or length is 0 will return false
	 * 
	 * **/
	public boolean hasUnprimitiveTypeFields() {
		return !unprimitiveTypeFields.isEmpty();
	}
	
	/**
	 * 
	 * Get the unprimitive type fields {@code ArrayList}.It can be {@code null} or an empty list
	 * 
	 * **/
	public List<Field> getUnprimitiveTypeFieldsList(){
		return this.unprimitiveTypeFields;
	}
	
	/**
	 * 
	 * get the primitive type fields {@code ArrayList}. It can be {@code null} or an empty list
	 * 
	 * **/
	public List<Field> getPrimitiveTypeFieldsList(){
		return this.primitiveTypeFields;
	}
	
	/**
	 * 
	 * This method can allow a {@code String} parse any class extends {@code java.lang.Number}<br>
	 * 
	 * @param val This {@code Object} option can accept any type of class,but it won't do any operation if this option not extends {@code java.lang.String}
	 * @param type The target type to parse .But this method won't do any operation if this option isn't 8 basic types or their packaging classes
	 * 
	 * @return If the method didn't do any operation,it returns the {@param val}.Or return the target type value after parsing
	 * 
	 * **/
	private Object stringParseNumber(Object val,Class<?> type) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if(val==null) return val;
		if(val.getClass().getName()!="java.lang.String") {
			return val;
		}
		if(String.valueOf(val).equals("")) {
			val="0";
		}
		if(type.getSuperclass()==null) {
			type=Class.forName(ModelCreator.packagingClassIndex.get(type.getSimpleName()));
		}
		if(type.getSuperclass().getName()!="java.lang.Number") {
			return val;
		}
		
		if(type.getName()=="java.lang.Integer"||type.getName()=="int") {
			return type.getMethod("parseInt", val.getClass()).invoke(null, val);
		}
		String[] s=type.getName().split("\\.");
		Character firstLetter=s[s.length-1].charAt(0);
		firstLetter=Character.toUpperCase(firstLetter);
		String name=firstLetter+s[s.length-1].substring(1);
		Method m=type.getMethod("parse"+name, val.getClass());
		return m.invoke(null,val);
	}
}
