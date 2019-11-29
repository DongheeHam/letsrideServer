package clabs.tools;

public class ResObject {

	private int rc;
	private String resMessage;
	private Object result;
	
	public ResObject() { 
		this(0, ""); 
	}
	public ResObject(int rc, String msg) {
		this(rc, msg, null);
	}
	public ResObject(int rc, String msg, Object obj) {
		this.rc = rc;
		this.resMessage = msg;
		this.result = obj;
	}
	public int getRc() {
		return rc;
	}
	public void setRc(int rc) {
		this.rc = rc;
	}
	public String getResMessage() {
		return resMessage;
	}
	public void setResMessage(String resMessage) {
		this.resMessage = resMessage;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
}
