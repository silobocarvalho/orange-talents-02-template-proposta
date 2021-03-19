package br.com.zup.orange.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;

import org.bouncycastle.util.encoders.Hex;
import org.hibernate.id.UUIDGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

public class StringAttributeConverter implements AttributeConverter<String, String>{

	@Value("${crypto.secret}")
	private String secret;
	
	String salt = Hex.toHexString(new UUIDGenerator().toString().getBytes());
	
	@Override
	public String convertToDatabaseColumn(String attribute) {

		TextEncryptor encryptors = Encryptors.text(secret, salt);
        return encryptors.encrypt(attribute);
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		TextEncryptor encryptors = Encryptors.text(secret, salt);
        return encryptors.decrypt(dbData);
	}

}
