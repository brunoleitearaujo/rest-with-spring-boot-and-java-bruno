package br.com.bruno.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bruno.data.vo.v1.security.AccountCredentialsVO;
import br.com.bruno.services.AuthServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	AuthServices authServices;

	@SuppressWarnings("rawtypes")
	@Operation(summary = "Authenticates a user and returns a token")
	@PostMapping(value = "/signin")
	public ResponseEntity signin(@RequestBody AccountCredentialsVO data) {
		if (data == null || data.getUserName() == null || data.getUserName().isBlank()
				|| data.getPassword() == null || data.getPassword().isBlank()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
		}

		var token = authServices.signin(data);
		if (token == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
		}

		return token;
	}

	@SuppressWarnings("rawtypes")
	@Operation(summary = "Refresh token for authenticated user and returns a token")
	@PutMapping(value = "/refresh/{username}")
	public ResponseEntity refreshToken(@PathVariable String username, @RequestHeader("Authorization") String refreshToken) {
		if (username == null || username.isBlank() || refreshToken == null || refreshToken.isBlank()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
		}

		var token = authServices.refreshToken(username, refreshToken);
		if (token == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
		}

		return token;
	}
}
