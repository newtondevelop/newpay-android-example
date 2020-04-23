package org.newtonproject.newtoncore.android.data.entity.response;

import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/29--PM 4:54
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class ProfileInfo {

    @SerializedName("newid")
    public String newid;
    @SerializedName("country_code")
    public String countryCode;
    @SerializedName("cellphone")
    public String cellphone;
    @SerializedName("invite_code")
    public String inviteCode;
    @SerializedName("avatar")
    public String avatarPath;
    @SerializedName("name")
    public String name;

    @SerializedName("access_key")
    public String accessKey;
    @SerializedName("wallet_address")
    public String address;

    @SerializedName("candidate_status")
    public int candidateStatus; //-1:no elected, 0:candidate,1:elected,2:backup,3:quit, 4: partner node pending
    @SerializedName("candidate_node_id")
    public String candidateNodeId;
    @SerializedName("candidate_rank")
    public int candidateRank;
    @SerializedName("candidate_node_type")
    public int candidateNodeType = NodeCondition.NODE_TYPE.PROMOTION;
    @SerializedName("candidate_node_name")
    public String candidateNodeName;

    @SerializedName("vote_node_id")
    public String voteNodeId;
    @SerializedName("vote_rank")
    public int voteRank;
    @SerializedName("vote_status")
    public int voteStatus;
    @SerializedName("vote_node_type")
    public int voteNodeType = NodeCondition.NODE_TYPE.PROMOTION;
    @SerializedName("vote_node_name")
    public String voteNodeName;

    // check can change lock
    @SerializedName("can_candidate_add")
    public boolean canCandidateAdd = false;

    @SerializedName("can_candidate_reduce")
    public boolean canCandidateReduce = false;

    @SerializedName("can_vote_add")
    public boolean canVoteAdd = false;

    @SerializedName("can_vote_reduce")
    public boolean canVoteReduce = false;

    @SerializedName("can_vote_change")
    public boolean canVoteChange = false;

    @SerializedName("is_in_counting")
    public boolean isInCounting;
    // is partner node
    @SerializedName("candidate_is_partner_node")
    public boolean candidateIsPartnerNode = false;
    @SerializedName("vote_is_partner_node")
    public boolean voteIsPartnerNode = false;
    @SerializedName("is_organizer")
    public boolean isOrganizer = false;

    public String getNewid() {
        return newid;
    }

    public void setNewid(String newid) {
        this.newid = newid;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccessKey() {
        return accessKey;
    }

    @JsonSetter("access_key")
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCandidateStatus() {
        return candidateStatus;
    }

    public void setCandidateStatus(int candidateStatus) {
        this.candidateStatus = candidateStatus;
    }

    public String getCandidateNodeId() {
        return candidateNodeId;
    }

    public void setCandidateNodeId(String candidateNodeId) {
        this.candidateNodeId = candidateNodeId;
    }

    public int getCandidateRank() {
        return candidateRank;
    }

    public void setCandidateRank(int candidateRank) {
        this.candidateRank = candidateRank;
    }

    public String getVoteNodeId() {
        return voteNodeId;
    }

    public void setVoteNodeId(String voteNodeId) {
        this.voteNodeId = voteNodeId;
    }

    public int getVoteRank() {
        return voteRank;
    }

    public void setVoteRank(int voteRank) {
        this.voteRank = voteRank;
    }

    public int getVoteStatus() {
        return voteStatus;
    }

    public void setVoteStatus(int voteStatus) {
        this.voteStatus = voteStatus;
    }

    @Override
    public String toString() {
        return "ProfileInfo{" +
                "newid='" + newid + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", cellphone='" + cellphone + '\'' +
                ", inviteCode='" + inviteCode + '\'' +
                ", avatarPath='" + avatarPath + '\'' +
                ", name='" + name + '\'' +
                ", accessKey='" + accessKey + '\'' +
                ", address='" + address + '\'' +
                ", candidateStatus=" + candidateStatus +
                ", candidateNodeId='" + candidateNodeId + '\'' +
                ", candidateRank=" + candidateRank +
                ", candidateNodeType=" + candidateNodeType +
                ", candidateNodeName='" + candidateNodeName + '\'' +
                ", voteNodeId='" + voteNodeId + '\'' +
                ", voteRank=" + voteRank +
                ", voteStatus=" + voteStatus +
                ", voteNodeType=" + voteNodeType +
                ", voteNodeName='" + voteNodeName + '\'' +
                ", canCandidateAdd=" + canCandidateAdd +
                ", canCandidateReduce=" + canCandidateReduce +
                ", canVoteAdd=" + canVoteAdd +
                ", canVoteReduce=" + canVoteReduce +
                ", canVoteChange=" + canVoteChange +
                ", isInCounting=" + isInCounting +
                ", candidateIsPartnerNode=" + candidateIsPartnerNode +
                ", voteIsPartnerNode=" + voteIsPartnerNode +
                ", isOrganizer=" + isOrganizer +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileInfo that = (ProfileInfo) o;
        return candidateStatus == that.candidateStatus &&
                candidateRank == that.candidateRank &&
                voteRank == that.voteRank &&
                Objects.equals(newid, that.newid) &&
                Objects.equals(countryCode, that.countryCode) &&
                Objects.equals(cellphone, that.cellphone) &&
                Objects.equals(inviteCode, that.inviteCode) &&
                Objects.equals(avatarPath, that.avatarPath) &&
                Objects.equals(name, that.name) &&
                Objects.equals(accessKey, that.accessKey) &&
                Objects.equals(address, that.address) &&
                Objects.equals(candidateNodeId, that.candidateNodeId) &&
                Objects.equals(candidateNodeName, that.candidateNodeName) &&
                Objects.equals(voteNodeName, that.voteNodeName) &&
                Objects.equals(voteNodeType, that.voteNodeType) &&
                Objects.equals(candidateNodeType, that.candidateNodeType) &&
                Objects.equals(voteNodeId, that.voteNodeId) &&
                Objects.equals(candidateIsPartnerNode, that.candidateIsPartnerNode) &&
                Objects.equals(voteIsPartnerNode, that.voteIsPartnerNode) &&
                Objects.equals(isOrganizer, that.isOrganizer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newid, countryCode, cellphone, inviteCode, avatarPath, name, accessKey, address, candidateStatus, candidateNodeId, candidateRank, voteNodeId, voteRank, voteStatus, candidateNodeName,
                voteNodeName, candidateNodeType, voteNodeType, candidateIsPartnerNode, voteIsPartnerNode, isOrganizer);
    }

    public void updateProfile(ProfileInfo profileInfo) {
        this.canCandidateAdd = profileInfo.canCandidateAdd;
        this.canCandidateReduce = profileInfo.canCandidateReduce;
        this.cellphone = profileInfo.cellphone;
        this.candidateRank = profileInfo.candidateRank;
        this.candidateStatus = profileInfo.candidateStatus;
        this.canVoteAdd = profileInfo.canVoteAdd;
        this.canVoteChange = profileInfo.canVoteChange;
        this.canVoteReduce = profileInfo.canVoteReduce;
        this.voteNodeId = profileInfo.voteNodeId;
        this.voteRank = profileInfo.voteRank;
        this.voteStatus = profileInfo.voteStatus;
        this.candidateNodeName = TextUtils.isEmpty(profileInfo.candidateNodeName) ? this.candidateNodeName : profileInfo.candidateNodeName;
        this.voteNodeName = TextUtils.isEmpty(profileInfo.voteNodeName) ? this.voteNodeName : profileInfo.voteNodeName;
        this.candidateNodeType = profileInfo.candidateNodeType;
        this.voteNodeType = profileInfo.voteNodeType;

        this.name = TextUtils.isEmpty(profileInfo.name) ? this.name : profileInfo.name;
        this.avatarPath = TextUtils.isEmpty(profileInfo.avatarPath) ? this.avatarPath : profileInfo.avatarPath;
        this.cellphone = TextUtils.isEmpty(profileInfo.cellphone) ? this.cellphone : profileInfo.cellphone;
        this.countryCode = TextUtils.isEmpty(profileInfo.countryCode) ? this.countryCode : profileInfo.countryCode;
        this.inviteCode = TextUtils.isEmpty(profileInfo.inviteCode) ? this.inviteCode : profileInfo.inviteCode;
        this.candidateNodeId = TextUtils.isEmpty(profileInfo.candidateNodeId) ? this.candidateNodeId : profileInfo.candidateNodeId;

        this.candidateIsPartnerNode = profileInfo.candidateIsPartnerNode;
        this.voteIsPartnerNode = profileInfo.voteIsPartnerNode;
        this.isOrganizer = profileInfo.isOrganizer;
    }
}
