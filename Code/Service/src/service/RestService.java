package service;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;
import dao.AuthTokenDao;
import dao.AuthTokenDaoimpl;
import dao.InvokeRecordDao;
import dao.InvokeRecordDaoimpl;

@Path("/RestService")
public class RestService {
	AuthTokenDao atd = new AuthTokenDaoimpl();
	InvokeRecordDao ird = new InvokeRecordDaoimpl();
	@Context HttpServletRequest req; 
	@Path("/RestAPI")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String RestAPIHandler(String param){
		String rtn = "";
		String bizClassName = "";
		String bizMethodName = "";
		String mParam = "";
		String authToken = "";
		Integer userId = 0;
		String requestIp = "";
		Map<String, String> map = new HashMap<String, String>();
		try {  
			JSONObject json_param = JSONObject.fromObject(param);
			bizClassName = json_param.getString("cname");
			bizMethodName = json_param.getString("mname");
			mParam = json_param.getString("mparam");
			authToken = json_param.getString("authToken");
			userId = json_param.getInt("userId");
			requestIp = req.getRemoteAddr();
    		String userAgent = req.getHeader("user-agent");
    		if (authToken.equals(atd.getTokenByIpAgentAndUserId(requestIp, userAgent, userId))){
    			JSONObject mObject = JSONObject.fromObject(mParam);
    			mObject.put("uid", userId);
    			bizClassName = "biz." + bizClassName + "Service";
    	        Class<?> bizClass = Class.forName(bizClassName);  
    	        Method bizMethod =  bizClass.getMethod(bizMethodName, Class.forName("java.lang.String")) ;
    	        rtn = bizMethod.invoke(bizClass.newInstance(), mObject.toString()).toString();
    		} else {
    			map.put("status", "-200");
    	    	map.put("msg", "Authentication failed.");// Wrong userId and authToken.
    	    	JSONObject json = JSONObject.fromObject(map);
    	    	rtn = json.toString();
    		}
	    } catch (ClassNotFoundException e) { 
	    	map.put("status", "-101");
	    	map.put("msg", "Class not found exception.");
	    	JSONObject json = JSONObject.fromObject(map);
	    	rtn = json.toString();
	    } catch (NoSuchMethodException e) {  
	    	map.put("status", "-102");
	    	map.put("msg", "No such method exception.");
	    	JSONObject json = JSONObject.fromObject(map);
	    	rtn = json.toString();
	    } catch (Exception e) {
	    	map.put("status", "-100");
	    	map.put("msg", e.getMessage());
	    	e.printStackTrace();
	    	JSONObject json = JSONObject.fromObject(map);
	    	rtn = json.toString();
	    }
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
			if (rtn.length() > 199){
				ird.addInvokeRecord(userId, requestIp, bizClassName, bizMethodName, rtn.substring(0, 199), sdf.format(new Date()));
			} else {
				ird.addInvokeRecord(userId, requestIp, bizClassName, bizMethodName, rtn, sdf.format(new Date()));
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return rtn;
	}  
}
