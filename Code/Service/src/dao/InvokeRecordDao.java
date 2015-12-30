package dao;

public interface InvokeRecordDao {
	public Integer addInvokeRecord(Integer uid, String ip, String cname, String mname, String returnContent, String invokeTime);
}
