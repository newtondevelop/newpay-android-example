package org.newtonproject.newtoncore.android.data.entity.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2019/1/6--6:38 PM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 * {
 *     "errorCode": 1,
 *     "result": {
 *         data: [
 *             {
 *                 "newid": "NEWID1823343434343434",
 *                 "amount": "100000",
 *                 "action": "staking",
 *                 "created_at": 129837192837,
 *             },
 *         ]
 *     }
 * }
 */
public class StakingHistory {
    @SerializedName("total_page")
    public int totalPage;
    @SerializedName("page_id")
    public int pageId;
    @SerializedName("data")
    public ArrayList<StakingInfo> datas;

    public class StakingInfo{
        @SerializedName("newid")
        public String newid;
        @SerializedName("amount")
        public String amount;
        @SerializedName("action")
        public String action;
        @SerializedName("created_at")
        public long createAt;
        @SerializedName("end_at")
        public long endAt;

        @Override
        public String toString() {
            return "StakingInfo{" +
                    "newid='" + newid + '\'' +
                    ", amount='" + amount + '\'' +
                    ", action='" + action + '\'' +
                    ", createAt=" + createAt +
                    ", endAt=" + endAt +
                    '}';
        }
    }
}
