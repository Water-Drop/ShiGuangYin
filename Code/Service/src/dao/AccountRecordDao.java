package dao;

import java.util.List;

import model.AccountRecord;

public interface AccountRecordDao {
	public List<AccountRecord> getAccountRecordsByAbid(Integer abid, Integer uid);
	public AccountRecord getAccountRecordById(Integer id, Integer uid);
	public Integer addAccountRecord(AccountRecord accountRecord, Integer uid);
	public Integer deleteAccountRecord(Integer id, Integer uid);
	public Integer modifyAccountRecord(AccountRecord accountRecord, Integer uid);   
}
