package core.pdf.bean;

import java.util.List;

import core.pdf.AbstractDocumentVo;


/**
 * 债权转让与受让详情 CreditTransferVO
 */
public  class CreditTransferVO extends AbstractDocumentVo {
	
	/**
	 * 报告时间
	 */
	private String 	reportTime;
	/**
	 * 出让人姓名
	 */
	private String transferorName;
	/**
	 * 出让人身份证号
	 */
	private String transferorIdentityId;
	/**
	 * 受让人姓名
	 */
	private String assigneeName;
	/**
	 * 受让人身份证号
	 */
	private String assigneeIdentityId;
	
	
	/**
	 * 受让金额
	 */
	private int assigneeMoney;
	/**
	 * 图片存放路径
	 */
	private String imagePath;
	/**
	 *  债务人基本信息和债权收益信息
	 */
	private List<CreditAssigneeVO> creditAssigneeList;
	/**
	 * 数据信息
	 */
	private String ids;
	
	/**
	 *投资期限
	 */
	private String assigneeTmer;
	/**
	 * 投资起期
	 */
	private String assigneeDate;
	/**
	 * 产品
	 */
	private String product;
	/**
	 * //0:已复核；1复核未发送邮件；2 复核已发邮件
	 */
	private  int status;
	/**
	 * 邮件投递状态 0：未发送（默认）1：已发送
	 */
	private int mailStatus;
	/**
	 * 客户email
	 */
	private String mail;
	/**
	 * 客户电话
	 */
	private String customtel;
	
	
	
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public List<CreditAssigneeVO> getCreditAssigneeList() {
		return creditAssigneeList;
	}
	public void setCreditAssigneeList(List<CreditAssigneeVO> creditAssigneeList) {
		this.creditAssigneeList = creditAssigneeList;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	public String getTransferorName() {
		return transferorName;
	}
	public void setTransferorName(String transferorName) {
		this.transferorName = transferorName;
	}
	public String getTransferorIdentityId() {
		return transferorIdentityId;
	}
	public void setTransferorIdentityId(String transferorIdentityId) {
		this.transferorIdentityId = transferorIdentityId;
	}
	public String getAssigneeName() {
		return assigneeName;
	}
	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}
	public String getAssigneeIdentityId() {
		return assigneeIdentityId;
	}
	public void setAssigneeIdentityId(String assigneeIdentityId) {
		this.assigneeIdentityId = assigneeIdentityId;
	}
	public int getAssigneeMoney() {
		return assigneeMoney;
	}
	public void setAssigneeMoney(int assigneeMoney) {
		this.assigneeMoney = assigneeMoney;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getAssigneeTmer() {
		return assigneeTmer;
	}
	public void setAssigneeTmer(String assigneeTmer) {
		this.assigneeTmer = assigneeTmer;
	}
	public String getAssigneeDate() {
		return assigneeDate;
	}
	public void setAssigneeDate(String assigneeDate) {
		this.assigneeDate = assigneeDate;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getCustomtel() {
		return customtel;
	}
	public void setCustomtel(String customtel) {
		this.customtel = customtel;
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
	@Override
	public String findPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
