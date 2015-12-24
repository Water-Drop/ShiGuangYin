package service;

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

import model.AuthToken;
import model.User;
import net.sf.json.JSONObject;
import util.MD5Helper;
import dao.AuthTokenDao;
import dao.AuthTokenDaoimpl;
import dao.UserDao;
import dao.UserDaoimpl;

@Path("/Account")
public class UserService {
	UserDao ud = new UserDaoimpl();
	AuthTokenDao atd = new AuthTokenDaoimpl();
    @Context HttpServletRequest req; 
	/*
    public String getAllUsers(String param){
        Integer status = -1;
        Map<String, String> map = new HashMap<String, String>();
        List<User> users = ud.getAllUsers();
        if (users.size() > 0){
			status = 0;
			List<String> e_jsons = new ArrayList<String>();
			for (int i = 0; i < users.size(); i++){
			Map<String, String> e_map = new HashMap<String, String>();
            	e_map.put("email",users.get(i).getEmail().toString());
            	e_map.put("telephone",users.get(i).getTelephone().toString());
            	e_map.put("password",users.get(i).getPassword().toString());
            	e_map.put("type",users.get(i).getType().toString());
			JSONObject e_json = JSONObject.fromObject(e_map);
			e_jsons.add(e_json.toString());
      	    }
      	    JSONArray jsonArray = JSONArray.fromObject(e_jsons);
			map.put("Users", jsonArray.toString());
      	} else {
      		status = 1;
      	}
		map.put("status", status.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
    }
    public String getUserById(String param){
    	Integer status = -1;
    	Map<String, String> map = new HashMap<String, String>();
		JSONObject paramjson = JSONObject.fromObject(param);
		User user = new User();
		user = ud.getUserById(paramjson.getInt("userId"));
	  	if (user.getStatus() != 2) {//status = 2 means no such entity for the id
	  		status = 0;
	  		map.put("email",user.getEmail().toString());
	  		map.put("telephone",user.getTelephone().toString());
	  		map.put("password",user.getPassword().toString());
	  		map.put("type",user.getType().toString());
	  	} else {
			status = 1;// no such entity for the id
			}
		map.put("status", status.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();	
    }
    
    public String modifyUser(String param){
    	Integer status = -1;
		JSONObject paramjson = JSONObject.fromObject(param);
		User user = new User();
			user.setEmail(paramjson.getString("email"));
			user.setTelephone(paramjson.getString("telephone"));
			user.setPassword(paramjson.getString("password"));
			user.setType(paramjson.getInt("type"));
   		status = ud.modifyUser(user);
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", status.toString());	
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
    }
    public String deleteUser(String param){
    	Integer status = -1;
		JSONObject paramjson = JSONObject.fromObject(param);
		status = ud.deleteUser(paramjson.getInt("userId"));
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", status.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
    }
    */
    @Path("/SignUp")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
    public String addUser(String param){
    	Integer status = -1;
		Integer userId = -1;
		String token = "";
		try {
			JSONObject paramjson = JSONObject.fromObject(param);
			User user = new User();
			String paramEmail = paramjson.getString("email");
			String paramTelephone = paramjson.getString("telephone");
			if (paramEmail != ""){
				user.setEmail(paramEmail);
				if (ud.getUserIdByEmail(paramEmail) > 0){
					System.out.println(ud.getUserIdByEmail(paramEmail));
					status = 1;// Already exist email.
				} else {
					status = 0;
				}
			}
			if (paramTelephone != ""){
				user.setTelephone(paramTelephone);
				ud.getUserIdByTelephone(paramTelephone);
				if (ud.getUserIdByTelephone(paramTelephone) > 0){
					if (status == 1){
						status = 3;// Already exist email and telephone.
					} else {
						status = 2;// Already exist telephone.
					}
				} else {
					if (!(status > 0)){
						status = 0;
					}
				}
			}
			if (status == 0){
				user.setPassword(paramjson.getString("password"));
				user.setType(paramjson.getInt("type"));
				if ((userId = ud.addUser(user)) > 0){
					String requestIp = req.getRemoteAddr();
		    		String userAgent = req.getHeader("user-agent");
		    		AuthToken at = new AuthToken();
		    		at.setUid(userId);
		    		at.setIp(requestIp);
		    		at.setAgent(userAgent);
		    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
		    		at.setAuthTime(sdf.format(new Date()));
		    		token = MD5Helper.getMD5(at.getUid().toString() + at.getIp() + at.getAgent() + at.getAuthTime());
		    		at.setToken(token);
		    	    if (!(atd.addAuthToken(at) > 0)){
		    	    	status = 5;// Exception while generate authentication token.
		    	    }
				} else {
					status = 4;// Exception while add user.
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", status.toString());
		if (status == 0){
    		map.put("userId", userId.toString());
    		map.put("authToken", token);
    	}
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
    }
    @Path("/SignIn")
   	@POST
   	@Produces(MediaType.APPLICATION_JSON)
    public String getUserByLoginNameAndPassword(String param){
    	Integer status = -1;
    	Integer userId = -1;
    	String token = "";
    	try {
	    	JSONObject paramjson = JSONObject.fromObject(param);
	    	String loginName = paramjson.getString("loginName");
	    	String password = paramjson.getString("password");
	    	Integer type = paramjson.getInt("type");
	    	if (!((userId = ud.getUserIdByEmailAndPassword(loginName, password, type)) > 0)){
	    		userId = ud.getUserIdByTelephoneAndPassword(loginName, password, type);
	    	}
	    	if (userId > 0){
	    		String requestIp = req.getRemoteAddr();
	    		String userAgent = req.getHeader("user-agent");
	    		AuthToken at = new AuthToken();
	    		at.setUid(userId);
	    		at.setIp(requestIp);
	    		at.setAgent(userAgent);
	    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
	    		at.setAuthTime(sdf.format(new Date()));
	    		token = MD5Helper.getMD5(at.getUid().toString() + at.getIp() + at.getAgent() + at.getAuthTime());
	    		at.setToken(token);
	    	    if (!(atd.addAuthToken(at) > 0)){
	    	    	status = 2;// Exception while generate authentication token.
	    	    } else {
	    	    	status = 0;
	    	    }
	    	} else {
	    		status = 1;// No such user with the loginName and password
	    	}
    	} catch (Exception e){
    		e.printStackTrace();
    	}
    	Map<String, String> map = new HashMap<String, String>();
    	map.put("status", status.toString());
    	if (status == 0){
    		map.put("userId", userId.toString());
    		map.put("authToken", token);
    	}
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
    }
}
