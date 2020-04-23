package org.newtonproject.newtoncore.android.data.entity.common;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class TransactionResponse {
    public int total;
    public int limit;
    public int page;
    public int pages;
    public Transaction[] docs;
}
