package com.jeefw.service.sys;

import java.util.List;






import java.util.Map;

import com.jeefw.model.sys.Mail;

import core.pdf.bean.CreditTransferVO;
import core.service.Service;

/**
 * 邮件的业务逻辑层的接口
 */
public interface MailService extends Service<Mail> {
	public List<CreditTransferVO> getCreditTransferVO(String[] mess);
	
	boolean sendMailList(Map<String, String[]> meailMess);
	
}
