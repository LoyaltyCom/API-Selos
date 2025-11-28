package loyaltycom.api_selos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@ComponentScan
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class ApiSelosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiSelosApplication.class, args);
	}

}
