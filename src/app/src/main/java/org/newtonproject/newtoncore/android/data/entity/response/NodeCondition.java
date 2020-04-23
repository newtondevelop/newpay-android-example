package org.newtonproject.newtoncore.android.data.entity.response;

import com.google.gson.annotations.SerializedName;

import org.newtonproject.newtoncore.android.utils.BalanceUtils;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2019/1/6--5:36 PM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class NodeCondition {
    @SerializedName("is_partner")
    public boolean isPartner;
    @SerializedName("contract_address")
    public String contractAddress;
    @SerializedName("node_type_list")
    public ArrayList<Detail> nodeTypeList; // 1 is promotion（default）, 2 is business, 3 is technology
    @SerializedName("document_url")
    public String documentUrl;

    public static class Detail{
        @SerializedName("node_type")
        public int nodeType;
        @SerializedName("minimum_tokens")
        public String minimumTokens;
        @SerializedName("change_minimum_tokens")
        public String changeMinimumTokens;
        // add partner node information
        @SerializedName("organizer_minimum_tokens")
        public String organizerMinimumTokens;
        @SerializedName("management_ratio_minimum")
        private String managementRatioMinimum;
        @SerializedName("management_ratio_maximum")
        private String managementRatioMaximum;
        @SerializedName("partner_minimum_tokens")
        public String partnerMinimumTokens;

        public int getManagementRatioMinimumProcess() {
            return Integer.valueOf(BalanceUtils.divideZero(new BigDecimal(managementRatioMinimum).multiply(new BigDecimal(10000)).toBigInteger().toString()));
        }

        public int getManagementRatioMaximumProcess() {
            return Integer.valueOf(BalanceUtils.divideZero(new BigDecimal(managementRatioMaximum).multiply(new BigDecimal(10000)).toBigInteger().toString()));
        }

        public String getManagementRatioMinimumPercent() {
            return new BigDecimal(managementRatioMinimum).multiply(new BigDecimal(10000)).divide(new BigDecimal(100)).toString() + " %";
        }

        public String getManagementRatioMaximumPercent() {
            return new BigDecimal(managementRatioMaximum).multiply(new BigDecimal(10000)).divide(new BigDecimal(100)).toString() + " %";
        }

        @Override
        public String toString() {
            return "Detail{" +
                    "nodeType=" + nodeType +
                    ", minimumTokens='" + minimumTokens + '\'' +
                    ", changeMinimumTokens='" + changeMinimumTokens + '\'' +
                    ", organizerMinimumTokens='" + organizerMinimumTokens + '\'' +
                    ", managementRatioMinimum='" + managementRatioMinimum + '\'' +
                    ", managementRatioMaximum='" + managementRatioMaximum + '\'' +
                    ", partnerMinimumTokens='" + partnerMinimumTokens + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NodeCondition{" +
                "isPartner=" + isPartner +
                ", contractAddress='" + contractAddress + '\'' +
                ", nodeTypeList=" + nodeTypeList +
                ", documentUrl='" + documentUrl + '\'' +
                '}';
    }

    public interface NODE_TYPE {
        int PROMOTION = 1;
        int BUSINESS = 2;
        int TECHNOLOGY = 3;
    }
}
