package model;
 
public class AccountBook {
    private Integer uid;
    private String name;
    private String description;
    private String createTime;
	private Integer status; 
	private Integer id; 
	
    public AccountBook() {    
    }
    public AccountBook(Integer uid, String name, String description, String createTime) {
    	this.uid = uid;
    	this.name = name;
    	this.description = description;
    	this.createTime = createTime;
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
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public Integer getStatus() {
    	return status;
    }
    public void setStatus(Integer status) {
    	this.status = status;
    }
}
