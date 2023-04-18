package com.wyl.super_robot.utils;

public class SM2KeyPair {

    public SM2KeyPair(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    /** 公钥 */
    private String publicKey;
    /** 私钥 */
    private String privateKey;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

}