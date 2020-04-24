package org.newtonproject.newtoncore.android.utils.crypto;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/12/1--10:38 AM
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
import org.spongycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.spongycastle.jce.ECNamedCurveTable;
import org.spongycastle.jce.ECPointUtil;
import org.spongycastle.jce.spec.ECNamedCurveParameterSpec;
import org.spongycastle.jce.spec.ECParameterSpec;
import org.spongycastle.jce.spec.ECPrivateKeySpec;
import org.spongycastle.math.ec.ECCurve;
import org.newtonproject.web3j.utils.Numeric;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

public class Sepc128r1Curve {
    private static final String CURVE = "secp128r1";
    private static byte[] derEncodeSignature(final byte[] signature) {
        // split into r and s
        final byte[] r = Arrays.copyOfRange(signature, 0, 16);
        final byte[] s = Arrays.copyOfRange(signature, 16, 32);
        int rLen = r.length;
        int sLen = s.length;
        if ((r[0] & 0x80) != 0) {
            rLen++;
        }
        if ((s[0] & 0x80) != 0) {
            sLen++;
        }
        final byte[] encodedSig = new byte[rLen + sLen + 6]; // 6 T and L bytes
        encodedSig[0] = 0x30; // SEQUENCE
        encodedSig[1] = (byte) (4 + rLen + sLen);
        encodedSig[2] = 0x02; // INTEGER
        encodedSig[3] = (byte) rLen;
        encodedSig[4 + rLen] = 0x02; // INTEGER
        encodedSig[4 + rLen + 1] = (byte) sLen;
        // copy in r and s
        encodedSig[4] = 0;
        encodedSig[4 + rLen + 2] = 0;
        System.arraycopy(r, 0, encodedSig, 4 + rLen - r.length, r.length);
        System.arraycopy(s, 0, encodedSig, 4 + rLen + 2 + sLen - s.length, s.length);
        return encodedSig;
    }

    private static byte[] derDecodeSignature(final byte[] encodedSig) {
        if (encodedSig[0] != 0x30) {
            return null;
        }
        if (encodedSig[2] != 0x02) {
            return null;
        }
        int rLen = encodedSig[3];
        if (encodedSig[4 + rLen] != 0x02) {
            return null;
        }
        int sLen = encodedSig[4 + rLen + 1];
        if (encodedSig[1] != 4 + rLen + sLen) {
            return null;
        }
        byte[] signature = new byte[32];
        System.arraycopy(encodedSig, 4 + rLen - 16, signature, 0, 16);
        System.arraycopy(encodedSig, 4 + rLen + 2 + sLen - 16, signature, 16, 16);
        return signature;

    }

    public static KeyPair GenerateKeys()
            throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp128r1");
        KeyPairGenerator g = KeyPairGenerator.getInstance("ECDSA", "SC");
        g.initialize(ecSpec, new SecureRandom());
        return g.generateKeyPair();
    }

    public static PrivateKey MakePrivateKeyFromHexString(String hexString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory eckf = null;
        try {
            eckf = KeyFactory.getInstance("EC","SC");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        ECNamedCurveParameterSpec parameterSpec = ECNamedCurveTable.getParameterSpec("secp128r1");
        ECParameterSpec spec = new ECParameterSpec(parameterSpec.getCurve(), parameterSpec.getG(), parameterSpec.getN(), parameterSpec.getH(), parameterSpec.getSeed());
        ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(Numeric.toBigInt(hexString), spec);
        return eckf.generatePrivate(ecPrivateKeySpec);

    }

    public static PublicKey MakePublicKeyFromHexString(byte[] encoded) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
        ECNamedCurveParameterSpec params = ECNamedCurveTable.getParameterSpec(CURVE);
        KeyFactory fact = KeyFactory.getInstance("ECDSA", "SC");
        ECCurve curve = params.getCurve();
        java.security.spec.EllipticCurve ellipticCurve = EC5Util.convertCurve(curve, params.getSeed());
        java.security.spec.ECPoint point = ECPointUtil.decodePoint(ellipticCurve, encoded);
        java.security.spec.ECParameterSpec params2 = EC5Util.convertSpec(ellipticCurve, params);
        java.security.spec.ECPublicKeySpec keySpec = new java.security.spec.ECPublicKeySpec(point, params2);
        return fact.generatePublic(keySpec);
    }

    public static byte[] Sign(PrivateKey privateKey, byte[] data) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature ecdsaSign = Signature.getInstance("NONEwithECDSA", "SC");
        ecdsaSign.initSign(privateKey);
        ecdsaSign.update(data);
        byte[] signature = ecdsaSign.sign();
        return derDecodeSignature(signature);
    }

    public static boolean Verify(PublicKey publicKey, byte[] data, byte[] signature) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature ecdsaVerify = Signature.getInstance("NONEwithECDSA", "SC");
        ecdsaVerify.initVerify(publicKey);
        ecdsaVerify.update(data);
        return ecdsaVerify.verify(derEncodeSignature(signature));
    }

    static {
        /*if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }*/
        if (Security.getProvider("SC") == null) {
            Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
        }
    }
}