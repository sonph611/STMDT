package com.API.model;


import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "test")
public class tests {
	@Id
	private Integer t;
	@Temporal(TemporalType.TIMESTAMP)
	private Date tt;
	public int getT() {
		return t;
	}
	public void setT(int t) {
		this.t = t;
	}
	public Date getTt() {
		return tt;
	}
	public void setTt(Date tt) {
		this.tt = tt;
	}
}
