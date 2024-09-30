package br.com.bruno.integrationtests.controller.withxml;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.bruno.configs.TestConfigs;
import br.com.bruno.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.bruno.integrationtests.vo.AccountCredentialsVO;
import br.com.bruno.integrationtests.vo.TokenVO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class AuthControllerXmlTest extends AbstractIntegrationTest {

	private static TokenVO tokenVO;

	@Test
	@Order(1)
	public void testSignin() throws JsonMappingException, JsonProcessingException {
		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "12345");

		tokenVO = given()
				.basePath("/auth/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_XML)
				.body(user)	
					.when()
				.post()
					.then()
						.statusCode(200)
							.extract()
							.body()
								.as(TokenVO.class);

		assertNotNull(tokenVO.getAccessToken());
		assertNotNull(tokenVO.getRefreshToken());
	}

	@Test
	@Order(2)
	public void testRefresh() throws JsonMappingException, JsonProcessingException {
		var newTokenVO = given()
				.basePath("/auth/refresh")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_XML)
						.pathParam("username", tokenVO.getUserName())
						.header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
					.when()
						.put("{username}")
					.then()
						.statusCode(200)
					.extract()
						.body()
							.as(TokenVO.class);

		assertNotNull(newTokenVO.getAccessToken());
		assertNotNull(newTokenVO.getRefreshToken());
	}
}
