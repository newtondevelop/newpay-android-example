package org.newtonproject.newtoncore.android.data.entity.common;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class ServiceException extends Exception {
	public final ErrorEnvelope error;

	public ServiceException(String message) {
		super(message);

		error = new ErrorEnvelope(message);
	}
}
