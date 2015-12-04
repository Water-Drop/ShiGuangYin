package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.User;
import util.JDBCHelper;


public class UserDaoimpl implements UserDao{
	JDBCHelper jh = new JDBCHelper();
    public List<User> getAllUsers(){
    	Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<User> users = new ArrayList<User>();
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("SELECT email,telephone,password,type FROM model_user WHERE status!=2");
			rs = ps.executeQuery();
			while (rs.next()){
				User user = new User();
				user.setEmail(rs.getString("email"));
				user.setTelephone(rs.getString("telephone"));
				user.setPassword(rs.getString("password"));
				user.setType(rs.getInt("type"));
				users.add(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
    	return users;
    }
    public User getUserById(Integer id){
    	Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
    	User user = new User();
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("SELECT email,telephone,password,type FROM model_user WHERE id=? AND status!=2");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.first() != false){
				user.setEmail(rs.getString("email"));
				user.setTelephone(rs.getString("telephone"));
				user.setPassword(rs.getString("password"));
				user.setType(rs.getInt("type"));
			}  else {
				user.setStatus(2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
    	return user;
    }
    public Integer addUser(User user){
        Connection conn = null;
		PreparedStatement ips = null;
		PreparedStatement sps = null;
		ResultSet rs = null;
		Integer newid = -1;
		try {
			conn = jh.getConnection();
			conn.setAutoCommit(false);
			ips = conn.prepareStatement("INSERT INTO model_user (email,telephone,password,type,status) VALUES (?,?,?,?,0)");
			ips.setString(1,user.getEmail());
			ips.setString(2,user.getTelephone());
			ips.setString(3,user.getPassword());
			ips.setInt(4,user.getType());
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
    public Integer deleteUser(Integer id){
    	Connection conn = null;
		PreparedStatement ps = null;
		Integer rtn = -1;
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("UPDATE model_user SET status=2 WHERE id=?");
			ps.setInt(1, id);
			Integer num = ps.executeUpdate();
			if (num == 0){
				rtn = 1;
			} else {
				rtn = 0;
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
		return rtn;
    }
    public Integer modifyUser(User user){
    	Connection conn = null;
		PreparedStatement ps = null;
		Integer rtn = -1;
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("UPDATE model_user SET email,telephone,password,type WHERE id=?");
			
			ps.setString(1,user.getEmail());
			ps.setString(2,user.getTelephone());
			ps.setString(3,user.getPassword());
			ps.setInt(4,user.getType());
			
			ps.setInt(5,user.getId());
			Integer num = ps.executeUpdate();
			if (num == 0){
				rtn = 1;
			} else {
				rtn = 0;
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
		return rtn;
    }
}