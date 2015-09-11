package com.jeefw.model.sys.param;

import core.support.ExtJSBaseParameter;

/**
 * 投资参数类
 */
public class InvestParameter extends ExtJSBaseParameter {
	
	private static final long serialVersionUID = -2128980673422261583L;
	private String $eq_dictKey;
	private String $like_dictValue;

	public String get$eq_dictKey() {
		return $eq_dictKey;
	}

	public void set$eq_dictKey(String $eq_dictKey) {
		this.$eq_dictKey = $eq_dictKey;
	}

	public String get$like_dictValue() {
		return $like_dictValue;
	}

	public void set$like_dictValue(String $like_dictValue) {
		this.$like_dictValue = $like_dictValue;
	}

}
