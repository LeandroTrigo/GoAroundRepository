package e.utilizador.around;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

final public class ChCrypto {
    final static Base64.Encoder encorder = Base64.getEncoder();
    final static Base64.Decoder decorder = Base64.getDecoder();


    static private Cipher cipher(int opmode, String secretKey) throws Exception{
        if(secretKey.length() != 32) throw new RuntimeException("SecretKey length is not 32 chars");
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec sk = new SecretKeySpec(secretKey.getBytes(), "AES");
        String ivstring = "hidjY7dndihdUY65";
        IvParameterSpec iv = new IvParameterSpec(ivstring.getBytes());
        c.init(opmode, sk, iv);
        return c;
    }
    static public String encrypt(String str, String secretKey){
        try{
            byte[] encrypted = cipher(Cipher.ENCRYPT_MODE, secretKey).doFinal(str.getBytes("UTF-8"));
            return new String(encorder.encode(encrypted));
        }catch(Exception e){
            return null;
        }
    }
    static public String decrypt(String str, String secretKey){
        try{
            byte[] byteStr = decorder.decode(str.getBytes());
            return new String(cipher(Cipher.DECRYPT_MODE, secretKey).doFinal(byteStr),"UTF-8");
        }catch(Exception e){
            return null;
        }
    }
}