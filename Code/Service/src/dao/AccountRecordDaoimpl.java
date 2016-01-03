package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.AccountRecord;
import util.JDBCHelper;


public class AccountRecordDaoimpl implements AccountRecordDao{
	JDBCHelper jh = new JDBCHelper();
	public List<AccountRecord> getAccountRecordsByAbid(Integer abid, Integer uid){
    	Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AccountRecord> accountRecords = new ArrayList<AccountRecord>();
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("SELECT t1.id,abid,cost,t1.description,t1.createTime,t1.status FROM tb_accountrecord t1 JOIN tb_accountbook t2 ON t1.abid=t2.id WHERE t1.abid=? AND t2.uid=? AND t1.status!=2 AND t2.status!=2");
			ps.setInt(1, abid);
			ps.setInt(2, uid);
			rs = ps.executeQuery();
			while (rs.next()){
				AccountRecord accountRecord = new AccountRecord();
				accountRecord.setId(rs.getInt("id"));
				accountRecord.setAbid(rs.getInt("abid"));
				accountRecord.setCost(rs.getInt("cost"));
				accountRecord.setDescription(rs.getString("description"));
				accountRecord.setCreateTime(rs.getString("createTime"));
				accountRecord.setStatus(rs.getInt("status"));
				accountRecords.add(accountRecord);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
    	return accountRecords;
    }
    public AccountRecord getAccountRecordById(Integer id, Integer uid){
    	Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
    	AccountRecord accountRecord = new AccountRecord();
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("SELECT abid,cost,t1.description,t1.createTime,t1.status FROM tb_accountrecord t1 JOIN tb_accountbook t2 ON t1.abid=t2.id WHERE t1.id=? AND t2.uid=? AND t1.status!=2 AND t2.status!=2");
			ps.setInt(1, id);
			ps.setInt(2, uid);
			rs = ps.executeQuery();
			if (rs.first() != false){
				accountRecord.setId(id);
				accountRecord.setAbid(rs.getInt("abid"));
				accountRecord.setCost(rs.getInt("cost"));
				accountRecord.setDescription(rs.getString("description"));
				accountRecord.setCreateTime(rs.getString("createTime"));
				accountRecord.setStatus(rs.getInt("status"));
			}  else {
				accountRecord.setStatus(2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
    	return accountRecord;
    }
    public Integer addAccountRecord(AccountRecord accountRecord, Integer uid){
    	Connection conn = null;
		PreparedStatement ips = null;
		PreparedStatement sps = null;
		ResultSet rs = null;
		Integer newid = -1;
    	if (getAccountBookNumByAbidAndUid(accountRecord.getAbid(), uid) == 1){
    		try {
    			conn = jh.getConnection();
    			conn.setAutoCommit(false);
    			ips = conn.prepareStatement("INSERT INTO tb_accountrecord (abid,cost,description,createTime,status) VALUES (?,?,?,?,0)");
    			ips.setInt(1,accountRecord.getAbid());
    			ips.setInt(2,accountRecord.getCost());
    			ips.setString(3,accountRecord.getDescription());
    			ips.setString(4,accountRecord.getCreateTime());
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
    	} else {
    		newid = -2; // Mismatch abid and uid
    	}
		return newid;
    }
    public Integer deleteAccountRecord(Integer id, Integer uid){
    	Connection conn = null;
		PreparedStatement ps = null;
		Integer rtn = -1;
		if (getAccountBookNumByIdAndUid(id, uid) == 1){
			try {
				conn = jh.getConnection();
				ps = conn.prepareStatement("UPDATE tb_accountrecord SET status=2 WHERE id=?");
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
		} else {
			rtn = -2; // Mismatch id and uid
		}
		return rtn;
    }
    public Integer modifyAccountRecord(AccountRecord accountRecord, Integer uid){
    	Connection conn = null;
		PreparedStatement ps = null;
		Integer rtn = -1;
		if (getAccountBookNumByAbidAndUid(accountRecord.getAbid(), uid) == 1){
			try {
				conn = jh.getConnection();
				ps = conn.prepareStatement("UPDATE tb_accountrecord SET abid=?,cost=?,description=?,createTime=? WHERE id=?");
				ps.setInt(1,accountRecord.getAbid());
				ps.setInt(2,accountRecord.getCost());
				ps.setString(3,accountRecord.getDescription());
				ps.setString(4,accountRecord.getCreateTime());
				ps.setInt(5,accountRecord.getId());
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
		} else {
			rtn = -2; // Mismatch abid and uid
		}
		return rtn;
    }
    private Integer getAccountBookNumByAbidAndUid(Integer abid, Integer uid){
    	Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
    	Integer accountBookNum = 0;
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("SELECT status FROM tb_accountbook WHERE id=? AND uid=? AND status!=2");
			ps.setInt(1, abid);
			ps.setInt(2, uid);
			rs = ps.executeQuery();
			if (rs.first() != false){
				accountBookNum = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
    	return accountBookNum;
    }
    private Integer getAccountBookNumByIdAndUid(Integer id, Integer uid){
    	Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
    	Integer accountBookNum = 0;
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("SELECT t1.status FROM tb_accountrecord t1 JOIN tb_accountbook t2 ON t1.abid=t2.id WHERE t1.id=? AND t2.uid=? AND t1.status!=2 AND t2.status!=2");
			ps.setInt(1, id);
			ps.setInt(2, uid);
			rs = ps.executeQuery();
			if (rs.first() != false){
				accountBookNum = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
    	return accountBookNum;
    }
}