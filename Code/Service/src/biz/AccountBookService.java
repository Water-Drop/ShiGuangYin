package biz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.AccountBook;
import net.sf.json.JSONObject;
import dao.AccountBookDao;
import dao.AccountBookDaoimpl;

public class AccountBookService{
    AccountBookDao accountBookDao = new AccountBookDaoimpl();
    public String getAccountBooksByUid(String param){
        Integer status = -1;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
        JSONObject paramjson = JSONObject.fromObject(param);
		Integer uid = paramjson.getInt("uid");
        List<AccountBook> accountBooks = accountBookDao.getAccountBooksByUid(uid);
        if (accountBooks.size() > 0){
			status = 0;
			map.put("AccountBooks", accountBooks);
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
    public String getAccountBookById(String param){
    	Integer status = -1;
    	Map<String, String> map = new HashMap<String, String>();
    	try {
			JSONObject paramjson = JSONObject.fromObject(param);
			AccountBook accountBook = new AccountBook();
			accountBook = accountBookDao.getAccountBookById(paramjson.getInt("id"));
		  	if (accountBook.getStatus() != 2) {//status = 2 means no such entity for the id
		  		status = 0;
		  		map.put("id", accountBook.getId().toString());
		  		map.put("uid",accountBook.getUid().toString());
		  		map.put("name",accountBook.getName().toString());
		  		map.put("description",accountBook.getDescription().toString());
		  		map.put("createTime",accountBook.getCreateTime().toString());
		  		map.put("status",accountBook.getStatus().toString());
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
    public String addAccountBook(String param){
    	Integer status = -1;
		Integer accountBookid = -1;
		try {
			JSONObject paramjson = JSONObject.fromObject(param);
			AccountBook accountBook = new AccountBook();
			accountBook.setUid(paramjson.getInt("uid"));
			accountBook.setName(paramjson.getString("name"));
			accountBook.setDescription(paramjson.getString("description"));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
			accountBook.setCreateTime(sdf.format(new Date()));
	   		accountBookid = accountBookDao.addAccountBook(accountBook);
			if (accountBookid > 0){
				status = 0;
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", status.toString());
		map.put("accountBookid", accountBookid.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
    }
    public String modifyAccountBook(String param){
    	Integer status = -1;
    	try {
			JSONObject paramjson = JSONObject.fromObject(param);
			AccountBook accountBook = new AccountBook();
			accountBook.setId(paramjson.getInt("id"));
			accountBook.setUid(paramjson.getInt("uid"));
			accountBook.setName(paramjson.getString("name"));
			accountBook.setDescription(paramjson.getString("description"));
	   		status = accountBookDao.modifyAccountBook(accountBook);
    	} catch (Exception e){
    		e.printStackTrace();
    	}
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", status.toString());	
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
    }
    public String deleteAccountBook(String param){
    	Integer status = -1;
    	try {
			JSONObject paramjson = JSONObject.fromObject(param);
			status = accountBookDao.deleteAccountBook(paramjson.getInt("id"),paramjson.getInt("uid"));
    	} catch (Exception e){
    		e.printStackTrace();
    	}
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", status.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
    }
}