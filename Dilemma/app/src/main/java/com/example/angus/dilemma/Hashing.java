package com.example.angus.dilemma;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.*;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class  Hashing{

    private byte[] salt = new byte[16];
    private byte[] hash;

    //Called when registering
    public Hashing(String plain_Pass) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecureRandom rand = new SecureRandom();
        rand.nextBytes(salt);
        //print(salt);

        KeySpec spec = new PBEKeySpec(plain_Pass.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        hash = factory.generateSecret(spec).getEncoded();
        //print(hash);
    }

    //Called when logging in
    public Hashing(String plain_Pass, byte[] theSalt) throws InvalidKeySpecException, NoSuchAlgorithmException{
        KeySpec spec = new PBEKeySpec(plain_Pass.toCharArray(), theSalt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        hash = factory.generateSecret(spec).getEncoded();
        //print(hash);
    }


    private String print(byte[] s) {
        String temp= "";
        for(int i=0; i<s.length; i++) {
            temp += (char) s[i];
        }
        return temp;
    }

    public String getSalt(){
        return print(salt);
    }

    public String getHash(){
        return print(hash);
    }

}

