package com.salesforce;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Contract implements Serializable{
	private static final long serialVersionUID = 1234567898797077088L;
	@JsonProperty("attributes")
	private Object attributes;
	@JsonProperty("Id")
	private String Id;
	@JsonProperty("ContractId__c")
	private String ContractId__c;
	@JsonProperty("Name")
	private String Name;
	@JsonProperty("ContractActivationDate__c")
	private String ContractActivationDate__c;
	@JsonProperty("Status__c")
	private String Status__c;
	@JsonProperty("AmountRequested__c")
	private String AmountRequested__c;
	@JsonProperty("BusinessNumber__c")
	private String BusinessNumber__c;
	
	public Object getAttributes() {
		return attributes;
	}
	public void setAttributes(Object attributes) {
		this.attributes = attributes;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getContractId__c() {
		return ContractId__c;
	}
	public void setContractId__c(String contractId__c) {
		ContractId__c = contractId__c;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getContractActivationDate__c() {
		return ContractActivationDate__c;
	}
	public void setContractActivationDate__c(String contractActivationDate__c) {
		ContractActivationDate__c = contractActivationDate__c;
	}
	public String getStatus__c() {
		return Status__c;
	}
	public void setStatus__c(String status__c) {
		Status__c = status__c;
	}
	public String getAmountRequested__c() {
		return AmountRequested__c;
	}
	public void setAmountRequested__c(String amountRequested__c) {
		AmountRequested__c = amountRequested__c;
	}
	public String getBusinessNumber__c() {
		return BusinessNumber__c;
	}
	public void setBusinessNumber__c(String businessNumber__c) {
		BusinessNumber__c = businessNumber__c;
	}
	public Contract() {
		super();
	}
}
