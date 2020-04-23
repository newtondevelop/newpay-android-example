package org.newtonproject.newtoncore.android.data.entity.hepnode;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2019-06-03--19:18
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class NewNetProofSubmit {

    @SerializedName("uuid")
    public String uuid;
    @SerializedName("dapp_id")
    public String dappId;
    @SerializedName("dapp_key")
    public String dappKey;
    @SerializedName("protocol")
    public String protocol;
    @SerializedName("version")
    public String version;
    @SerializedName("ts")
    public String timestamp;
    @SerializedName("nonce")
    public String nonce;
    @SerializedName("signature")
    public String signature;
    @SerializedName("sign_type")
    public String signType;
    @SerializedName("action")
    public String action;

    @SerializedName("content")
    public OrderContent content;
    @SerializedName("language")
    public String language;
    @SerializedName("os")
    public String os;

    public class OrderContent{
        @SerializedName("total_price")
        public String totalPrice;
        @SerializedName("price_currency")
        public String priceCurrency;
        @SerializedName("submitter")
        public String submitter;
        @SerializedName("proof_type")
        public String proofType;
        @SerializedName("orders")
        public ArrayList<Order> orders;

        @Override
        public String toString() {
            return "OrderContent{" +
                    "totalPrice='" + totalPrice + '\'' +
                    ", priceCurrency='" + priceCurrency + '\'' +
                    ", submitter='" + submitter + '\'' +
                    ", proofType='" + proofType + '\'' +
                    ", orders=" + orders +
                    '}';
        }
    }


    public class Order{
        @SerializedName("order_number")
        public String orderNumber;
        @SerializedName("description")
        public String description;
        @SerializedName("chain_txid")
        public String chainTxid;
        @SerializedName("seller")
        public String seller;
        @SerializedName("broker")
        public String broker;
        @SerializedName("customer")
        public String customer;
        @SerializedName("total_price")
        public String totalPrice;
        @SerializedName("price_currency")
        public String priceCurrency;
        @SerializedName("order_items")
        public ArrayList<OrderItem> orderItems;

        @Override
        public String toString() {
            return "Order{" +
                    "orderNumber='" + orderNumber + '\'' +
                    ", description='" + description + '\'' +
                    ", chainTxid='" + chainTxid + '\'' +
                    ", seller='" + seller + '\'' +
                    ", broker='" + broker + '\'' +
                    ", customer='" + customer + '\'' +
                    ", totalPrice='" + totalPrice + '\'' +
                    ", priceCurrency='" + priceCurrency + '\'' +
                    ", orderItems=" + orderItems +
                    '}';
        }
    }

    public class OrderItem {
        @SerializedName("order_item_number")
        public String orderItemNumber;
        @SerializedName("ordered_item")
        public OrderDetail orderDetail;
        @SerializedName("order_item_quantity")
        public int orderQuantity;
        @SerializedName("price")
        public String price;
        @SerializedName("price_currency")
        public String priceCurrency;

        @Override
        public String toString() {
            return "OrderItem{" +
                    "orderItemNumber='" + orderItemNumber + '\'' +
                    ", orderDetail=" + orderDetail +
                    ", orderQuantity=" + orderQuantity +
                    ", price='" + price + '\'' +
                    ", priceCurrency='" + priceCurrency + '\'' +
                    '}';
        }
    }

    public class OrderDetail {
        @SerializedName("name")
        public String name;
        @SerializedName("thing_type")
        public String type;
        @SerializedName("thing_id")
        public String productId;

        @Override
        public String toString() {
            return "OrderDetail{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", productId='" + productId + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NewNetProofSubmit{" +
                "uuid='" + uuid + '\'' +
                ", dappId='" + dappId + '\'' +
                ", dappKey='" + dappKey + '\'' +
                ", protocol='" + protocol + '\'' +
                ", version='" + version + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", nonce='" + nonce + '\'' +
                ", signature='" + signature + '\'' +
                ", signType='" + signType + '\'' +
                ", action='" + action + '\'' +
                ", content=" + content +
                ", language='" + language + '\'' +
                ", os='" + os + '\'' +
                '}';
    }

    public ArrayList<OrderItem> getAllOrderItem() {
        ArrayList<Order> orders = content.orders;
        ArrayList<OrderItem> items = new ArrayList<>();
        for(Order order : orders) {
            items.addAll(order.orderItems);
        }
        return items;
    }
}
