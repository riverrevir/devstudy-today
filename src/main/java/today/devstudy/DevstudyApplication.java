package today.devstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class DevstudyApplication {
	public static void main(String[] args) {
		SpringApplication.run(DevstudyApplication.class, args);
	}
}
