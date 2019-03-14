package packer;

import java.lang.reflect.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ModelPacker {
	public Object getModel(String className,ResultSet re) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
		ModelCreator m=new ModelCreator(className);
		if(m.hasUnprimitiveTypeFields()) {
			List<Field> unprimitiveList=m.getUnprimitiveTypeFieldsList();
			for(Field x:unprimitiveList) {
				Object obj=getModel(x.getType().getName(),re);
				m.invokeSet(x.getName(), obj);
			}
		}
		List<Field> primitiveFieldList=m.getPrimitiveTypeFieldsList();
		for(Field x:primitiveFieldList) {
			m.invokeSet(x.getName(), re.getString(x.getName()));//模型类成员名与列名相同
		}
		return m.getModel();
	}
}
