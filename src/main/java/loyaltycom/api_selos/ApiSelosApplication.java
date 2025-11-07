package loyaltycom.api_selos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class ApiSelosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiSelosApplication.class, args);
	}

}
