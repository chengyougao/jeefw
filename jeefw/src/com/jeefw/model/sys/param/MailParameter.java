package com.jeefw.model.sys.param;

import core.support.ExtJSBaseParameter;

/**
 * 邮件的参数类
 */
public class MailParameter extends ExtJSBaseParameter {

	private static final long serialVersionUID = 5303795602788116765L;
	private String $like_subject;

	public String get$like_subject() {
		return $like_subject;
	}

	public void set$like_subject(String $like_subject) {
		this.$like_subject = $like_subject;
	}

}
