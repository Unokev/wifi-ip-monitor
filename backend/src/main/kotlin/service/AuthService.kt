package service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Service
import java.util.Date
import java.util.UUID

@Service
class AuthService {

    private val jwtSecret: String = "your_secret_key"
    private val jwtExpiration: Long = 86400000 // 24 hours

    fun generateToken(username: String): String {
        val claims: Claims = Jwts.claims().setSubject(username)
        val now: Date = Date()
        val expiration: Date = Date(now.time + jwtExpiration)

        return Jwts.builder()
            .setClaims(claims)
            .setId(UUID.randomUUID().toString())
            .setIssuedAt(now)
            .setExpiration(expiration)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            val claims: Claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .body
            !claims.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }
}