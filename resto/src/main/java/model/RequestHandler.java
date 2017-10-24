package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import dto.Constants;
import dto.Parameter;
import dto.RequestDTO;
import dto.ResponseDTO;

public class RequestHandler {

	
	public ResponseDTO sendRequest(RequestDTO requestDTO){
		ResponseDTO responseDTO=null;
		// the logic goes here

		
		switch(requestDTO.getMethod()){
		
		case Constants.METHOD_GET:
			responseDTO=this.sendGetRequest(requestDTO);
			break;
		case Constants.METHOD_POST:
			responseDTO=sendPostRequest(requestDTO);
			break;
		}
		
		
		return responseDTO;
		
	}
		

		/**
		 * Sends a GET Request
		 * @param requestDTO
		 * @throws IOException 
		 * @throws ClientProtocolException 
		 */
		private ResponseDTO sendGetRequest(RequestDTO requestDTO)  {
			
			ResponseDTO responseDTO = new ResponseDTO();
			
			// here we start  
			try {
				// 1 get client from httpClient
				CloseableHttpClient client = HttpClients.createDefault();

				//2- composite url with parameters if it is existed.
				String url = requestDTO.getUrl();

				url=prepareQueryParameters(requestDTO, url);
				// temporary log
				System.out.println("URL -> "+url);

				//3 -  create a get request
				HttpGet getRequest = new HttpGet(url);

				//4- Add user agent to request.
				getRequest.addHeader("User-Agent", Constants.USER_AGENT);

				// add header Parameters
				for (Parameter parameter : requestDTO.getHeaderParams()) {

					getRequest.addHeader(parameter.getKey(), parameter.getValue());
				}

				// 5- fire the request
				CloseableHttpResponse httpResponse;

				httpResponse = client.execute(getRequest);
				// 6 - prepare response dto.
				responseDTO=fillResponseDTO(httpResponse);

				// 7- close response
				httpResponse.close();

			} catch (Exception e) {

				handleException(e, responseDTO);
			}


			return responseDTO;
		}
		
		/***
		 * Sends a POST Request
		 * @param requestDTO
		 * @return
		 */
		private ResponseDTO sendPostRequest(RequestDTO requestDTO){
			
			
		ResponseDTO responseDTO = new ResponseDTO();
			
			// here we start  
			try {
				// 1 get client from httpClient
				CloseableHttpClient client = HttpClients.createDefault();

				//2- composite url with parameters if it is existed.
				String url = requestDTO.getUrl();

				// temporary log
				System.out.println("URL -> "+url);

				//3 -  create a post request
				HttpPost postRequest = new HttpPost(url);
				
				//add parameters to request
				List <NameValuePair> bodyParams = preparePostRequestParameters(requestDTO);
				if(bodyParams.size()>0)
					postRequest.setEntity(new UrlEncodedFormEntity(bodyParams));
				
				//4- Add user agent to request.
				postRequest.addHeader("User-Agent", Constants.USER_AGENT);

				// add header Parameters
				for (Parameter parameter : requestDTO.getHeaderParams()) {

					postRequest.addHeader(parameter.getKey(), parameter.getValue());
				}

				// 5- fire the request
				CloseableHttpResponse httpResponse = client.execute(postRequest);
				// 6 - prepare response dto.
				responseDTO=fillResponseDTO(httpResponse);

				// 7- close respone
				httpResponse.close();

			} catch (Exception e) {

				handleException(e, responseDTO);
			}


			return responseDTO;
			
		}

		
		private String prepareQueryParameters(RequestDTO requestDTO, String url) {
			//add parameters to URL
			Iterator<Parameter> iterator = requestDTO.getBodyParams().iterator();
				
			if(iterator.hasNext()){
				
				url+="?";
				while(iterator.hasNext()) {
					
					Parameter parameter = iterator.next();
					url+=parameter.getKey()+"="+parameter.getValue();
					if(iterator.hasNext()){
						url+="&";
					}
					
					
				}
				
			}
			
			return url;
		}
		
		
		/**
		 * to add parameters to request
		 * @param requestDTO
		 * @param url
		 * @return
		 */
		private List <NameValuePair> preparePostRequestParameters(RequestDTO requestDTO) {
			
			
			
			//add parameters to URL
			Iterator<Parameter> iterator = requestDTO.getBodyParams().iterator();
			
			List <NameValuePair> bodyParams = new ArrayList <NameValuePair>();
		
			while(iterator.hasNext()){
				
				Parameter parameter =  iterator.next();
				bodyParams.add(new BasicNameValuePair(parameter.getKey(), parameter.getValue()));
			}
						
			return bodyParams;
		}
		
		
		private ResponseDTO fillResponseDTO(CloseableHttpResponse httpResponse) throws IOException{
			
			// temporary log
			System.out.println("Response Status is >> "+ httpResponse.getStatusLine().getStatusCode());
			
			ResponseDTO responseDTO = new ResponseDTO();
			//set status
			responseDTO.setStatus(httpResponse.getStatusLine().getStatusCode());
			
			//read its content
			
			// first of all create a bufferReader
			BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";

			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			System.out.println("Response is >> " + result);
			responseDTO.setBody(result);
			
			// done has
			return responseDTO;
		}
		
		
		private void handleException(Exception exception,ResponseDTO responseDTO){
			
			// temporary till log it
			exception.printStackTrace();
			if(exception.getMessage()!=null)
			responseDTO.setBody(new StringBuffer(exception.getMessage()));
			else
				responseDTO.setBody(new StringBuffer("AN ERORR HAS OCCURED !!"));	
			
			
			
		}

}
