package com.jeefw.service.sys;

import java.util.List;
import java.util.Map;

import core.pdf.bean.CreditTransferVO;


/**
 * 角色的业务逻辑层的接口
 */
public interface PdfService  {

	public Map<String, String[]> createPdfList(List<CreditTransferVO> creditTransferList);

}
