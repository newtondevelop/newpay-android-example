package org.newtonproject.newtoncore.android.data.entity.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2019/1/9--6:39 PM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class AssetInfo {

    @SerializedName("newid")
    public String newid;
    @SerializedName("staking_tokens")
    public StakingTokens stakingTokens;
    @SerializedName("pending_tokens")
    public PendingTokens pendingTokens;
    @SerializedName("available_tokens")
    public AvailableTokens availableTokens;
    @SerializedName("nf")
    public NewForce nf;

    @Override
    public String toString() {
        return "AssetInfo{" +
                "newid='" + newid + '\'' +
                ", stakingTokens=" + stakingTokens +
                ", pendingTokens=" + pendingTokens +
                ", availableTokens=" + availableTokens +
                ", nf=" + nf +
                '}';
    }

    public class StakingTokens {
        @SerializedName("total")
        public String total;
        @SerializedName("vote")
        public String vote;
        @SerializedName("node")
        public String node;
        @SerializedName("node_pending")
        public String nodePending;
        @SerializedName("vote_pending")
        public String votePending;
        @SerializedName("my_staking_tokens")
        public String myStakingTokens;
        @SerializedName("candidate_staking_tokens")
        public String candidateStakingTokens;

        @Override
        public String toString() {
            return "StakingTokens{" +
                    "total='" + total + '\'' +
                    ", vote='" + vote + '\'' +
                    ", node='" + node + '\'' +
                    ", nodePending='" + nodePending + '\'' +
                    ", votePending='" + votePending + '\'' +
                    ", myStakingTokens='" + myStakingTokens + '\'' +
                    ", candidateStakingTokens='" + candidateStakingTokens + '\'' +
                    '}';
        }
    }

    public class PendingTokens{
        @SerializedName("total")
        public String total;
        @SerializedName("buy")
        public String buy;
        @SerializedName("invite")
        public String invite;

        @Override
        public String toString() {
            return "PendingTokens{" +
                    "total='" + total + '\'' +
                    ", buy='" + buy + '\'' +
                    ", invite='" + invite + '\'' +
                    '}';
        }
    }

    public class AvailableTokens {
        @SerializedName("total")
        public String total;
        @SerializedName("invite")
        public String invite;
        @SerializedName("buy")
        public String buy;
        @SerializedName("sale")
        public String sale;
        @SerializedName("vote")
        public String vote;

        @Override
        public String toString() {
            return "AvailableTokens{" +
                    "total='" + total + '\'' +
                    ", invite='" + invite + '\'' +
                    ", buy='" + buy + '\'' +
                    ", sale='" + sale + '\'' +
                    ", vote='" + vote + '\'' +
                    '}';
        }
    }

    public class NewForce {
        @SerializedName("total")
        public String total;
        @SerializedName("direct_buy")
        public String directBuy;
        @SerializedName("indirect_buy")
        public String indirectBy;
        @SerializedName("sale")
        public String sale;

        @Override
        public String toString() {
            return "NewForce{" +
                    "total='" + total + '\'' +
                    ", directBuy='" + directBuy + '\'' +
                    ", indirectBy='" + indirectBy + '\'' +
                    ", sale='" + sale + '\'' +
                    '}';
        }
    }
}
