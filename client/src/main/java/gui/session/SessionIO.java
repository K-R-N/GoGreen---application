package gui.session;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Class to handle input output operations for the SessionID.
 */
public class SessionIO {

    /**
     * Makes an Session object and places string in it.
     * Writes an Session object to file.
     * @param sessionID String which will be placed in an Session Object and written to a file.
     */
    public static void write(String sessionID) {
        try {
            String key = LocalMac.macToKey();
            if (key.isBlank()) {
                System.out.println("Couldn't get key for writing stored session");
                return;
            }
            String initV = "#GoGreen-OOPP90!";
            String storedString = null;

            if (sessionID != null) {
                storedString = encrypt(key, initV, sessionID);
            }

            Session session = new Session();
            session.setStoredString(storedString);
            File file = new File("client\\src\\main\\java\\gui\\session\\store.xml".replaceAll(
                    "[/\\\\]+", Matcher.quoteReplacement(System.getProperty("file.separator")))
            );
            JAXBContext jaxbContext = JAXBContext.newInstance(Session.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(session, file);

        } catch (JAXBException e) {
            System.out.println("Can't write the stored session to file.");
        }

    }

    /**
     * Reads an Session object from file and gets the session object out of it.
     * Returns String from that session object.
     */
    public static String read() {
        try {
            File file = new File("client\\src\\main\\java\\gui\\session\\store.xml".replaceAll(
                    "[/\\\\]+", Matcher.quoteReplacement(System.getProperty("file.separator")))
            );
            JAXBContext jaxbContext = JAXBContext.newInstance(Session.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            if (!file.exists()) {
                return null;
            } else if (!file.canRead()) {
                return null;
            } else {
                Session session = (Session) jaxbUnmarshaller.unmarshal(file);
                String sessionID = null;

                if (session.getStoredString() != null) {
                    String key = LocalMac.macToKey();
                    if (key.isBlank()) {
                        System.out.println("Couldn't get key for reading stored session");
                        return null;
                    }
                    String initV = "#GoGreen-OOPP90!";
                    sessionID = decrypt(key, initV, session.getStoredString());
                }
                
                return sessionID;
            }

        } catch (JAXBException e) {
            System.out.println("Can't read the stored session");
            return null;
        }
    }

    /**
     * Encrypts a String with help of a other String.
     * @param key String containing the key for encrypting the value string.
     * @param initV String containing the Initialization Vector .
     * @param value String you want to encrypt.
     * @return Encrypted String
     * @throws NoSuchAlgorithmException for when the algorithm couldn't be found.
     * @throws UnsupportedEncodingException for when encoding type is not supported.
     * @throws IllegalBlockSizeException for when input is not a multiple of 8 bytes.
     * @throws BadPaddingException for when padding is not multiple of 8 bytes.
     * @throws InvalidAlgorithmParameterException for when algorithm doesn't
     *          have the right parameters.
     * @throws InvalidKeyException for when key is not in the right format.
     */
    private static String encrypt(String key, String initV, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initV.getBytes("UTF-8"));
            SecretKeySpec secret = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secret, iv);
            byte[] encrypted = cipher.doFinal(value.getBytes());

            return Base64.encodeBase64String(encrypted);

        } catch (NoSuchAlgorithmException e) {
            System.out.println("Algorithm used for encrypting doesn't exist");
        }   catch (NoSuchPaddingException e) {
            System.out.println("Padding use for encrypting is incorrect");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Encoding typ used for encrypting is unsupported");
        } catch (IllegalBlockSizeException e) {
            System.out.println("Block size used for encrypting is not multiple of 8 bytes");
        } catch (BadPaddingException e) {
            System.out.println("Padding used for encrypting is not multiple of 8 bytes");
        } catch (InvalidAlgorithmParameterException e) {
            System.out.println("Algorithm parameters used for encrypting is incorrect");
        } catch (InvalidKeyException e) {
            System.out.println("Key used for encrypting is in incorrect format");
        }
        return null;
    }

    /**
     * Decrypts a String with help of a other String.
     * @param key String containing the key for decrypting the encrypted string.
     * @param initV String containing the Initialization Vector .
     * @param encrypted String containing the encrypted string.
     * @return Decrypted string.
     * @throws NoSuchAlgorithmException for when the algorithm couldn't be found.
     * @throws UnsupportedEncodingException for when encoding type is not supported.
     * @throws IllegalBlockSizeException for when input is not a multiple of 8 bytes.
     * @throws BadPaddingException for when padding is not multiple of 8 bytes.
     * @throws InvalidAlgorithmParameterException for when algorithm doesn't
     *          have the right parameters.
     * @throws InvalidKeyException for when key is not in the right format.
     */
    private static String decrypt(String key, String initV, String encrypted) {
        try {
            SecretKeySpec secret = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            IvParameterSpec iv = new IvParameterSpec(initV.getBytes("UTF-8"));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secret, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
            return new String(original);

        } catch (NoSuchAlgorithmException e) {
            System.out.println("Algorithm used for decrypting doesn't exist ");
        }   catch (NoSuchPaddingException e) {
            System.out.println("Padding used for decrypting is incorrect");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Encoding type used for decrypting is unsupported");
        } catch (IllegalBlockSizeException e) {
            System.out.println("Block size used for decrypting is not multiple of 8 bytes");
        } catch (BadPaddingException e) {
            System.out.println("Padding used for decrypting is not multiple of 8 bytes");
        } catch (InvalidAlgorithmParameterException e) {
            System.out.println("Algorithm parameters used for decrypting is incorrect");
        } catch (InvalidKeyException e) {
            System.out.println("Key used for decrypting is in incorrect format");
        }

        return null;
    }





}
