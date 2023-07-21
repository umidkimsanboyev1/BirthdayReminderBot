package uz.masters.springbootbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uz.masters.springbootbot.services.TimeService;

@SpringBootApplication
public class SpringbootbotApplication {

	@Autowired
	TimeService timeService;

	public static void main(String[] args) {

		SpringApplication.run(SpringbootbotApplication.class, args);
	}

}
