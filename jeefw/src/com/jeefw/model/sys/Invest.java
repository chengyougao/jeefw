package com.jeefw.model.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.google.common.base.Objects;
import com.jeefw.model.sys.param.InvestParameter;

/**
 * 投资信息
 */
@Entity
@Table(name = "invest")
@Cache(region = "all", usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(value = { "maxResults", "firstResult", "topCount",
		"sortColumns", "cmd", "queryDynamicConditions", "sortedConditions",
		"dynamicProperties", "success", "message", "sortColumnsString", "flag" })
public class Invest extends InvestParameter {
	private static final long serialVersionUID = 1727967820584595526L;
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	/**
	 * 投资人信息
	 */
	@Column(name = "investor", length = 50, nullable = false, unique = true)
	private String investor;

	@Column(name = "identityId", length = 40, nullable = true)
	private String identityId;

	@Column(name = "phone", length = 40, nullable = true)
	private String phone;

	/**
	 * 投资人邮箱
	 */
	@Column(name = "email", length = 40, nullable = true)
	private String email;
	/**
	 * 投资期限
	 */
	@Column(name = "investTerm", length = 4, nullable = true)
	private int investTerm;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 投资金额
	 */
	@Column(name = "investAmount", length = 12, nullable = true)
	private double investAmount;

	/**
	 * 投资起始日期
	 */
	@Column(name = "investDate", length = 40, nullable = true)
	private String investDate;
	/**
	 * 投资到期日期
	 */
	@Column(name = "investDateEnd", length = 40, nullable = true)
	private String investDateEnd;

	/**
	 * 投资产品
	 */
	@Column(name = "investProdut", length = 255, nullable = true)
	private String investProdut;

	/**
	 * 审核投递状态 0：审核未通过 1：审核通过（默认）
	 */
	@Column(name = "status", length = 1, nullable = true)
	private int status;
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getInvestDateEnd() {
		return investDateEnd;
	}

	public void setInvestDateEnd(String investDateEnd) {
		this.investDateEnd = investDateEnd;
	}

	/**
	 * 导入人ID
	 */
	@Column(name = "importPeId", length = 40, nullable = true)
	private String importPeId;

	/**
	 * 导入时间
	 */
	@Column(name = "importDate", length = 40, nullable = true)
	private String importDate;

	private double totalAmount;

	private double guding;// 初始于投資金額相等，固定不變，投資金額在匹配資產期限相等的情況下可能變更investAmount值

	public String getImportPeId() {
		return importPeId;
	}

	public void setImportPeId(String importPeId) {
		this.importPeId = importPeId;
	}

	public String getImportDate() {
		return importDate;
	}

	public void setImportDate(String importDate) {
		this.importDate = importDate;
	}

	public Long getId() {
		return id;
	}

	public String getInvestProdut() {
		return investProdut;
	}

	public void setInvestProdut(String investProdut) {
		this.investProdut = investProdut;
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

	public String getIdentityId() {
		return identityId;
	}

	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getInvestTerm() {
		return investTerm;
	}

	public void setInvestTerm(int investTerm) {
		this.investTerm = investTerm;
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Invest other = (Invest) obj;
		return Objects.equal(this.id, other.id)
				&& Objects.equal(this.investAmount, other.investAmount)
				&& Objects.equal(this.investDate, other.investDate)
				&& Objects.equal(this.investProdut, other.investProdut);
	}

	public int hashCode() {
		return Objects.hashCode(this.id, this.investAmount, this.investDate,
				this.investProdut);
	}


	public void setInvestAmount(int investAmount) {
		this.investAmount = investAmount;
	}

	
	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getInvestDate() {
		return investDate;
	}

	public void setInvestDate(String investDate) {
		this.investDate = investDate;
	}

	public double getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(double investAmount) {
		this.investAmount = investAmount;
	}

	public double getGuding() {
		return guding;
	}

	public void setGuding(double guding) {
		this.guding = guding;
	}
    
}
