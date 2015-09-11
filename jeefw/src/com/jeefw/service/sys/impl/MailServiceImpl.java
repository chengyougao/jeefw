package com.jeefw.service.sys.impl;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.jeefw.dao.jdbc.JdbcBaseDao;
import com.jeefw.dao.sys.MailDao;
import com.jeefw.model.sys.Mail;
import com.jeefw.service.sys.MailService;

import core.pdf.bean.CreditAssigneeVO;
import core.pdf.bean.CreditTransferVO;
import core.service.BaseService;

/**
 * 邮件的业务逻辑层的实现
 */
@Service
public class MailServiceImpl extends BaseService<Mail> implements MailService {
	@Resource
	private JavaMailSenderImpl emailProper;
	
	@Resource
	private JdbcBaseDao jdbcBaseDao;
	
	@Resource
	public void setMailDao(MailDao mailDao) {
		this.dao = mailDao;
	}
	
	@Override
	public List<CreditTransferVO> getCreditTransferVO(String[] mess) {
		List<String> list=new ArrayList<String>();
		List<CreditTransferVO> creditTransferList = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select a.investor as assigneeName,a.identityId as assigneeIdentityId,a.investamount as assigneeMoney,a.investTerm as assigneeTmer,a.investDate as assigneeDate,a.investProdut as product ,a.email as mail ,a.phone as customtel,a.id as ids,");
		sql.append("c.borrower ,c.identityId,c.loanamount,b.misAmount,c.loanpurpose,b.loanDays,b.loanDays,b.loanDays,b.status as mailStatus  ");
		sql.append("from invest a left join t_mismatch b on a.id = b.invesId left join loan c on c.id = b.loanId where a.status ='1' ");
		if(mess[0].length() > 0 ){
			sql.append(" and instr(concat(',',?,','),concat(',',a.id,','))");
			list.add(mess[0]);
			String[] array = (String[])list.toArray(new String[list.size()]);
			creditTransferList = getCreditTransferVOList(sql.toString(),array);
			return creditTransferList;
		};
		if(mess[1].length() > 0){
			sql.append(" and a.investor like ? ");
			list.add('%'+mess[1]+'%');
		};
		if(mess[2].length() > 0){
			sql.append(" and a.investTerm = ? ");
			list.add(mess[2]);
		};
		if(mess[3].length() > 0){
			sql.append(" and a.produt like ? ");
			list.add('%'+mess[3]+'%');
		};
		if(mess[4].length() > 0){
			sql.append(" and b.date = ? ");
			list.add(mess[4]);
		};
		if(mess[5].length() > 0){
			sql.append(" and b.status = ? ");
			list.add(mess[5]);
		}
		String[] array = (String[])list.toArray(new String[list.size()]);
		creditTransferList = getCreditTransferVOList(sql.toString(),array);
		return creditTransferList;
	}
	/*
	 * 信息查询
	 */
	public List<CreditTransferVO> getCreditTransferVOList(String sql,Object[] object) { 
    	List<CreditTransferVO> creditTransferVOList = null;
    	JdbcTemplate jdbcTemplate = jdbcBaseDao.getJdbcTemplate();
    	creditTransferVOList = jdbcTemplate.query(sql,
    									object, 
										new RowMapper<CreditTransferVO>() {  
								            @Override  
								            public CreditTransferVO mapRow(ResultSet rs, int rowNum) throws SQLException {  
								            	CreditTransferVO creditTransferVO = new CreditTransferVO();   
								            	creditTransferVO.setAssigneeName(rs.getString("assigneeName"));
								            	creditTransferVO.setAssigneeIdentityId(rs.getString("assigneeIdentityId"));   
								            	creditTransferVO.setAssigneeMoney(rs.getInt("assigneeMoney")/1000);
								            	creditTransferVO.setAssigneeTmer(rs.getString("assigneeTmer"));
								            	creditTransferVO.setAssigneeDate(rs.getString("assigneeDate"));
								            	creditTransferVO.setProduct(rs.getString("product"));
								            	creditTransferVO.setMailStatus(rs.getInt("mailStatus"));
								            	creditTransferVO.setMail(rs.getString("mail"));
								            	creditTransferVO.setCustomtel(rs.getString("customtel"));
								            	creditTransferVO.setIds(rs.getString("ids"));
								            	creditTransferVO.setStatus(rs.getInt("mailStatus"));
								            	
								            	CreditAssigneeVO creditAssigneeVO = new CreditAssigneeVO();
								            	creditAssigneeVO.setLoanName(rs.getString("borrower"));
								            	creditAssigneeVO.setIdentityId(rs.getString("identityId"));
								            	creditAssigneeVO.setLoanamountBegin(rs.getInt("loanamount")/1000);
								            	creditAssigneeVO.setTransferValue(rs.getInt("misAmount")/1000);
								            	creditAssigneeVO.setPricePay(rs.getInt("misAmount")/1000);
								            	creditAssigneeVO.setLoanSituation(rs.getString("loanpurpose"));
								            	creditAssigneeVO.setLoanpurpose(rs.getString("loanpurpose"));
								            	creditAssigneeVO.setRepaymentTerm(rs.getInt("loanDays"));
								            	creditAssigneeVO.setRepaymentDate(rs.getString("loanDays"));
								            	creditAssigneeVO.setRepaymentTermRemain(rs.getInt("loanDays"));
								            	
								            	List<CreditAssigneeVO> creditAssigneeVOList = new ArrayList<CreditAssigneeVO>();
								            	creditAssigneeVOList.add(creditAssigneeVO);
								            	creditTransferVO.setCreditAssigneeList(creditAssigneeVOList);
								                return creditTransferVO;   
								            }  
							        	}
									);  
    	return combinCreditTransferVOs(creditTransferVOList);
    }
	//合并相同项
	public List<CreditTransferVO> combinCreditTransferVOs(List<CreditTransferVO> list) {
		Map<String,CreditTransferVO> loanGroups = new HashMap<String,CreditTransferVO>();
		for(CreditTransferVO cred : list){ 
			 if(loanGroups.containsKey(cred.getIds())){ //如果已经存在这个数组，就放在这里  
	             CreditTransferVO entiryList = loanGroups.get(cred.getIds());
	             List<CreditAssigneeVO> creditAssigneeVOList = entiryList.getCreditAssigneeList();
	             creditAssigneeVOList.add(cred.getCreditAssigneeList().get(0));
	             entiryList.setCreditAssigneeList(creditAssigneeVOList);
	         }else{  
	        	 loanGroups.put(cred.getIds(), cred);  
	         }  
		}
		List<CreditTransferVO> creditTransferVOList = new ArrayList<CreditTransferVO>();
		for (CreditTransferVO  v : loanGroups.values()) {
			creditTransferVOList.add(v);
	    }
		return creditTransferVOList;
	}

