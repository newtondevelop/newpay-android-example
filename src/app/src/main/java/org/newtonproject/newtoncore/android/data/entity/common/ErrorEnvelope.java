package org.newtonproject.newtoncore.android.data.entity.common;

import android.support.annotation.Nullable;

import org.newtonproject.newtoncore.android.C;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class ErrorEnvelope {
	public final int code;
	@Nullable
	public final String message;
	@Nullable
	private final Throwable throwable;

	public ErrorEnvelope(@Nullable String message) {
		this(C.ErrorCode.UNKNOWN, message);
	}

	public ErrorEnvelope(int code, @Nullable String message) {
		this(code, message, null);
	}

	public ErrorEnvelope(int code, @Nullable String message, @Nullable Throwable throwable) {
		this.code = code;
		this.message = message;
		this.throwable = throwable;
	}
}
