package dao;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.JDBCHelper;
import model.AuthToken;


public class AuthTokenDaoimpl implements AuthTokenDao{
	JDBCHelper jh = new JDBCHelper();
    public List<AuthToken> getAllAuthTokens(){
    	Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AuthToken> authTokens = new ArrayList<AuthToken>();
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("SELECT uid,authTime,ip,agent,token FROM tb_authtoken WHERE status!=2");
			rs = ps.executeQuery();
			while (rs.next()){
				AuthToken authToken = new AuthToken();
				authToken.setUid(rs.getInt("uid"));
				authToken.setAuthTime(rs.getString("authTime"));
				authToken.setIp(rs.getString("ip"));
				authToken.setAgent(rs.getString("agent"));
				authToken.setToken(rs.getString("token"));
				authTokens.add(authToken);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
    	return authTokens;
    }
    public AuthToken getAuthTokenById(Integer id){
    	Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
    	AuthToken authToken = new AuthToken();
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("SELECT uid,authTime,ip,agent,token FROM tb_authtoken WHERE id=? AND status!=2");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.first() != false){
				authToken.setUid(rs.getInt("uid"));
				authToken.setAuthTime(rs.getString("authTime"));
				authToken.setIp(rs.getString("ip"));
				authToken.setAgent(rs.getString("agent"));
				authToken.setToken(rs.getString("token"));
			}  else {
				authToken.setStatus(2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
    	return authToken;
    }
    public Integer addAuthToken(AuthToken authToken){
        Connection conn = null;
		PreparedStatement ips = null;
		PreparedStatement sps = null;
		ResultSet rs = null;
		Integer newid = -1;
		try {
			conn = jh.getConnection();
			conn.setAutoCommit(false);
			ips = conn.prepareStatement("INSERT INTO tb_authtoken (uid,authTime,ip,agent,token,status) VALUES (?,?,?,?,?,0)");
			ips.setInt(1,authToken.getUid());
			ips.setString(2,authToken.getAuthTime());
			ips.setString(3,authToken.getIp());
			ips.setString(4,authToken.getAgent());
			ips.setString(5,authToken.getToken());
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
    public Integer deleteAuthToken(Integer id){
    	Connection conn = null;
		PreparedStatement ps = null;
		Integer rtn = -1;
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("UPDATE tb_authtoken SET status=2 WHERE id=?");
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
    public Integer modifyAuthToken(AuthToken authToken){
    	Connection conn = null;
		PreparedStatement ps = null;
		Integer rtn = -1;
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("UPDATE tb_authtoken SET uid,authTime,ip,agent,token WHERE id=?");
			ps.setInt(1,authToken.getUid());
			ps.setString(2,authToken.getAuthTime());
			ps.setString(3,authToken.getIp());
			ps.setString(4,authToken.getAgent());
			ps.setString(5,authToken.getToken());
			ps.setInt(6,authToken.getId());
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
    public String getTokenByIpAgentAndUserId(String ip, String agent, Integer userId){
    	Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String token = "";
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("SELECT token FROM tb_authtoken WHERE ip=? AND agent=? AND uid=? AND status!=2");
			ps.setString(1, ip);
			ps.setString(2, agent);
			ps.setInt(3, userId);
			rs = ps.executeQuery();
			if (rs.first() != false){
				token = rs.getString("token");
			}  
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
    	return token;
    }
    public Integer deleteAuthTokensByIpAgentAndUserId(String ip, String agent, Integer userId){
    	Connection conn = null;
		PreparedStatement ps = null;
		Integer rtn = -1;
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("UPDATE tb_authtoken SET status=2 WHERE ip=? AND agent=? AND uid=? AND status!=2");
			ps.setString(1, ip);
			ps.setString(2, agent);
			ps.setInt(3, userId);
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