	@Override
	public boolean sendMailList(Map<String, String[]> meailMess) {
		Boolean flag = false ;
		String subject = "subject";//邮件主题
		String message = "message";//邮件内容
		String[] receiveMeail = meailMess.get("receiveMeail");//收件邮箱
		String[] fileName = meailMess.get("fileName"); //附件名称
		String[] absolutePath = meailMess.get("absolutePath");//附件的绝对路径
		for (int j = 0; j < receiveMeail.length; j++) {
			try {
				JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
				senderImpl.setHost(emailProper.getHost());//发件服务器地址
				senderImpl.setUsername(emailProper.getUsername());//发件用户名
				senderImpl.setPassword(emailProper.getPassword());//发件用户密码
				MimeMessage mimeMessage = senderImpl.createMimeMessage();
				MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");// true表示有附件
				mimeMessageHelper.setTo(receiveMeail[j]);//收件邮箱
				mimeMessageHelper.setFrom(emailProper.getJavaMailProperties().getProperty("sendFrom"));//发件邮箱
				mimeMessageHelper.setSubject(subject);
				mimeMessageHelper.setText(message, true);// true表示HTML格式的邮件
				FileSystemResource fileSystemResource = new FileSystemResource(new File(absolutePath[j]));
				mimeMessageHelper.addAttachment(MimeUtility.encodeWord(fileName[j]), fileSystemResource);
				senderImpl.send(mimeMessage);
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
				return flag;
			}
		}
		return flag;
	}

	

}
