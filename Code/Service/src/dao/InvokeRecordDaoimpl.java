package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.JDBCHelper;

public class InvokeRecordDaoimpl implements InvokeRecordDao {
	JDBCHelper jh = new JDBCHelper();
	public Integer addInvokeRecord(Integer uid, String ip, String cname, String mname, String returnContent, String invokeTime){
		Connection conn = null;
		PreparedStatement ips = null;
		PreparedStatement sps = null;
		ResultSet rs = null;
		Integer newid = -1;
		try {
			conn = jh.getConnection();
			conn.setAutoCommit(false);
			ips = conn.prepareStatement("INSERT INTO tb_invokerecord (uid,ip,cname,mname,returnContent,invokeTime) VALUES (?,?,?,?,?,?)");
			ips.setInt(1,uid);
			ips.setString(2,ip);
			ips.setString(3,cname);
			ips.setString(4,mname);
			ips.setString(5,returnContent);
			ips.setString(6, invokeTime);
			sps = conn.prepareStatement("SELECT LAST_INSERT_ID()");
			ips.execute();
			rs = sps.executeQuery();
			conn.commit();
			conn.setAutoCommit(true);
			rs.last();
			Integer count = rs.getRow();
			if (count != 0){
				rs.first();
				newid = Integer.parseInt(rs.getBigDecimal(1).toString());
				}
			} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
		return newid;
	}

}
