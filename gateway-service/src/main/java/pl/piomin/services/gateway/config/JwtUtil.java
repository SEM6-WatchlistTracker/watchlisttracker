package pl.piomin.services.gateway.config;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;

@Component
public class JwtUtil {

    public DecodedJWT verifyAndGetClaims(String token) throws UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("AuthenticationService")
                .build();
        return verifier.verify(token);
    }

    public String getUserIdFromToken(DecodedJWT jwt) throws UnsupportedEncodingException {
        String userId = jwt.getClaim("userId").toString();
        return userId;
    }
}
