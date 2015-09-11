package com.jeefw.model.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 错配信息表
 * @author pfzeng
 *
 */
@Entity
@Table(name = "t_mismatch")
public class Mismatch {
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	
	@Column(name = "invesId")
	private long invesId;
	
	private long loanId;
	
	private double misAmount;
	
	private int state;
	
	private int invesDays;
	
	private int loanDays;
	
	private String eropDate;
	//0:已复核；1复核未发送邮件；2复核已发邮件
	@Column(name = "status")
	private int status;
	
	private String date;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public long getInvesId() {
		return invesId;
	}

	public void setInvesId(long invesId) {
		this.invesId = invesId;
	}

	public long getLoanId() {
		return loanId;
	}

	public void setLoanId(long loanId) {
		this.loanId = loanId;
	}
	

	public double getMisAmount() {
		return misAmount;
	}

	public void setMisAmount(double misAmount) {
		this.misAmount = misAmount;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getInvesDays() {
		return invesDays;
	}

	public void setInvesDays(int invesDays) {
		this.invesDays = invesDays;
	}

	public int getLoanDays() {
		return loanDays;
	}

	public void setLoanDays(int loanDays) {
		this.loanDays = loanDays;
	}

	public String getEropDate() {
		return eropDate;
	}

	public void setEropDate(String eropDate) {
		this.eropDate = eropDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
