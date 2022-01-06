package com.group3sc2.cyactivity;

import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.io.File;

import static com.group3sc2.cyactivity.constant.FileConstant.USER_FOLDER;

@SpringBootApplication
public class CyactivityApplication {

	public static void main(String[] args) {
		SpringApplication.run(CyactivityApplication.class, args);
		new File(USER_FOLDER).mkdirs();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	public OpenAPI openApiConfig(){
		return new OpenAPI().info(apiInfo());
	}

	public Info apiInfo(){
		Info info = new Info();
		info.title("CyActivity").description("Find people sharing what you love doing").version("v1.0.0");
		return info;
	}

}
