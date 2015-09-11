package com.jeefw.model.sys;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.google.common.base.Objects;

import core.support.ExtJSBaseParameter;

/**
 * 用户的实体类
 */

@Cache(region = "all", usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(value = { "maxResults", "firstResult", "topCount", "sortColumns", "cmd", "queryDynamicConditions", "sortedConditions", "dynamicProperties", "success", "message", "sortColumnsString", "flag" })
public class CapitalHistory extends ExtJSBaseParameter {

	private static final long serialVersionUID = 9042740588920716370L;

	private Long id;

	private String investor;

	private int inveSum;

	private String inveEndDate;

	private String investProdut;

	private int loanSum;

	private int investterm;

	private String investdate;
	
	private int loanterm;

	private String loanDate;

	private String loadEndDate;

	public int getInvestterm() {
		return investterm;
	}

	public void setInvestterm(int investterm) {
		this.investterm = investterm;
	}

	public String getInvestdate() {
		return investdate;
	}

	public void setInvestdate(String investdate) {
		this.investdate = investdate;
	}

	public int getLoanterm() {
		return loanterm;
	}

	public void setLoanterm(int loanterm) {
		this.loanterm = loanterm;
	}

	public String getLoanDate() {
		return loanDate;
	}

	public void setLoanDate(String loanDate) {
		this.loanDate = loanDate;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInvestor() {
		return investor;
	}

	public void setInvestor(String investor) {
		this.investor = investor;
	}

	public int getInveSum() {
		return inveSum;
	}

	public void setInveSum(int inveSum) {
		this.inveSum = inveSum;
	}

	public String getInveEndDate() {
		return inveEndDate;
	}

	public void setInveEndDate(String inveEndDate) {
		this.inveEndDate = inveEndDate;
	}

	public String getInvestProdut() {
		return investProdut;
	}

	public void setInvestProdut(String investProdut) {
		this.investProdut = investProdut;
	}

	public int getLoanSum() {
		return loanSum;
	}

	public void setLoanSum(int loanSum) {
		this.loanSum = loanSum;
	}

	public String getLoadEndDate() {
		return loadEndDate;
	}

	public void setLoadEndDate(String loadEndDate) {
		this.loadEndDate = loadEndDate;
	}

	// 各个字段的含义请查阅文档的数据库结构部分
	/*
	 * @Id
	 * 
	 * @GeneratedValue
	 * 
	 * @Column(name = "id") private Long id;
	 * 
	 * @Column(name = "investor", length = 40, nullable = false) private String
	 * investor;
	 * 
	 * @Column(name = "investProdut") private String investProdut;
	 * 
	 * @Column(name = "investdate") private String investdate;
	 * 
	 * @Column(name = "investterm") private String investterm;
	 * 
	 * private String inveEndDate;
	 * 
	 * public String getInveEndDate() { return inveEndDate; }
	 * 
	 * public void setInveEndDate(String inveEndDate) { this.inveEndDate =
	 * inveEndDate; }
	 * 
	 * public String getInvestdate() { return investdate; }
	 * 
	 * public void setInvestdate(String investdate) { this.investdate =
	 * investdate; }
	 * 
	 * public String getInvestterm() { return investterm; }
	 * 
	 * public void setInvestterm(String investterm) { this.investterm =
	 * investterm; }
	 * 
	 * public String getInvestor() { return investor; }
	 * 
	 * public void setInvestor(String investor) { this.investor = investor; }
	 * 
	 * public String getInvestProdut() { return investProdut; }
	 * 
	 * public void setInvestProdut(String investProdut) { this.investProdut =
	 * investProdut; }
	 * 
	 * public Long getId() { return id; }
	 * 
	 * public void setId(Long id) { this.id = id; }
	 * 
	 * public CapitalHistory() {
	 * 
	 * }
	 */

	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final CapitalHistory other = (CapitalHistory) obj;
		return Objects.equal(this.id, other.id)
				&& Objects.equal(this.investor, other.investor)
				&& Objects.equal(this.investProdut, other.investProdut)
				&& Objects.equal(this.investdate, other.investdate)
				&& Objects.equal(this.investterm, other.investterm)
				&& Objects.equal(this.inveSum, other.inveSum)
				&& Objects.equal(this.inveEndDate, other.inveEndDate)
				&& Objects.equal(this.loanSum, other.loanSum)
				&& Objects.equal(this.loadEndDate, other.loadEndDate)
				&& Objects.equal(this.loanterm, other.loanterm)
				&& Objects.equal(this.loanDate, other.loanDate);

	}

	public int hashCode() {
		return Objects.hashCode(this.id, this.investor, this.investProdut,
				this.investdate, this.investterm, this.inveSum,
				this.inveEndDate, this.loanSum, this.loadEndDate,
				this.loanterm, this.loanDate);
	}

}