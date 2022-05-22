package com.watchlisttracker.wltgateway;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
// import org.springframework.http.HttpStatus;
// import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
// import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WltGatewayApplicationTests {

	@LocalServerPort
	int port;
	private WebTestClient client;

	@BeforeEach
	public void setup() {
		client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void pathRouteWorks() {
		client.get().uri("/get")
				.exchange()
				.expectStatus().isOk()
				.expectBody(Map.class)
				.consumeWith(result -> {
					assertThat(result.getResponseBody()).isNotEmpty();
				});
	}

	@Test
	@SuppressWarnings("unchecked")
	public void hostRouteWorks() {
		client.get().uri("/headers")
				.header("Host", "www.watchlisttracker.org")
				.exchange()
				.expectStatus().isOk()
				.expectBody(Map.class)
				.consumeWith(result -> {
					assertThat(result.getResponseBody()).isNotEmpty();
				});
	}

	@Test
	@SuppressWarnings("unchecked")
	public void rewriteRouteWorks() {
		client.get().uri("/foo/get")
				.header("Host", "www.rewrite.org")
				.exchange()
				.expectStatus().isOk()
				.expectBody(Map.class)
				.consumeWith(result -> {
					assertThat(result.getResponseBody()).isNotEmpty();
				});
	}

	// ...

}
