package com.nat.geeklolspring.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.SecureRandom;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TokenProviderTest {

    @Test
    @DisplayName("토큰 비밀키")
    void makeSecretkey() {
        SecureRandom random = new SecureRandom();

        byte[] key = new byte[64];
        random.nextBytes(key);

        String encoded = Base64.getEncoder().encodeToString(key);

        System.out.println("\n\n\n\n\n\n");
        System.out.println(encoded);
        System.out.println("\n\n\n\n\n\n");

    }

}