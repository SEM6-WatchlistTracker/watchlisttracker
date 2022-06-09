package pl.piomin.services.service.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import pl.piomin.services.service.model.document.AuthUser;

import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service
public class TokenService {

    public String createToken(AuthUser user) throws UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        return JWT.create()
                .withIssuer("AuthenticationService")
                .withClaim("userId", user.getUserId().toString())
                .withClaim("role", user.getRole().toString())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .sign(algorithm);
    }
}
