package biz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.AccountRecord;
import net.sf.json.JSONObject;
import dao.AccountRecordDao;
import dao.AccountRecordDaoimpl;

public class AccountRecordService{
    AccountRecordDao accountRecordDao = new AccountRecordDaoimpl();
    public String getAccountRecordsByAbid(String param){
        Integer status = -1;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
        JSONObject paramjson = JSONObject.fromObject(param);
        Integer abid = paramjson.getInt("abid");
		Integer uid = paramjson.getInt("uid");
        List<AccountRecord> accountRecords = accountRecordDao.getAccountRecordsByAbid(abid,uid);
        if (accountRecords.size() > 0){
			status = 0;
			map.put("AccountRecords", accountRecords);
      	} else {
      		status = 1;
      	}
        } catch (Exception e){
        	e.printStackTrace();
        }
		map.put("status", status.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
    }
    public String getAccountRecordById(String param){
    	Integer status = -1;
    	Map<String, String> map = new HashMap<String, String>();
    	try {
			JSONObject paramjson = JSONObject.fromObject(param);
			AccountRecord accountRecord = new AccountRecord();
			accountRecord = accountRecordDao.getAccountRecordById(paramjson.getInt("id"),paramjson.getInt("uid"));
		  	if (accountRecord.getStatus() != 2) {//status = 2 means no such entity for the id
		  		status = 0;
		  		map.put("id", accountRecord.getId().toString());
		  		map.put("abid",accountRecord.getAbid().toString());
		  		map.put("cost",accountRecord.getCost().toString());
		  		map.put("description",accountRecord.getDescription().toString());
		  		map.put("createTime",accountRecord.getCreateTime().toString());
		  		map.put("status",accountRecord.getStatus().toString());
		  	} else {
				status = 1;// no such entity for the id
			}
    	} catch (Exception e){
    		e.printStackTrace();
    	}
		map.put("status", status.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();	
    }
    public String addAccountRecord(String param){
    	Integer status = -1;
		Integer accountRecordid = -1;
		try {
			JSONObject paramjson = JSONObject.fromObject(param);
			AccountRecord accountRecord = new AccountRecord();
			accountRecord.setAbid(paramjson.getInt("abid"));
			accountRecord.setCost(paramjson.getInt("cost"));
			accountRecord.setDescription(paramjson.getString("description"));
			if (paramjson.getString("createTime").equals("")){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
				accountRecord.setCreateTime(sdf.format(new Date()));
			} else {
				accountRecord.setCreateTime(paramjson.getString("createTime"));
			}
	   		accountRecordid = accountRecordDao.addAccountRecord(accountRecord,paramjson.getInt("uid"));
			if (accountRecordid > 0){
				status = 0;
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", status.toString());
		map.put("accountRecordid", accountRecordid.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
    }
    public String modifyAccountRecord(String param){
    	Integer status = -1;
    	try {
			JSONObject paramjson = JSONObject.fromObject(param);
			AccountRecord accountRecord = new AccountRecord();
			accountRecord.setId(paramjson.getInt("id"));
			accountRecord.setAbid(paramjson.getInt("abid"));
			accountRecord.setCost(paramjson.getInt("cost"));
			accountRecord.setDescription(paramjson.getString("description"));
			if (!paramjson.getString("createTime").equals("")){
				accountRecord.setCreateTime(paramjson.getString("createTime"));
			}
	   		status = accountRecordDao.modifyAccountRecord(accountRecord,paramjson.getInt("uid"));
    	} catch (Exception e){
    		e.printStackTrace();
    	}
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", status.toString());	
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
    }
    public String deleteAccountRecord(String param){
    	Integer status = -1;
    	try {
			JSONObject paramjson = JSONObject.fromObject(param);
			status = accountRecordDao.deleteAccountRecord(paramjson.getInt("id"),paramjson.getInt("uid"));
    	} catch (Exception e){
    		e.printStackTrace();
    	}
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", status.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
    }
}