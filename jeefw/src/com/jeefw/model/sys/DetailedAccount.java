package com.jeefw.model.sys;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import core.support.ExtJSBaseParameter;

@Cache(region = "all", usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(value = { "maxResults", "firstResult", "topCount",
		"sortColumns", "cmd", "queryDynamicConditions", "sortedConditions",
		"dynamicProperties", "success", "message", "sortColumnsString", "flag" })
public class DetailedAccount extends ExtJSBaseParameter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7491720200695689895L;
	private Long id;
	private String investor;
	private String investorID;
	private int investMoney;
	private int investorterm;
	private String investDate;
	private String product;
	private String loancompany;
	private int loanMoney;
	private int status;
	private int mailStatus;
	private Long mismatchID;
	private Long detailAccountID;
	private String detailAccountTime;

	public Long getDetailAccountID() {
		return detailAccountID;
	}

	public void setDetailAccountID(Long detailAccountID) {
		this.detailAccountID = detailAccountID;
	}

	public String getDetailAccountTime() {
		return detailAccountTime;
	}

	public void setDetailAccountTime(String detailAccountTime) {
		this.detailAccountTime = detailAccountTime;
	}

	
	public Long getMismatchID() {
		return mismatchID;
	}

	public void setMismatchID(Long mismatchID) {
		this.mismatchID = mismatchID;
	}

	public Long getInvID() {
		return id;
	}

	public void setInvID(Long id) {
		this.id = id;
	}

	public String getInvestor() {
		return investor;
	}

	public void setInvestor(String investor) {
		this.investor = investor;
	}

	public String getInvestorID() {
		return investorID;
	}

	public void setInvestorID(String investorID) {
		this.investorID = investorID;
	}

	public int getInvestMoney() {
		return investMoney;
	}

	public void setInvestMoney(int investMoney) {
		this.investMoney = investMoney;
	}

	public int getInvestorterm() {
		return investorterm;
	}

	public void setInvestorterm(int investorterm) {
		this.investorterm = investorterm;
	}

	public String getInvestDate() {
		return investDate;
	}

	public void setInvestDate(String investDate) {
		this.investDate = investDate;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getLoancompany() {
		return loancompany;
	}

	public void setLoancompany(String loancompany) {
		this.loancompany = loancompany;
	}

	public int getLoanMoney() {
		return loanMoney;
	}

	public void setLoanMoney(int loanMoney) {
		this.loanMoney = loanMoney;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getMailStatus() {
		return mailStatus;
	}

	public void setMailStatus(int mailStatus) {
		this.mailStatus = mailStatus;
	}

}
