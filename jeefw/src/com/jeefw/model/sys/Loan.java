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
import com.jeefw.model.sys.param.LoanParameter;

/**
 * 借款信息
 */
@Entity
@Table(name = "loan")
@Cache(region = "all", usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(value = { "maxResults", "firstResult", "topCount", "sortColumns", "cmd", "queryDynamicConditions", "sortedConditions", "dynamicProperties", "success", "message", "sortColumnsString", "flag" })
public class Loan extends LoanParameter {

	// 各个字段的含义请查阅文档的数据库结构部分
	private static final long serialVersionUID = -2847988368314678488L;
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	@Column(name = "loanName", length = 50, nullable = false, unique = true)
	private String loanName;
	@Column(name = "borrower", length = 40, nullable = true)
	private String borrower;
	@Column(name = "identityId", length = 40, nullable = true)
	private String identityId;	
	@Column(name = "phone", length = 40, nullable = true)
	private String phone;	
	/**
	 * 借款期限
	 */
	@Column(name = "loanTerm", length = 4, nullable = true)
	private int loanTerm;	
	/**
	 * 借款金额
	 */
	@Column(name = "loanAmount", length = 12, nullable = true)
	private double loanAmount;	
	
	/**
	 * 放款日期
	 */
	@Column(name = "loanDate", length = 40, nullable = true)
	private String loanDate;
	
	/**
	 * 借款到期日期
	 */
	@Column(name = "loanDateEnd", length = 40, nullable = true)
	private String loanDateEnd;
	
	

	/**
	 * 借款用途
	 */
	@Column(name = "loanPurpose", length = 40, nullable = true)
	private String loanPurpose;
	
	
	/**
	 * 借款周期 天
	 */
	@Column(name = "loanDays", length = 4, nullable = true)
	private int loanDays;
	
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
	
	private Long totalAmount;
		
	
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


	public String getLoanDateEnd() {
		return loanDateEnd;
	}

	public void setLoanDateEnd(String loanDateEnd) {
		this.loanDateEnd = loanDateEnd;
	}

	public String getLoanPurpose() {
		return loanPurpose;
	}

	public void setLoanPurpose(String loanPurpose) {
		this.loanPurpose = loanPurpose;
	}

	public int getLoanDays() {
		return loanDays;
	}

	public void setLoanDays(int loanDays) {
		this.loanDays = loanDays;
	}
	public Loan() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
    

	public String getLoanName() {
		return loanName;
	}

	public void setLoanName(String loanName) {
		this.loanName = loanName;
	}

	public String getBorrower() {
		return borrower;
	}

	public void setBorrower(String borrower) {
		this.borrower = borrower;
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
	
	public int getLoanTerm() {
		return loanTerm;
	}

	public void setLoanTerm(int loanTerm) {
		this.loanTerm = loanTerm;
	}

	
	public double getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(double loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getLoanDate() {
		return loanDate;
	}

	public void setLoanDate(String loanDate) {
		this.loanDate = loanDate;
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Loan other = (Loan) obj;
		return Objects.equal(this.id, other.id)&& Objects.equal(this.loanAmount, other.loanAmount) && Objects.equal(this.loanName, other.loanName) && Objects.equal(this.identityId, other.identityId)
			;
	}
    
	public Long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int hashCode() {
		return Objects.hashCode(this.id, this.identityId, this.loanAmount);
	}

}