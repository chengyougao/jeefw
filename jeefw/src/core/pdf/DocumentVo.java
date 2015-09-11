package core.pdf;

import java.util.Map;
/**
 * 模板中需要的数据视图
 * @ClassName: DocumentVo
 * @Description:模板中需要的数据视图
 */
public interface DocumentVo {
	/**
	 * 获取主键,用于记录日志
	 * @Title: findPrimaryKey
	 * @Description: 获取主键,用于记录日志
	 */
	public String findPrimaryKey();
	/**
	 * 获取数据map
	 * @Title: fillDataMap
	 * @Description: 
	 * @return
	 */
	public Map<String, Object> fillDataMap();
}
