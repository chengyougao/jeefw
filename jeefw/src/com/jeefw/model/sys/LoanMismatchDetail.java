package com.jeefw.model.sys;

/**
 * 借款错配明细
 * 
 * @author pfzeng
 * 
 */
public class LoanMismatchDetail {

	private long id;

	private long loanId;

	/**
	 * 投资错配明细Id
	 */
	private long invesMisId;

	/**
	 * 错配金额
	 */
	private long misMoney;

	/**
	 * 状态 1 过期 0 正常
	 */
	private int status;

	/**
	 * 过期时间
	 */
	private String expirTime;

	/**
	 * 天数
	 */
	private int days;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLoanId() {
		return loanId;
	}

	public void setLoanId(long loanId) {
		this.loanId = loanId;
	}

	public long getInvesMisId() {
		return invesMisId;
	}

	public void setInvesMisId(long invesMisId) {
		this.invesMisId = invesMisId;
	}

	public double getMisMoney() {
		return misMoney;
	}

   
	public void setMisMoney(long misMoney) {
		this.misMoney = misMoney;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getExpirTime() {
		return expirTime;
	}

	public void setExpirTime(String expirTime) {
		this.expirTime = expirTime;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

}
