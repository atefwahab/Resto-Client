package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Savepoint;
import java.util.Iterator;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import model.RequestHandler;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

import dto.Parameter;
import dto.RequestDTO;
import dto.ResponseDTO;
import view.MainView;

public class LauncherController {

	MainView mainView;

	public LauncherController() {

		mainView = new MainView(this);

	}

	public static void main(String[] args) {
		
		
//		System.out.println("C:\\Users\\atefw\\Desktop\\google service.json");
//		new LauncherController().SaveRequest(new RequestDTO(), "C:\\Users\\atefw\\Desktop\\TEFO\\google service.json");
		

		 try {
			UIManager.setLookAndFeel(
			            UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			
			
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 //enable debugging of Apache HttpClient 
		 enableLogAndDebugApache();
		 
		new LauncherController();
	}

	private static void enableLogAndDebugApache() {
		System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.SimpleLog");
		 System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		 System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.wire", "DEBUG");
		// System.setProperty("javax.net.debug", "all");
		 
	}

	
	
	public void sendRequest(RequestDTO requestDTO){
		
		RequestHandler handler = new RequestHandler();
		
		ResponseDTO response=null;
			response = handler.sendRequest(requestDTO);
	
		mainView.viewResponse(response);
		// TODO The Print is not right.
		System.out.println("done ..");
	}

	/**
	 * POST Case
	 * @param requestDTO
	 */
	private ResponseDTO sendPostRequest(RequestDTO requestDTO) {

		HttpClient httpClient = new DefaultHttpClient();
		return null;
	}

	/**
	 * to save request into file ..
	 * @param requestDTO
	 */
	public void SaveRequest(RequestDTO requestDTO,String filepath){
		
		//convert it to json
		Gson gson = new Gson();
		String gsonObj = gson.toJson(requestDTO);
		System.out.println("GSON ... "+gsonObj);
		try {
			File fileToCreate = new File(filepath);
			if(!fileToCreate.getParentFile().exists()){
				fileToCreate.getParentFile().mkdir();
			}
			FileWriter fileWriter = new FileWriter(fileToCreate);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(gsonObj);
			if(bufferedWriter!=null)
				bufferedWriter.close();
			if(fileWriter!=null)
				fileWriter.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

	public RequestDTO loadRequestDTO(String filePath){
		
		RequestDTO requestDTO=null;
		String gsonRequest = new String();
		Gson gson = new Gson();
		try {
			FileReader reader = new FileReader(filePath);
			BufferedReader bufferedReader = new BufferedReader(reader);
			String line="";
			StringBuffer buffer = new StringBuffer();
			
			while((line=bufferedReader.readLine())!=null){
				buffer.append(line);
			}
			gsonRequest=new String(buffer);
			reader.close();
			bufferedReader.close();
			requestDTO = gson.fromJson(gsonRequest, RequestDTO.class);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return requestDTO;
	}
	
}
