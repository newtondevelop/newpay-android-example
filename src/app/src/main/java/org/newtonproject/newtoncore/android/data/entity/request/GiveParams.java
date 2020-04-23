package org.newtonproject.newtoncore.android.data.entity.request;

import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes16;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;

import java.math.BigInteger;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2019/4/17--9:28 PM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class GiveParams {

    public Utf8String message;
    public Uint256 redPocketCount;
    public Uint8 luckyType; // 0 is random, 1 is average.
    public BigInteger value;
    public Bytes16 giftId; // gift id

    public GiveParams(String message, int redPocketCount, int luckyType,byte[] bytes, BigInteger value) {
        this.message = new Utf8String(message);
        this.redPocketCount = new Uint256(BigInteger.valueOf(redPocketCount));
        this.luckyType = new Uint8(luckyType);
        this.value = value;
        this.giftId = new Bytes16(bytes);
    }

    @Override
    public String toString() {
        return "GiveParams{" +
                "message=" + message +
                ", redPocketCount=" + redPocketCount +
                ", luckyType=" + luckyType +
                ", value=" + value +
                ", giftId=" + giftId +
                '}';
    }
}
