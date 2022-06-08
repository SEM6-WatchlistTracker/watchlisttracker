package pl.piomin.services.gateway.config;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.auth0.jwt.interfaces.DecodedJWT;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilterFactory implements GatewayFilterFactory<AuthenticationFilterFactory.Config> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilterFactory.class);
    @Autowired private JwtUtil jwtUtil;

    @Override
	public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            LOGGER.info("Received request.");
            ServerHttpRequest request = exchange.getRequest();

            if (isSecured(request)) {
                if (this.isAuthMissing(request)) return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);
                final String token = this.getAuthHeader(request);
                String jwtToken = token.split(" ")[1].trim();
                DecodedJWT jwt = null;
                try {
                    jwt = jwtUtil.verifyAndGetClaims(jwtToken);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                this.populateRequestWithHeaders(exchange, jwt);
            }
            return chain.filter(exchange);
		});
	}

    private boolean isSecured(ServerHttpRequest request) {
        final String[] openApiEndpoints = new String[]{
                "/auth/signIn",
                "/auth/signUp"
        };
        return !Arrays.asList(openApiEndpoints).contains(request.getPath().toString());
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, DecodedJWT jwt) {
        String userId = jwt.getClaim("userId").toString();
        exchange.getRequest().mutate()
                .header("userId", userId)
                .build();
    }

	@Override
	public Class<Config> getConfigClass() {
		return Config.class;
	}

	@Override
	public Config newConfig() {
		Config c = new Config();
		return c;
	}

	public static class Config {}
}
