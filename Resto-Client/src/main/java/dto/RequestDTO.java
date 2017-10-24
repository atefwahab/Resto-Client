package dto;

import java.util.List;

/**
 * 
 * @author Mohamed.atef
 *
 *this class represent DTO for request.
 */

public class RequestDTO {

	
	String url;
	String method;
	List<Parameter> headerParams;
	List<Parameter> bodyParams;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public List<Parameter> getHeaderParams() {
		return headerParams;
	}
	public void setHeaderParams(List<Parameter> headerParams) {
		this.headerParams = headerParams;
	}
	public List<Parameter> getBodyParams() {
		return bodyParams;
	}
	public void setBodyParams(List<Parameter> bodyParams) {
		this.bodyParams = bodyParams;
	}
	
	
	

}
