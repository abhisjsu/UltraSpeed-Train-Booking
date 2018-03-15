package com.cmpe275.cusr.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Cancelledtrains {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	Long cancelID;
	String name;
	String date;
	
	

	public Cancelledtrains() {
		super();
	}

	public Cancelledtrains(String name, String date) {
		this.name = name;
		this.date = date;
	}

	public Long getCancelID() {
		return cancelID;
	}

	public void setCancelID(Long cancelID) {
		this.cancelID = cancelID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
