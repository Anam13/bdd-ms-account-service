package com.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Accounts API", version = "2.0", description = "Accounts Information: Users to login =[user1:password1,user2:password2,user3:password3]"))
@SecurityScheme(name = "Authorization", scheme = "bearer", bearerFormat = "JWT", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)

public class BddMsAccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BddMsAccountServiceApplication.class, args);
	}

}
