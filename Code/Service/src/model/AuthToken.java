package model;
 
public class AuthToken {
    private Integer uid;
    private String authTime;
    private String ip;
    private String agent;
    private String token;
	private Integer status; 
	private Integer id; 
	
    public AuthToken() {    
    }
    public AuthToken(Integer uid, String authTime, String ip, String agent, String token) {
    	this.uid = uid;
    	this.authTime = authTime;
    	this.ip = ip;
    	this.agent = agent;
    	this.token = token;
    }

	public Integer getId() {
    	return id;
    }
    public void setId(Integer id) {
    	this.id = id;
    }
    public Integer getUid() {
        return uid;
    }
    public void setUid(Integer uid) {
        this.uid = uid;
    }
    public String getAuthTime() {
        return authTime;
    }
    public void setAuthTime(String authTime) {
        this.authTime = authTime;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getAgent() {
        return agent;
    }
    public void setAgent(String agent) {
        this.agent = agent;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public Integer getStatus() {
    	return status;
    }
    public void setStatus(Integer status) {
    	this.status = status;
    }
}
