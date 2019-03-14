package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
 
/**
 * Tool class to connect database(MySQL) by using JDBC
 * It support query, delete and update operations
 * This class is single design
 * 
 * @ThreadSafe {@link #query(String) #update(String) #close()} is synchronized<br>
 * And the {@link #lock} will make the static field synchronized
 * 
 * @author DayDream
 * @version SliverFairy v1.0
 * **/
public final class DButils {
	
	/**
	 * database user account
	 * use{@link #setUserName(String)}} to set it;
	 * @default {@code UserAccount="root"}
	 * **/
	private  static String UserAccount = "root";
	
	/**
	 * database user password
	 * use{@link #setPass(String)} to set it
	 * @default {@code PassWord="123456"}
	 * **/
	private  static String PassWord = "123456";
	
	/**
	 * URL of JDBC connection 
	 * use {@link #setURL(String, String)} to set it
	 * you can appoint database platform future
	 * 
	 * @default {@param port "3306"} {@param databaseName "book"}
	 * **/
	private  static String Url = "jdbc:mysql://localhost:3306/book?characterEncoding=utf-8";
//	 jdbc:mysql://<host>:<port>/<database_name>
//	 jdbc:sqlserver://<server_name>:<port>
	
	/**
	 * single instance
	 * **/
	private static DButils single = new DButils();
	
	/**
	 * JDBC Connection
	 * **/
	private static Connection conn = null;
	
	/**
	 * JDBC PreparedStatement
	 * **/
	private static PreparedStatement prep = null;
	
	/**
	 * JDBC ResultSet
	 * **/
	private static ResultSet re = null;
	
	/**
	 * to protect the static field
	 * 
	 * **/
	private static Lock lock = new ReentrantLock();
	
	/**
	 * single instance
	 * load the JDBC Driver
	 * print {@code "驱动加载成功"} if load succeed
	 * 
	 * @exception thorw ClassNotFoundException if not find the Driver class
	 * **/
	private DButils() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * get the single instance
	 * 
	 * @return {@link #single}
	 * **/
	public static DButils getInstance() {
		return single;
	}
	
	/**
	 * set {@link #Url}
	 * 
	 * @param port the port number database platform used like 3306
	 * @param databaseName name of the database
	 * 
	 * **/
	public void setURL(String port,String databaseName) {
		DButils.Url="jdbc:mysql://localhost:"+port+"/"+databaseName+"?characterEncoding=utf-8";
	}
	
	/**
	 * set {@link #UserAccount}
	 * 
	 * @param newAccount new user account
	 * 
	 * **/
	public void setUserAccount(String newAccount) {
		DButils.UserAccount=newAccount;
	}
	
	/**
	 * set{@link #PassWord}
	 * 
	 * @param newPass new password
	 * 
	 * **/
	public void setPass(String newPass) {
		DButils.PassWord=newPass;
	}
	
	/**
	 * connect the database use the {@link #Url #UserAccount #PassWord}
	 * auto call by {@link #query(String) #update(String)}
	 * 
	 * @exception SQLException some error during the connection
	 * 
	 * **/
	synchronized private void getConn() {
		try {
			conn = DriverManager.getConnection(Url, UserAccount, PassWord);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("链接失败");
			e.printStackTrace();
		}
	}
	
	/**
	 * Proxy the {@link #java.sql.Statement.executeQuery(String)}<br>
	 * the {@link #lock} will get locked to protect the {@link #prep #re}
	 * 
	 * @exception SQLException some error like wrong SQL expression...
	 * 
	 *  @return {@link #re}
	 * 
	 * **/
	public ResultSet query(String sql) {
		
		try {
			lock.lock();
			if(conn==null||conn.isClosed()) {
			getConn();
		}
			prep=conn.prepareStatement(sql);
			re=prep.executeQuery();
			return re;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
//			DButils.close();
			lock.unlock();
		}
		return null;
	}
	
	/**
	 * 
	 * proxy the {link #java.sql.Statement.update(String)}
	 * {@link #lock} will get locked to protect the {@link #prep #re}
	 * 
	 * @exception SQLException some error like wrong SQL expression...
	 * 
	 *  @return {@code n>0? true:false}}
	 * 
	 * **/
	public boolean update(String sql) {
		int n=0;
		try {
			if(conn==null||conn.isClosed()) {
			getConn();
		}
			lock.lock();
			prep=conn.prepareStatement(sql);
			n=prep.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
//			DButils.close();
			lock.unlock();
		}
		return n>0? true:false;
	}
	
	/**
	 * delete one piece of data from {@param tableName} by the {@param key}
	 * 
	 * @param tableName the name of table which may has the data
	 * @param key the key to find the data
	 * @param value  value of the key
	 * 
	 * @return true if delete succeed and false if delete failed or can't find the data
	 * **/
	public boolean delete(String tableName,String key,String value) {
		String sql="delete from "+tableName+" where "+key+" = '"+value+"'";
		return update(sql);
	}
	/**
	 * 
	 * close the {@link #re #prep #conn} which not equals null
	 * 
	 * @exception SQLException error when close
	 * 
	 * **/
	synchronized public static void close() {
		try {
			if(re!=null)re.close();
			if(prep!=null)prep.close();
			if(conn!=null)conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
