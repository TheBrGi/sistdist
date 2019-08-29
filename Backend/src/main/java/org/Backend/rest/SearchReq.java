package org.Backend.rest;

import java.util.Date;

import org.Backend.model.Categoria;

public class SearchReq {
	private String da;
	private String a;
	private Date dal;
	private Categoria cat;
	public String getDa() {
		return da;
	}
	public void setDa(String da) {
		this.da = da;
	}
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	public Date getDal() {
		return dal;
	}
	public void setDal(Date dal) {
		this.dal = dal;
	}
	public Categoria getCat() {
		return cat;
	}
	public void setCat(Categoria cat) {
		this.cat = cat;
	}

}
