package core.pdf.bean;

import core.pdf.AbstractDocumentVo;


/**
 *  债务人基本信息和债权收益信息CreditAssigneeVO
 */
public  class CreditAssigneeVO extends AbstractDocumentVo{  
	
	/**
	 * 借款人
	 */
	private String 	loanName;
	/**
	 * 借款人身份证号
	 */
	private String identityId;
	/**
	 * 原始借款额
	 */
	private int loanamountBegin;
	/**
	 * 本次转让价值
	 */
	private int  transferValue;
	/**
	 * 需支付对价
	 */
	private int pricePay;
	
	/**
	 * 借款人个人情况
	 */
	private String loanSituation;
	/**
	 * 借款人价款用途
	 */
	private String loanpurpose;
	/**
	 * 还款起始日期
	 */
	private String repaymentDate;
	/**
	 * 还款期限（月）
	 */
	private int repaymentTerm;
	/**
	 * 剩余还款月数
	 */
	private int repaymentTermRemain;
	
	public String getLoanName() {
		return loanName;
	}
	public void setLoanName(String loanName) {
		this.loanName = loanName;
	}
	public String getIdentityId() {
		return identityId;
	}
	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}
	public int getLoanamountBegin() {
		return loanamountBegin;
	}
	public void setLoanamountBegin(int loanamountBegin) {
		this.loanamountBegin = loanamountBegin;
	}
	public int getTransferValue() {
		return transferValue;
	}
	public void setTransferValue(int transferValue) {
		this.transferValue = transferValue;
	}
	public int getPricePay() {
		return pricePay;
	}
	public void setPricePay(int pricePay) {
		this.pricePay = pricePay;
	}
	public String getLoanSituation() {
		return loanSituation;
	}
	public void setLoanSituation(String loanSituation) {
		this.loanSituation = loanSituation;
	}
	public String getLoanpurpose() {
		return loanpurpose;
	}
	public void setLoanpurpose(String loanpurpose) {
		this.loanpurpose = loanpurpose;
	}
	public String getRepaymentDate() {
		return repaymentDate;
	}
	public void setRepaymentDate(String repaymentDate) {
		this.repaymentDate = repaymentDate;
	}
	public int getRepaymentTerm() {
		return repaymentTerm;
	}
	public void setRepaymentTerm(int repaymentTerm) {
		this.repaymentTerm = repaymentTerm;
	}
	public int getRepaymentTermRemain() {
		return repaymentTermRemain;
	}
	public void setRepaymentTermRemain(int repaymentTermRemain) {
		this.repaymentTermRemain = repaymentTermRemain;
	}
	@Override
	public String findPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
