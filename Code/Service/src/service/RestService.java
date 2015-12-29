package service;

import java.lang.reflect.Method;
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

@Path("/RestService")
public class RestService {
	AuthTokenDao atd = new AuthTokenDaoimpl();
	@Context HttpServletRequest req; 
	@Path("/RestAPI")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String RestAPIHandler(String param){
		String rtn = "";
		Map<String, String> map = new HashMap<String, String>();
		try {  
			JSONObject json_param = JSONObject.fromObject(param);
			String bizClassName = json_param.getString("cname");
			String bizMethodName = json_param.getString("mname");
			String mParam = json_param.getString("mparam");
			String authToken = json_param.getString("authToken");
			Integer userId = json_param.getInt("userId");
			String requestIp = req.getRemoteAddr();
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
		return rtn;
	}  
}
