package dto;

public class ResponseDTO {

	int status;
	StringBuffer body;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public StringBuffer getBody() {
		return body;
	}
	public void setBody(StringBuffer body) {
		this.body = body;
	}
}
