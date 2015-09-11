package core.pdf;

import java.util.HashMap;
import java.util.Map;

import core.util.JacksonBinder;



/**
 * 模板中需要的数据视图 抽象类
 * @ClassName: AbstractDocumentVo
 * @Description: 模板中需要的数据视图 抽象类
 */
public abstract class AbstractDocumentVo implements DocumentVo{

	/**
	 * ,填充模板中数据,获取模板数据map
	 * @Title: fillDataMap
	 * @Description:  获取模板数据map
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> fillDataMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		DocumentVo vo = this.getDocumentVo();
		map = JacksonBinder.buildNonDefaultBinder().convertValue(vo, HashMap.class);
		return map;
	}
	
	private DocumentVo getDocumentVo() {
		return this;
	}

}
