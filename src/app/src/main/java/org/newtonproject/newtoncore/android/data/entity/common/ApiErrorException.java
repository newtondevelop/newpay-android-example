package org.newtonproject.newtoncore.android.data.entity.common;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class ApiErrorException extends Exception {
    private final ErrorEnvelope errorEnvelope;

    public ApiErrorException(ErrorEnvelope errorEnvelope) {
        super(errorEnvelope.message);

        this.errorEnvelope = errorEnvelope;
    }
}
