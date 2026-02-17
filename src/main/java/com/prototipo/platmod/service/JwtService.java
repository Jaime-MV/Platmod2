package com.prototipo.platmod.service;

import com.prototipo.platmod.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private String lastValidationFailure = ""; // Debugging only

    public String getLastValidationFailure() {
        return lastValidationFailure;
    }

    // Clave de ejemplo. En producción usa variables de entorno.
    private static final String SECRET_KEY = "mi_clave_secreta_super_segura_y_larga_para_firmar_tokens_12345";

    // 1. GENERAR TOKEN
    public String generateToken(Usuario usuario) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("rol", usuario.getRol());

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(usuario.getCorreo())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 2. VALIDAR TOKEN (¡ESTO TE FALTABA!)
    public boolean isTokenValid(String token, UserDetails userDetails) {
        lastValidationFailure = "";
        final String username = extractUsername(token);

        boolean usernameMatch = username.equals(userDetails.getUsername());
        if (!usernameMatch) {
            lastValidationFailure = "Username mismatch: Token=" + username + " vs DB=" + userDetails.getUsername();
            return false;
        }

        boolean expired = isTokenExpired(token);
        if (expired) {
            lastValidationFailure = "Token expired";
            return false;
        }

        return true;
    }

    // 3. EXTRAER USUARIO (USERNAME)
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 4. MÉTODOS AUXILIARES
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Key getSignInKey() {
        // Aseguramos que la clave sea decodificable
        byte[] keyBytes = Decoders.BASE64.decode(Base64.getEncoder().encodeToString(SECRET_KEY.getBytes()));
        return Keys.hmacShaKeyFor(keyBytes);
    }
}