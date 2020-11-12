package com.cash4books.cash4books;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


//TODO logging functionality
@SpringBootApplication
public class Cash4booksApplication {

	public static void main(String[] args) {
		SpringApplication.run(Cash4booksApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
