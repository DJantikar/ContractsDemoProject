package com.salesforce;


import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ContractsController {
	static final String USERNAME     = "**";
	static final String PASSWORD     = "**";
	static final String LOGINURL     = "**";
	static final String GRANTSERVICE = "**";
	static final String CLIENTID     = "**";
	static final String CLIENTSECRET = "**";
	
	@RequestMapping("/authentication")
	public String oAuthSessionProvider()
			throws HttpException, IOException, JSONException {

		// Assemble the login request URL
		String baseUrl = LOGINURL +
				GRANTSERVICE +
				"&client_id=" + CLIENTID +
				"&client_secret=" + CLIENTSECRET +
				"&username=" + USERNAME +
				"&password=" + PASSWORD;
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(baseUrl);
		BasicResponseHandler handler = new BasicResponseHandler();
		HttpResponse response = client.execute(post);
		JSONObject json =new JSONObject(handler.handleResponse(response));
		String accessToken=(String) json.get("access_token");
		//System.out.println(accessToken);
		return accessToken;
	}

	//Return a list of all contracts
	@GetMapping(path="/allcontracts",produces=MediaType.APPLICATION_JSON_VALUE)
	public String getAllContracts() throws HttpException, IOException{

		org.apache.commons.httpclient.HttpClient httpclient = new org.apache.commons.httpclient.HttpClient();

		GetMethod get = new GetMethod(LOGINURL + "/services/data/v42.0/query");
		
		String accessToken = oAuthSessionProvider();
		
		get.setRequestHeader("Authorization", "OAuth " + accessToken);

		// set the SOQL as a query param
		NameValuePair[] params = new NameValuePair[1];

		params[0] = new NameValuePair("q",
				"Select Id,ContractId__c, Name, ContractActivationDate__c, Status__c, AmountRequested__c , BusinessNumber__c from Contract__c");
		get.setQueryString(params);

		httpclient.executeMethod(get);
		String jsonResponse = get.getResponseBodyAsString();
		return jsonResponse;
	}
	//Return a list of all contracts by status (approved/denied)
	@GetMapping(path="/contracts",produces=MediaType.APPLICATION_JSON_VALUE)
	public String getContractByStatus(@RequestParam("status") String status) throws HttpException, IOException{

		org.apache.commons.httpclient.HttpClient httpclient = new org.apache.commons.httpclient.HttpClient();
		GetMethod get = new GetMethod(LOGINURL + "/services/data/v32.0/query");
		String accessToken = oAuthSessionProvider();
		get.setRequestHeader("Authorization", "OAuth " + accessToken);

		// set the SOQL as a query param
		NameValuePair[] params = new NameValuePair[1];

		params[0] = new NameValuePair("q",
				"Select Id,ContractId__c, Name, ContractActivationDate__c, Status__c, AmountRequested__c , BusinessNumber__c from Contract__C where Status__c='"+status.trim()+"'");
		get.setQueryString(params);

		httpclient.executeMethod(get);
		String jsonResponse = get.getResponseBodyAsString();
		//System.out.println("HTTP " + String.valueOf(httpclient) + ": " + jsonResponse);
		return jsonResponse;
	}
	//Return a single contract give a contract number
	@GetMapping(path="/contracts/{contractNumber}",produces=MediaType.APPLICATION_JSON_VALUE)
	public String getContractByContractNumber(@PathVariable("contractNumber") String contractNumber) throws HttpException, IOException{
		org.apache.commons.httpclient.HttpClient httpclient = new org.apache.commons.httpclient.HttpClient();
		GetMethod get = new GetMethod(LOGINURL + "/services/data/v42.0/query");
		String accessToken = oAuthSessionProvider();
		get.setRequestHeader("Authorization", "OAuth " + accessToken);

		// set the SOQL as a query param
		NameValuePair[] params = new NameValuePair[1];

		params[0] = new NameValuePair("q",
				"Select Id,ContractId__c, Name, ContractActivationDate__c, Status__c, AmountRequested__c , BusinessNumber__c from Contract__c where ContractId__c='"+contractNumber.trim()+"'");
		get.setQueryString(params);

		httpclient.executeMethod(get);
		String jsonResponse = get.getResponseBodyAsString();
		//System.out.println("HTTP " + String.valueOf(httpclient) + ": " + jsonResponse);
		return jsonResponse;
	}

	//Create a new contract
	@PostMapping(path="/contracts",consumes=MediaType.APPLICATION_JSON_VALUE)
	public String createContract(@RequestBody String contract) throws HttpException, IOException{
		try {

			//System.out.println(" record to be inserted:\n" + contract);

			//Construct the objects needed for the request
			HttpClient httpClient = HttpClientBuilder.create().build();

			HttpPost httpPost = new HttpPost(LOGINURL + "/services/data/v42.0/sobjects/Contract__c/");
			String accessToken = oAuthSessionProvider();
			Header oauthHeader = new BasicHeader("Authorization", "OAuth " + accessToken) ;
			httpPost.addHeader(oauthHeader);

			// The contract we are going to post
			StringEntity body = new StringEntity(contract);
			body.setContentType("application/json");
			httpPost.setEntity(body);

			//Make the request
			HttpResponse response = httpClient.execute(httpPost);

			//Process the results
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 201) {
				String response_string = EntityUtils.toString(response.getEntity());
				JSONObject json = new JSONObject(response_string);
				String contractId = json.getString("id");
				//System.out.println("New contract id from response: " + contractId);
				return contractId;
			} else {
				System.out.println("Insertion unsuccessful. Status code returned is " + statusCode);
			}
			
		} catch (JSONException e) {
			System.out.println("Issue creating JSON or processing results");
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		}
		return "";
	}
	
	//Update an existing contract
	@PutMapping(path="/contracts/{id}",consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateContract(@RequestBody String contract,@PathVariable("id") String id) throws HttpException, IOException{
		try {

			//System.out.println(" record to be updated:\n" + contract);

			//Construct the objects needed for the request
			HttpClient httpClient = HttpClientBuilder.create().build();

			HttpPatch httpPatch = new HttpPatch(LOGINURL + "/services/data/v42.0/sobjects/Contract__c/"+id);
			String accessToken = oAuthSessionProvider();
			Header oauthHeader = new BasicHeader("Authorization", "OAuth " + accessToken) ;
			httpPatch.addHeader(oauthHeader);
			StringEntity body = new StringEntity(contract);
			body.setContentType("application/json");
			httpPatch.setEntity(body);

			//Make the request
			HttpResponse response = httpClient.execute(httpPatch);

			//Process the response
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 204) {
				System.out.println("Updated the contract successfully.");
			} else {
				System.out.println("Contract update failed. Status code is " + statusCode);
			}
		} catch (JSONException e) {
			System.out.println("Issue creating JSON or processing results");
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		}
	}
	
	// Delete an existing contract
	@DeleteMapping(path="/contracts/{id}")
	public void deleteContract(@PathVariable("id") String id) throws HttpException, IOException{
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();

			HttpDelete httpDelete = new HttpDelete(LOGINURL + "/services/data/v42.0/sobjects/Contract__c/"+id);
			String accessToken = oAuthSessionProvider();
			Header oauthHeader = new BasicHeader("Authorization", "OAuth " + accessToken) ;
			httpDelete.addHeader(oauthHeader);;

			//Make the request
			HttpResponse response = httpClient.execute(httpDelete);

			//Process the response
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 204) {
				System.out.println("Deleted the contract successfully.");
			} else {
				System.out.println("Contract delete failed. Status code is " + statusCode);
			}
		} catch (JSONException e) {
			System.out.println("Issue creating JSON or processing results");
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		}
	}
	// Extend the Apache HttpPost method to implement an HttpPatch
	private static class HttpPatch extends HttpPost {
		public HttpPatch(String uri) {
			super(uri);
		}

		public String getMethod() {
			return "PATCH";
		}
	}
}
