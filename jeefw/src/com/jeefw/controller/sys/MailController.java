package com.jeefw.controller.sys;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;












import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContext;






import com.jeefw.core.Constant;
import com.jeefw.core.JavaEEFrameworkBaseController;
import com.jeefw.model.sys.Attachment;
import com.jeefw.model.sys.Mail;
import com.jeefw.model.sys.SysUser;
import com.jeefw.service.sys.AttachmentService;
import com.jeefw.service.sys.InvestService;
import com.jeefw.service.sys.MailService;
import com.jeefw.service.sys.MismatchService;
import com.jeefw.service.sys.PdfService;

import core.pdf.bean.CreditTransferVO;
import core.support.JqGridPageView;
import core.util.JavaEEFrameworkUtils;

/**
 * 邮件的控制层
 */
@Controller
@RequestMapping("/sys/mail")
public class MailController extends JavaEEFrameworkBaseController<Mail> implements Constant {

	@Resource
	private MailService mailService;
	@Resource
	private AttachmentService attachmentService;
	
	@Resource
	private PdfService pdfService;
	
	@Resource
	private InvestService investService;
	
	
	@Resource
	private MismatchService mismatchService;
	

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	// 查询用户的表格，包括分页、搜索和排序
	@ResponseBody
	@RequestMapping(value = "/index", method = { RequestMethod.POST, RequestMethod.GET })
	public void index(HttpServletRequest request, HttpServletResponse response,String postData) throws Exception {
		//当前页第一条记录
		Integer firstResult = Integer.valueOf(request.getParameter("page"));
		//每页显示记录数
		Integer maxResults = Integer.valueOf(request.getParameter("rows"));
		//查询条件
		String[] mess = postData.split("/");
		//总记录
		List<CreditTransferVO> queryResult = mailService.getCreditTransferVO(mess);
		//展示页面样式
		JqGridPageView<CreditTransferVO> sysUserListView = new JqGridPageView<CreditTransferVO>();
		//页面展示记录
		List<CreditTransferVO> creditTransferVOShowList = null;
		if ((firstResult - 1) * maxResults + maxResults > queryResult.size()) {
			creditTransferVOShowList = queryResult.subList((firstResult - 1) * maxResults,queryResult.size());
		} else {
			creditTransferVOShowList = queryResult.subList((firstResult - 1) * maxResults, (firstResult - 1)* maxResults + maxResults);
		}
		sysUserListView.setMaxResults(maxResults);
		sysUserListView.setRows(creditTransferVOShowList);
		sysUserListView.setRecords(queryResult.size());
		writeJSON(response, sysUserListView);
	}
	//投资信息修改
	@ResponseBody
	@RequestMapping(value = "/updateMess", method = { RequestMethod.POST, RequestMethod.GET })
	public void updateMess(HttpServletRequest request, HttpServletResponse response,String postData) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		String investId = request.getParameter("ids");
		String investor = request.getParameter("assigneeName");
		String identityId = request.getParameter("assigneeIdentityId");
		if(investService.updateInvest(investId,investor,identityId)){
			result.put("message", "信息修改成功！");
		}else{
			result.put("message", "信息修改失败！");
		}
		writeJSON(response, result);
	}
	
	//发送邮件
	@ResponseBody
	@RequestMapping(value = "/sendMails", method = { RequestMethod.POST})
	public void sendMails(HttpServletRequest request, HttpServletResponse response,String postData) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		//生成pdf信息条件
		String[] mess = postData.split("/");
		//获得信息
		List<CreditTransferVO> creditTransferList = mailService.getCreditTransferVO(mess);
		String investIds = "";
		for (int i = 0; i < creditTransferList.size(); i++) {
			investIds+=creditTransferList.get(i).getIds()+",";
		};
		//生成pdf，获得路径信息
		Map<String, String[]> meailMess = pdfService.createPdfList(creditTransferList);
		//批量发送邮件
		Boolean flag = mailService.sendMailList(meailMess);
		if(flag){
			mismatchService.updateMismatch(investIds.substring(0, investIds.length()-1));
			result.put("message", "邮件发送成功！");
		}else{
			result.put("message", "邮件发送失败！");
		}
		writeJSON(response, result);
	}

	// 删除邮件
	@RequestMapping("/deleteMail")
	public void deleteMail(HttpServletRequest request, HttpServletResponse response, @RequestParam("ids") Long[] ids) throws IOException {
		boolean flag = mailService.deleteByPK(ids);
		if (flag) {
			writeJSON(response, "{success:true}");
		} else {
			writeJSON(response, "{success:false}");
		}
	}

	 // 上传邮件附件
	@RequestMapping(value = "/sendMailAttachment", method = { RequestMethod.POST, RequestMethod.GET })
	public void sendMailAttachment(@RequestParam(value = "attachment[]", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!file.isEmpty()) {
			RequestContext requestContext = new RequestContext(request);
			Map<String, Object> result = new HashMap<String, Object>();
			if (file.getSize() > 2097152) {
				result.put("status", requestContext.getMessage("g_fileTooLarge"));
			} else {
				String originalFilename = file.getOriginalFilename();
				String fileName = sdf.format(new Date()) + JavaEEFrameworkUtils.getRandomString(3) + originalFilename.substring(originalFilename.lastIndexOf("."));
				File filePath = new File(getClass().getClassLoader().getResource("/").getPath().replace("/WEB-INF/classes/", "/static/upload/attachment/" + DateFormatUtils.format(new Date(), "yyyyMM")));
				if (!filePath.exists()) {
					filePath.mkdirs();
				}
				file.transferTo(new File(filePath.getAbsolutePath() + "\\" + fileName));

				String destinationFilePath = "/static/upload/attachment/" + DateFormatUtils.format(new Date(), "yyyyMM") + "/" + fileName;
				Long sysUserId = ((SysUser) request.getSession().getAttribute(SESSION_SYS_USER)).getId();
				Attachment attachment = new Attachment();
				attachment.setFileName(originalFilename);
				attachment.setFilePath(destinationFilePath);
				attachment.setType((short) 2);
				attachment.setTypeId(sysUserId);
				attachmentService.persist(attachment);

				result.put("status", "OK");
				result.put("originalFilename", originalFilename);
				result.put("url", filePath.getAbsolutePath() + "\\" + fileName);
			}
			writeJSON(response, result);
		}
	}

	// 发送邮件
	@RequestMapping(value = "/sendMail", method = { RequestMethod.POST, RequestMethod.GET })
	public void sendMail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String originalFilenames = request.getParameter("originalFilenames");
		String attachmentpaths = request.getParameter("attachmentpaths");
		String senderEmail = ((SysUser) request.getSession().getAttribute(SESSION_SYS_USER)).getEmail();
		String recipient = request.getParameter("recipient");
		String subject = request.getParameter("subject");
		String message = request.getParameter("message");
		String[] multiRecipient = recipient.split(",");
		String[] multiAttachmentFileName = originalFilenames.split(" ");
		String[] multiAttachmentAbsolutePath = attachmentpaths.split(",");
		Mail entity = new Mail();
		entity.setRecipient(recipient);
		entity.setSender(senderEmail);
		entity.setSubject(subject);
		entity.setMessage(message);
		mailService.persist(entity);

		for (int j = 0; j < multiRecipient.length; j++) {
			try {
				JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
				senderImpl.setHost("smtp.163.com");
				senderImpl.setUsername("javaeeframework@163.com");
				senderImpl.setPassword("abcd123456");
				MimeMessage mimeMessage = senderImpl.createMimeMessage();
				MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, StringUtils.isNotBlank(attachmentpaths) ? true : false, "utf-8");// true表示有附件
				mimeMessageHelper.setTo(multiRecipient[j]);
				mimeMessageHelper.setFrom("javaeeframework@163.com");
				mimeMessageHelper.setSubject(subject);
				mimeMessageHelper.setText(message, true);// true表示HTML格式的邮件
				if (StringUtils.isNotBlank(attachmentpaths)) {
					for (int i = 0; i < multiAttachmentAbsolutePath.length; i++) {
						FileSystemResource fileSystemResource = new FileSystemResource(new File(multiAttachmentAbsolutePath[i]));
						mimeMessageHelper.addAttachment(MimeUtility.encodeWord(multiAttachmentFileName[i].substring(multiAttachmentFileName[i].lastIndexOf("/") + 1)), fileSystemResource);
					}
				}
				senderImpl.send(mimeMessage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "邮件发送成功");
		writeJSON(response, result);
	}
}
