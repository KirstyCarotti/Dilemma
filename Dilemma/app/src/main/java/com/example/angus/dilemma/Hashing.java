package com.example.angus.dilemma;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.*;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;



public class Hashing{


    byte[] salt = new byte[16];
    byte[] hash;

    public Hashing(String plain_Pass) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecureRandom rand = new SecureRandom();
        rand.nextBytes(salt);
        print(salt);

        KeySpec spec = new PBEKeySpec(plain_Pass.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        hash = factory.generateSecret(spec).getEncoded();
        print(hash);
    }

    public Hashing(String plain_Pass, byte[] salt) throws InvalidKeySpecException, NoSuchAlgorithmException{
        KeySpec spec = new PBEKeySpec(plain_Pass.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] hash = factory.generateSecret(spec).getEncoded();
        print(hash);
    }


    private void print(byte[] s) {

        for( int i=0;i<s.length;i++) {
            System.out.print(s[i]);
        }
        System.out.println(" ");
    }

    public byte[] getSalt(){
        return salt;
    }

    public byte[] getHash(){
        return hash;
    }

}

