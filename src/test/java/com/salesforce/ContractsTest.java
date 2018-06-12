package com.salesforce;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(ContractsController.class)
public class ContractsTest {

	@Autowired
	private MockMvc mockMvc ;	

	@Test
	public void testAuthentication() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/authentication").accept(MediaType.APPLICATION_JSON);
		MvcResult result  = mockMvc.perform(request).
				andExpect(status().isOk()).
				andReturn();
		assertThat(result.getResponse()).isNotNull();
		assertThat(result.getResponse().getContentAsString()).isNotBlank();
	}

	@Test
	public void testGetAllContracts() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/allcontracts").accept(MediaType.APPLICATION_JSON);
		MvcResult result  = mockMvc.perform(request).
				andExpect(status().isOk()).
				andReturn();
		assertThat(result.getResponse()).isNotNull();
		assertThat(result.getResponse().getContentAsString()).isNotBlank();
		ObjectMapper objectMapper = new ObjectMapper();
		ContractsResponse contractsResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ContractsResponse.class);
		assertThat(contractsResponse).isNotNull();
		assertThat(contractsResponse.getRecords().size()).isNotNegative();
	}
	@Test
	public void testGetContractByStatus() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/contracts?status=Approved").accept(MediaType.APPLICATION_JSON);
		MvcResult result  = mockMvc.perform(request).
				andExpect(status().isOk()).
				andReturn();
		assertThat(result.getResponse()).isNotNull();
		assertThat(result.getResponse().getContentAsString()).isNotBlank();
		ObjectMapper objectMapper = new ObjectMapper();
		ContractsResponse contractsResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ContractsResponse.class);
		assertThat(contractsResponse).isNotNull();
		assertThat(contractsResponse.getRecords().get(0).getStatus__c()).isNotBlank();
		assertEquals("Approved",contractsResponse.getRecords().get(0).getStatus__c());
	}
	@Test
	public void testGetContractByIncorrectContractNumber() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/contracts/1000").accept(MediaType.APPLICATION_JSON);
		MvcResult result  = mockMvc.perform(request).
				andExpect(status().isOk()).
				andReturn();
		assertThat(result.getResponse()).isNotNull();
		assertThat(result.getResponse().getContentAsString()).isNotBlank();
		ObjectMapper objectMapper = new ObjectMapper();
		ContractsResponse contractsResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ContractsResponse.class);
		assertThat(contractsResponse).isNotNull();
		assertThat(contractsResponse.getRecords()).isEmpty();
	}
	@Test
	public void testGetContractByCorrectContractNumber() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/contracts/10001").accept(MediaType.APPLICATION_JSON);
		MvcResult result  = mockMvc.perform(request).
				andExpect(status().isOk()).
				andReturn();
		assertThat(result.getResponse()).isNotNull();
		assertThat(result.getResponse().getContentAsString()).isNotBlank();
		ObjectMapper objectMapper = new ObjectMapper();
		ContractsResponse contractsResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ContractsResponse.class);
		assertThat(contractsResponse).isNotNull();
		assertThat(contractsResponse.getRecords()).isNotEmpty();
		assertEquals("10001",contractsResponse.getRecords().get(0).getContractId__c());
	}
	@Test
	public void testCreateContract() throws Exception {
		
		Contract c = new Contract();
		c.setName("ee");
		c.setAmountRequested__c("50000");
		c.setBusinessNumber__c("123456");
		c.setContractActivationDate__c("06-18-2018");
		c.setContractId__c("10007");
		c.setStatus__c("Approved");
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = objectMapper.writeValueAsString(c);
		RequestBuilder request = MockMvcRequestBuilders.post("/contracts").contentType(MediaType.APPLICATION_JSON).content(jsonString);
		MvcResult result  = mockMvc.perform(request).
				andExpect(status().isOk()).
				andReturn();
		assertThat(result.getResponse()).isNotNull();
		assertThat(result.getResponse().getContentAsString()).isNotBlank();
	}

	@Test
	public void testDeleteContract() throws Exception {
	
		RequestBuilder request = MockMvcRequestBuilders.get("/contracts/10007").accept(MediaType.APPLICATION_JSON);
		MvcResult result  = mockMvc.perform(request).
				andExpect(status().isOk()).
				andReturn();
		assertThat(result.getResponse()).isNotNull();
		assertThat(result.getResponse().getContentAsString()).isNotBlank();
	    ObjectMapper objectMapper = new ObjectMapper();
		ContractsResponse contractsResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ContractsResponse.class);
		assertThat(contractsResponse).isNotNull();
		assertThat(contractsResponse.getRecords()).isNotEmpty();
		assertEquals("10007",contractsResponse.getRecords().get(0).getContractId__c());
		String id = contractsResponse.getRecords().get(0).getId();
	    request = MockMvcRequestBuilders.delete("/contracts/"+id).accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(request).
				andExpect(status().isOk()).
				andReturn();

	}
	
	@Test
	public void testUpdateContract() throws Exception {
		
		Contract c = new Contract();
		c.setName("test account updated");
		c.setAmountRequested__c("50000");
		c.setBusinessNumber__c("123456");
		c.setContractActivationDate__c("06-18-2008");
		c.setContractId__c("99");
		c.setStatus__c("Denied");
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = objectMapper.writeValueAsString(c);
		
		RequestBuilder request = MockMvcRequestBuilders.get("/contracts/99").accept(MediaType.APPLICATION_JSON);
		MvcResult result  = mockMvc.perform(request).
				andExpect(status().isOk()).
				andReturn();
		assertThat(result.getResponse()).isNotNull();
		assertThat(result.getResponse().getContentAsString()).isNotBlank();
	    objectMapper = new ObjectMapper();
		ContractsResponse contractsResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ContractsResponse.class);
		assertThat(contractsResponse).isNotNull();
		assertThat(contractsResponse.getRecords()).isNotEmpty();
		assertEquals("99",contractsResponse.getRecords().get(0).getContractId__c());
		String id = contractsResponse.getRecords().get(0).getId();
	    request = MockMvcRequestBuilders.put("/contracts/"+id).contentType(MediaType.APPLICATION_JSON).
				content(jsonString);
		mockMvc.perform(request).
				andExpect(status().isOk()).
				andReturn();

	}
}
