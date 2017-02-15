package gr.uoa.di.madgik.datatransformation.harvester.responsesofservice;

public class ServiceResponse {

	private boolean status = false;
	private Object data = null;
	private String message = null;
	
	public ServiceResponse() {}
	
	public ServiceResponse(boolean status, Object data, String message) {
		this.data = data;
		this.status = status;
		this.message = message;
	}

	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
