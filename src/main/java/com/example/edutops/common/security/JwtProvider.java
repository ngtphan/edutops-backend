package com.example.edutops.common.security;

import com.example.edutops.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Tiện ích sinh token JWT của hệ thống sử dụng Spring Security JwtEncoder (Nimbus).
 */
@Component
public class JwtProvider {

    private final JwtEncoder jwtEncoder;

    @Value("${app.jwt.expiration-ms:86400000}")
    private long jwtExpirationMs;

    public JwtProvider(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * Sinh token JWT cho User cụ thể.
     *
     * @param user Tài khoản người dùng
     * @return Chuỗi token JWT được ký số
     */
    public String generateToken(User user) {
        Instant now = Instant.now();
        Instant expiryDate = now.plus(jwtExpirationMs, ChronoUnit.MILLIS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("edutops")
                .issuedAt(now)
                .expiresAt(expiryDate)
                .subject(user.getEmail())
                .claim("publicId", user.getPublicId().toString())
                // Chèn tiền tố ROLE_ để Spring Security nhận dạng làm GrantedAuthority
                .claim("role", "ROLE_" + user.getRole().name())
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
