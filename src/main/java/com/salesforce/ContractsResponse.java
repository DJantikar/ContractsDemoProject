package com.salesforce;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContractsResponse implements Serializable{
	private static final long serialVersionUID = 1234567898797077098L;
	@JsonProperty("totalSize")
	private int totalSize;
	@JsonProperty("done")
	private boolean done;
	@JsonProperty("records")
	private List<Contract> records;
	public ContractsResponse() {
		super();
	}
	public int getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}
	public boolean isDone() {
		return done;
	}
	public void setDone(boolean done) {
		this.done = done;
	}
	public List<Contract> getRecords() {
		return records;
	}
	public void setRecords(List<Contract> records) {
		this.records = records;
	}

}
