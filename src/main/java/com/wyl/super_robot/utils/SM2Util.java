package com.wyl.super_robot.utils;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.x9.X9ECPoint;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;

import com.alibaba.fastjson.JSON;

public class SM2Util {
    public static final String LICENSE_PUBLIC_KEY =
            "04bb3edd68e66fbc40223b647cf5122e4aaf9dca507f9bad519eb2624cf77a12e3629bc81b89f0e1773cb0aeab263cf299226e26f9ec6311960955d403ad3786b9";
    public static SM2KeyPair generatorSM2KeyPair() {
        ECDomainParameters domainParameters = getDomainParameters();

        ECKeyPairGenerator keyPairGenerator = new ECKeyPairGenerator();
        try {
            keyPairGenerator
                .init(new ECKeyGenerationParameters(domainParameters, SecureRandom.getInstance("SHA1PRNG")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = keyPairGenerator.generateKeyPair();

        BigInteger privatekey = ((ECPrivateKeyParameters)asymmetricCipherKeyPair.getPrivate()).getD();
        String privateKeyHex = privatekey.toString(16);

        ECPoint ecPoint = ((ECPublicKeyParameters)asymmetricCipherKeyPair.getPublic()).getQ();
        String publicKeyHex = Hex.toHexString(ecPoint.getEncoded(false));
        return new SM2KeyPair(publicKeyHex, privateKeyHex);
    }

    private static ECDomainParameters getDomainParameters() {
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
        ECDomainParameters domainParameters =
            new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());
        return domainParameters;
    }

    public static String deCode(String privateKeyHex, String cipherData) {
        ECDomainParameters domainParameters = getDomainParameters();
        BigInteger privateKeyD = new BigInteger(privateKeyHex, 16);
        ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(privateKeyD, domainParameters);
        SM2Engine sm2Engine = new SM2Engine();
        sm2Engine.init(false, privateKeyParameters);

        byte[] cipherDataByte = Hex.decode(cipherData);
        try {
            return new String(
                Base64.getDecoder().decode(sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String enCode(String publicKeyHex, String cipherData) {
        byte[] cipherDataByte = Base64.getEncoder().encode(cipherData.getBytes());
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
        ECDomainParameters domainParams =
            new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());

        X9ECPoint localX9ECPoint = new X9ECPoint(sm2ECParameters.getCurve(), Hex.decode(publicKeyHex.getBytes()));
        ECPublicKeyParameters pk = new ECPublicKeyParameters(localX9ECPoint.getPoint(), domainParams);
        CipherParameters pubKeyParameters = new ParametersWithRandom(pk);
        SM2Engine sm2Engine = new SM2Engine();
        sm2Engine.init(true, pubKeyParameters);
        try {

            return Hex.toHexString(sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length));
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
        }

        return null;
    }


}