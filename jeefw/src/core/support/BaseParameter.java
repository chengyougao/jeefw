package core.support;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class BaseParameter implements Serializable {
	private static final long serialVersionUID = -2050801753454734869L;
	public static final String SORTED_ASC = "ASC";
	public static final String SORTED_DESC = "DESC";
	private Integer maxResults = Integer.valueOf(20);
	private Integer firstResult = Integer.valueOf(0);
	private Integer topCount;
	private String[] sortColumns;
	private String cmd;
	private String flag = "AND";
	private Map<String, Object> queryDynamicConditions = new HashMap<String, Object>(4);
	private Map<String, String> sortedConditions = new LinkedHashMap<String, String>(2);
	private Map<String, Object> dynamicProperties = new HashMap<String, Object>(4);

	public Integer getMaxResults() {
		return this.maxResults;
	}

	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}

	public Map<String, Object> getQueryDynamicConditions() {
		return this.queryDynamicConditions;
	}

	public void setQueryDynamicConditions(Map<String, Object> queryDynamicConditions) {
		this.queryDynamicConditions = queryDynamicConditions;
	}

	public Map<String, String> getSortedConditions() {
		return this.sortedConditions;
	}

	public void setSortedConditions(Map<String, String> sortedConditions) {
		this.sortedConditions = sortedConditions;
	}

	public Integer getTopCount() {
		return this.topCount;
	}

	public void setTopCount(Integer topCount) {
		this.topCount = topCount;
	}

	public String[] getSortColumns() {
		return this.sortColumns;
	}

	public Map<String, Object> getDynamicProperties() {
		return this.dynamicProperties;
	}

	public void setDynamicProperties(Map<String, Object> dynamicProperties) {
		this.dynamicProperties = dynamicProperties;
	}

	public String getSortColumnsString() {
		StringBuffer sb = new StringBuffer();
		if (this.sortColumns != null) {
			for (String s : this.sortColumns) {
				sb.append("&sortColumns=" + s);
			}
		}
		return sb.toString();
	}

	public void setSortColumns(String[] sortColumns) {
		this.sortColumns = sortColumns;
		if (sortColumns != null) {
			for (String s : sortColumns) {
				String[] sa = s.split("\\|");
				if (sa.length == 2) {
					this.sortedConditions.put(sa[0], sa[1]);
				}
			}
		}
	}

	public String getCmd() {
		return this.cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public Integer getFirstResult() {
		return this.firstResult;
	}

	public void setFirstResult(Integer firstResult) {
		this.firstResult = firstResult;
	}

	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
}
