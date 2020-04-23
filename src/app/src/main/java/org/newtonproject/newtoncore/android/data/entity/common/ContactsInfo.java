package org.newtonproject.newtoncore.android.data.entity.common;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class ContactsInfo {

    public String address;
    public String name;
    public String firstPinYin;

    public ContactsInfo(String address) {
        this.address = address;
    }

    public ContactsInfo(String address, String name) {
        this.address = address;
        this.name = name;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstPinYin() {
        return firstPinYin;
    }

    public void setFirstPinYin(String firstPinYin) {
        char c = firstPinYin.toUpperCase().charAt(0);
        if(c > 'Z' || c < 'A') {
            firstPinYin = "#";
        }
        this.firstPinYin = firstPinYin.toUpperCase();
    }
}
