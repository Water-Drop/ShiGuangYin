package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.AccountBook;
import util.JDBCHelper;


public class AccountBookDaoimpl implements AccountBookDao{
	JDBCHelper jh = new JDBCHelper();
    public List<AccountBook> getAllAccountBooks(){
    	Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AccountBook> accountBooks = new ArrayList<AccountBook>();
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("SELECT id,uid,name,description,createTime,planCost,endTime,status FROM tb_accountbook WHERE status!=2");
			rs = ps.executeQuery();
			while (rs.next()){
				AccountBook accountBook = new AccountBook();
				accountBook.setId(rs.getInt("id"));
				accountBook.setUid(rs.getInt("uid"));
				accountBook.setName(rs.getString("name"));
				accountBook.setDescription(rs.getString("description"));
				accountBook.setCreateTime(rs.getString("createTime"));
				accountBook.setPlanCost(rs.getInt("planCost"));
				accountBook.setEndTime(rs.getString("endTime"));
				accountBook.setStatus(rs.getInt("status"));
				accountBooks.add(accountBook);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
    	return accountBooks;
    }
    public AccountBook getAccountBookById(Integer id){
    	Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
    	AccountBook accountBook = new AccountBook();
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("SELECT uid,name,description,createTime,planCost,endTime,status FROM tb_accountbook WHERE id=? AND status!=2");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.first() != false){
				accountBook.setId(id);
				accountBook.setUid(rs.getInt("uid"));
				accountBook.setName(rs.getString("name"));
				accountBook.setDescription(rs.getString("description"));
				accountBook.setCreateTime(rs.getString("createTime"));
				accountBook.setPlanCost(rs.getInt("planCost"));
				accountBook.setEndTime(rs.getString("endTime"));
				accountBook.setStatus(rs.getInt("status"));
			}  else {
				accountBook.setStatus(2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
    	return accountBook;
    }
    public Integer addAccountBook(AccountBook accountBook){
        Connection conn = null;
		PreparedStatement ips = null;
		PreparedStatement sps = null;
		ResultSet rs = null;
		Integer newid = -1;
		try {
			conn = jh.getConnection();
			conn.setAutoCommit(false);
			ips = conn.prepareStatement("INSERT INTO tb_accountbook (uid,name,description,createTime,planCost,endTime,status) VALUES (?,?,?,?,?,?,0)");
			ips.setInt(1,accountBook.getUid());
			ips.setString(2,accountBook.getName());
			ips.setString(3,accountBook.getDescription());
			ips.setString(4,accountBook.getCreateTime());
			ips.setInt(5, accountBook.getPlanCost());
			ips.setString(6, accountBook.getEndTime());
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
    public Integer deleteAccountBook(Integer id, Integer uid){
    	Connection conn = null;
		PreparedStatement ps = null;
		Integer rtn = -1;
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("UPDATE tb_accountbook SET status=2 WHERE id=? AND uid=?");
			ps.setInt(1, id);
			ps.setInt(2, uid);
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
    public Integer modifyAccountBook(AccountBook accountBook){
    	Connection conn = null;
		PreparedStatement ps = null;
		Integer rtn = -1;
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("UPDATE tb_accountbook SET uid=?,name=?,description=?,createTime=?,planCost=?,endTime=? WHERE id=?");
			ps.setInt(1,accountBook.getUid());
			ps.setString(2,accountBook.getName());
			ps.setString(3,accountBook.getDescription());
			ps.setString(4,accountBook.getCreateTime());
			ps.setInt(5, accountBook.getPlanCost());
			ps.setString(6, accountBook.getEndTime());
			ps.setInt(7,accountBook.getId());
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
    public List<AccountBook> getAccountBooksByUid(Integer uid){
    	Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AccountBook> accountBooks = new ArrayList<AccountBook>();
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("SELECT id,uid,name,description,createTime,planCost,endTime,status FROM tb_accountbook WHERE uid=? AND status!=2");
			ps.setInt(1, uid);
			rs = ps.executeQuery();
			while (rs.next()){
				AccountBook accountBook = new AccountBook();
				accountBook.setId(rs.getInt("id"));
				accountBook.setUid(rs.getInt("uid"));
				accountBook.setName(rs.getString("name"));
				accountBook.setDescription(rs.getString("description"));
				accountBook.setCreateTime(rs.getString("createTime"));
				accountBook.setPlanCost(rs.getInt("planCost"));
				accountBook.setEndTime(rs.getString("endTime"));
				accountBook.setStatus(rs.getInt("status"));
				accountBooks.add(accountBook);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
    	return accountBooks;
    }
}