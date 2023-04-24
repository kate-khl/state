package com.example.state;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.statemachine.config.EnableStateMachineFactory;

@SpringBootApplication
@EnableJpaRepositories(basePackages = { "com.example.state" })
public class StateApplication {

	public static void main(String[] args) {
		SpringApplication.run(StateApplication.class, args);
	}
	
	
	public enum States {
	    РАБОТАЮ, ПЬЮ_ПИВО, КОВЫРЯЮ_В_НОСУ
	}

	public enum Events {
	    НАСТУПИЛ_ПОНЕДЕЛЬНИК, НАСТУПИЛ_ВТОРНИК, НАСТУПИЛА_ПЯТНИЦА 
	}

}
