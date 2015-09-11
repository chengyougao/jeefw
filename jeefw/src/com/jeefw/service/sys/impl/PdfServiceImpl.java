package com.jeefw.service.sys.impl;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import com.jeefw.service.sys.PdfService;
import core.pdf.PdfDocumentGenerator;
import core.pdf.bean.CreditTransferVO;
import core.pdf.exception.DocumentGeneratingException;
import core.util.ResourceLoader;

/**
 * 角色的业务逻辑层的实现
 */
@Service
public class PdfServiceImpl  implements PdfService {
	
	//返回pdf名称和绝对路径
	@Override
	public Map<String, String[]> createPdfList(List<CreditTransferVO> creditTransferList) {
		long start = System.currentTimeMillis();
		Map<String, String[]> meailMess = new HashMap<String, String[]>();
		int size = creditTransferList.size();
		String[] receiveMeail = new String[size]; //收件邮箱
		String[] fileName  = new String[size]; //附件名称
		String[] absolutePath  = new String[size]; //附件的绝对路径
		String[] mess = new String[2];
		for(int i = 0 ;i < size; i++){
			receiveMeail[i]=creditTransferList.get(i).getMail();
			mess = createPdf(creditTransferList.get(i));
			fileName[i]=mess[0];
			absolutePath[i]=mess[1];
		}
		meailMess.put("fileName", fileName);
		meailMess.put("absolutePath", absolutePath);
		meailMess.put("receiveMeail", receiveMeail);
		System.err.println("耗时time=" + (System.currentTimeMillis() - start)/ 1000);
		return meailMess;
	}
	//生成PDF文件
	public String[] createPdf(CreditTransferVO creditTransferVO) {
		//模板数据
		creditTransferVO.setTransferorName("彭钢");
		creditTransferVO.setTransferorIdentityId("1212121212");
		creditTransferVO.setReportTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		creditTransferVO.setImagePath(ResourceLoader.getPath("config/images"));
		//pdf名称
		String fileName = creditTransferVO.getAssigneeName()+"-"+creditTransferVO.getAssigneeIdentityId()+"-"+creditTransferVO.getAssigneeTmer()+"-"+creditTransferVO.getAssigneeDate()+ ".pdf";//用户名-证件ID-投资期限-投资启期
		//中模板路径
		String template = "config/templates/transferOfCreditor.html";
		// 路径
		String outputFileClass = ResourceLoader.getPath("");
		// 生成pdf路径
		String outputFile = new File(outputFileClass).getParentFile().getParent()+ "/tmp/"+ System.currentTimeMillis()+"-"+creditTransferVO.getIds()+"-"+creditTransferVO.getAssigneeName() +"-"+ ".pdf";
		PdfDocumentGenerator pdfGenerator = new PdfDocumentGenerator();
		// 生成pdf
		try {
			pdfGenerator.generate(template, creditTransferVO, outputFile);
			System.out.println(" \n pdf生成成功  IS OK path=\n" + outputFile);
		} catch (DocumentGeneratingException e) {
			e.printStackTrace();
		}
		//名称和路径
		String[] fileNameAndPath = new String[2];
		fileNameAndPath[0]=fileName;
		fileNameAndPath[1]=outputFile;
		return fileNameAndPath;
	}

}
