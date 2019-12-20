package com.kratos.game.herphone.tencent.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import com.kratos.game.herphone.common.BaseService; 
import com.kratos.game.herphone.tencent.bean.Item;
/**
 * 临时类 ，用于修改老的qq unionid 相关操作
 * @author Administrator
 *
 */
@Deprecated
public class JdbcTemp extends BaseService {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  
	//正式服参数
	static final String DB_URL ="jdbc:mysql://rm-m5e8930io3917v3nk5o.mysql.rds.aliyuncs.com:3306/herphone?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&characterEncoding=UTF-8";
	static final String USER = "crxl2018";
	static final String PASS = "ASDasd19981018"; 
	
//	static final String DB_URL ="jdbc:mysql://localhost:3306/herphone_test?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&characterEncoding=UTF-8";
//	static final String USER = "root";
//	static final String PASS = "123456"; 
	
	
	//检查 腾讯 老的腾讯openid的检查 
	public static ArrayList<Item> findOpenidAndPlayerId() {		
		ArrayList<Item> list=new ArrayList<>();	 
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			String sql;
			sql = "SELECT id,tencent_openid FROM player WHERE tencent_openid IS NOT NULL";
			stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				long playerId = rs.getLong("id");
				String openid = rs.getString("tencent_openid"); 
				list.add(new Item(openid, "", playerId));
			}
			rs.close();
			stmt.close();

			// 开始更新
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return list;

	}

}
