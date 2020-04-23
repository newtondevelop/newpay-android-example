package org.newtonproject.newtoncore.android.data.entity.common;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class ScanResultInfo {
    public String type;
    public String address;
    public String amount;

    public ScanResultInfo(String type, String address, String amount) {
        this.type = type;
        this.address = address;
        this.amount = amount;
    }

    public ScanResultInfo(String type, String address) {
        this.type = type;
        this.address = address;
        this.amount = null;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object obj) {
        ScanResultInfo obj1 =(ScanResultInfo)obj;
        if(null != amount) {
            return amount.equals(obj1.amount) && address.equals(obj1.address) && type.equals(obj1.type);
        }else {
            return address.equals(obj1.address) && type.equals(obj1.type);
        }
    }

    @Override
    public String toString() {
        return "{ coin type: " + type + ", address is: " + address + ", amount is:" + amount + "}";
    }
}
