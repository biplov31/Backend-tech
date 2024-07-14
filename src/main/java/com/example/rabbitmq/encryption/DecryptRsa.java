package com.example.rabbitmq.encryption;

import com.example.rabbitmq.persistence.StorageService;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class DecryptRsa {

    private PrivateKey privateKey;
    private PublicKey publicKey;
    private final int KEY_SIZE = 2048;

    private StorageService storageService;

    public DecryptRsa(StorageService storageService) {
        this.storageService = storageService;

        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(KEY_SIZE);
            KeyPair pair = generator.generateKeyPair();
            // privateKey = pair.getPrivate();
            // publicKey = pair.getPublic();

            privateKey = (PrivateKey) decodeStringAsPrivateKey("MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCzk5BMMRPJl+KoTDHphr/5aGb1Dq+U+ooxJOyVBrq79InHaO6KTuJCmB6hcP5mHzVBa+cG/f1uMotvVOOOwWB5Q+YT91CpcGwQwYtuUdpKQV/vd5WY8HEL6TPQEeX4j+K/c+CSz4qOrqIqyEtSwKzrP+TMX8S7iYLiPicD2ivpp7KyHDFFXJ9nA+grBNd3PkicO7O1EJjLHGRejZw9ht6gmRWyciZBIUYznXgsOKn5BnGQOjWH0UdMkksTil/s36IzWHr/Ew97gyTLryimg9fu3X2tR357iGpiPer1U6JUJHIA/EoYr4mmX6Kw2+drMmAmoGDfcJwxvHTa37tltB+lAgMBAAECggEABib6OC11sM2y5ql2wpWPZwkQyDjQubkzGfns31bBiiqp4Jql28pWEmW3gTgnqoRLe8pyufQrs906lpABQPc7XDDJLrfu0s9Efa3mHklQeM1eAVWP7TfgwMGSJrfBsM8S4F23PPBjC9nMFV7cMlhP1EyazfZCj6P5tT7fleKhMRWg2/msZ+1LRuNxvcVHGKO1/+BcQluLsCbBacZZ6FX7QuAamaRhJW6FS8TKWxSbzAvldgVsQBC2zQ1LnRcjc+HBfgMO6Jg9S8cG/eUXA65IVC5gx1oCuCzcX8pYQdiK1DY6LdhZzUhaGD7TMs/z4qVHX0B1xl9vSPSHcyfHY6EbBQKBgQDnR0/Na9+pQC/A5KBMoHUQN+sUUkF97lzz77BWilhYGewQAf1hzMBXZXaLC+Yy7WmZp9CkVFLQahKzBvpALqT/hj4qY4ICG5RVjrvWuIa3GbZ0L9kyuEjxhxVBq5bKT6Cf5OYKv0VEDEUFypnV3CNFjYpxXUlJr0+QkT8BkMNYYwKBgQDGxXqIDgCo/Y3MaWj0juVkF0veB5GtZFgwknbjkTepAgyUQV+mS1aKHKJw5Td5K2jPajWLbamxJDGQL9KIpwLShkY8lI/og0o+n/0zcrxIxFw9Epay2Fs2lkakaZZkYkdx78Fk5w4eo0yycOrt3ljWjQ036Wma4cO1wYPEy8dyVwKBgCMd6dkYGxLY4ydXz5sy9fNrei9Qug8EBWsops7NHNLrxLM9ihqyezYJ+vDr8p0i/VjOaVi6UQTCRmqWkaLzMix2VrQa/d+SDnjbPlxBEnt01QZZSZJw9upth2W8Rx4a7xm4KNHS7xKBBm31sFwPm+9wqWfDMWCV1O/vjfCP6YlfAoGAbRJMDIByiEiqGLL27svCwEuwS+OnYpgfdHfKWVGEU29Wa7v7uoq4sAzKbly5NfpGBxrmyt2gMh4EPHSheG78s30O6Ysz9IimovqzvBmHi/6EBtc+bmEenDOWC/4MkoUDY1gGrVHEg7dkDL69i01pPkIkMLwaQO0FsaTAo6qWhP0CgYAVmI6bVcuYPDcTvCWdpNlHDjnwGN2jvSTkQEsSlgc7/jrZEhy00NIDfFqfaMwr4XDbQ9YJbnn9JR3V+d8N9PAGFGmI+fc4hY+HkQr0PBhTAaKaLGOV3E6QCS+Krn4H0Wn/zaDdP5u3KqChIfbi3Hs6lWv2xzQmonJWw8iA5WKcEg==");
            publicKey = (PublicKey) decodeStringAsPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAs5OQTDETyZfiqEwx6Ya/+Whm9Q6vlPqKMSTslQa6u/SJx2juik7iQpgeoXD+Zh81QWvnBv39bjKLb1TjjsFgeUPmE/dQqXBsEMGLblHaSkFf73eVmPBxC+kz0BHl+I/iv3Pgks+Kjq6iKshLUsCs6z/kzF/Eu4mC4j4nA9or6aeyshwxRVyfZwPoKwTXdz5InDuztRCYyxxkXo2cPYbeoJkVsnImQSFGM514LDip+QZxkDo1h9FHTJJLE4pf7N+iM1h6/xMPe4Mky68opoPX7t19rUd+e4hqYj3q9VOiVCRyAPxKGK+Jpl+isNvnazJgJqBg33CcMbx02t+7ZbQfpQIDAQAB");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    // cannot have two constructors doing different initializations
    // @Autowired
    // public DecryptRsa(StorageService storageService) {
    //     this.storageService = storageService;
    // }

    private String encodeKeyAsString(PrivateKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    private Key decodeStringAsPrivateKey(String encodedPrivate) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Base64.getDecoder().decode(encodedPrivate);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(keyBytes);

        return keyFactory.generatePrivate(privateKeySpec);
    }

    private Key decodeStringAsPublicKey(String encodedPublic) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Base64.getDecoder().decode(encodedPublic);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(keyBytes);

        return keyFactory.generatePublic(publicKeySpec);
    }

    public String encrypt(String message) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] messageInBytes = message.getBytes(StandardCharsets.UTF_8); // converts the String into a sequence of bytes using the UTF-8 character encoding
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(messageInBytes);

        return encode(encryptedBytes);
    }

    private String encode(byte[] dataInBytes) {
        return Base64.getEncoder().encodeToString(dataInBytes);
    }

    public String decrypt(String encryptedMessage) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        // since we have encoded the message after encryption, we need to decode it first and then decrypt
        byte[] encryptedBytes = decode(encryptedMessage);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedMessage = cipher.doFinal(encryptedBytes);

        return new String(decryptedMessage, StandardCharsets.UTF_8);
    }

    private byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    // To use Spring's dependency injection in a main method, you'll need to bootstrap the Spring application context and get the bean from there.
    // public static void main(String[] args) {
    //
    //     // Student student = new Student("John", "Doe", 19, "john@gmail.com");
    //
    //     // attributes to be encrypted can be prefixed as "enc_name" (at the DTO level) so it is easier to track
    //
    //     Student encryptedStudent = new Student();
    //     encryptedStudent.setFirstName("fN2T6mrPzLlxPUPyvZcdyPI1bvG1pS7ErvJfo274gtyZYGGuyQdlwizmA7PtC3iHwlQfS1d48vP/Xr/YRbn9eMocVxkm0iyF/Erb50oI+4JU5oKj/FVhgUIH4omTT0wthGX6jiRZ+4VoSl5wdS4TJIdzxm0Ch+q0Z1lxrbNqZFy/GYwI6FcqdxFZk30PBiU82RvTmofLekpy4qYwYtliXWDayC7ICfftR4sdJEfPRR68rvyRrqsuQsLp5M2CmCADvVc45/4h0RpDgqEWJh5h4P9iEitCxK4XV480/SP4t7XLoUTZdgdhBzHmHTtU57BGVD2LyVFJt78/9FkM1BiBYA==");
    //     encryptedStudent.setLastName("Doe");
    //     encryptedStudent.setAge(19);
    //     encryptedStudent.setEmail("ggQ096KjwKz2ULuHHchwgW+knNVNdvmpq9cYZDydnCztZwD24nX1A2zQ8HCOX/niaYJpqcmpWeXxU7uyELiftRfyecbH1y4aZrAND/Flb72kO/TZCBZpwo9V/s6PMj6MXkSFUW9vYw8Io6EHHfmTTIbPYLrZHBm1gHN2iKutxdYZcOtIHAwEXTIjXPrQ45VgGwY9z6DiZHrp3CdY05GMdhRypOU424mJvjRT71DIuRVJfy2GGk/EK5mJlgWQgZbcDemf15RMHGpUCpp3I7VU7c2BgBnl2b+H8mcFd4Nj8N9OP1H9qslEE9lrXPIEwQYYW0L4LPSFlIL85cELfgRqzw==");
    //
    //     DecryptRsa rsa = new DecryptRsa();
    //
    //     try {
    //         // System.out.println("Private key: " + rsa.encode(rsa.privateKey.getEncoded()));
    //         // System.out.println("Public key: " + rsa.encode(rsa.publicKey.getEncoded()));
    //
    //         // String originalEmail = student.getEmail();
    //         // String encryptedEmail = rsa.encrypt(originalEmail);
    //         // String decryptedEmail = rsa.decrypt(encryptedEmail);
    //
    //         // System.out.println("Encrypted email: " + encryptedEmail);
    //         // System.out.println("Decrypted email: " + decryptedEmail);
    //
    //         String decryptedFirstname = rsa.decrypt(encryptedStudent.getFirstName());
    //         String decryptedEmail = rsa.decrypt(encryptedStudent.getEmail());
    //         System.out.println("Decrypted student firstname: " + decryptedFirstname);
    //         System.out.println("Decrypted student email: " + decryptedEmail);
    //
    //         Student decryptedStudent = new Student(decryptedFirstname, encryptedStudent.getLastName(), encryptedStudent.getAge(), decryptedEmail);
    //         // storageService.saveStudent(decryptedStudent);
    //
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

}
