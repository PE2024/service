package ro.unibuc.contact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import ro.unibuc.contact.data.InformationEntity;
import ro.unibuc.contact.data.InformationRepository;
import ro.unibuc.contact.data.UserRepository;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = {UserRepository.class, InformationRepository.class})
public class HelloApplication {

	@Autowired
	private InformationRepository informationRepository;

	public static void main(String[] args) {
		SpringApplication.run(HelloApplication.class, args);
	}

	@PostConstruct
	public void runAfterObjectCreated() {
		informationRepository.deleteAll();
		informationRepository.save(new InformationEntity("Overview",
				"This is an example of using a data storage engine running separately from our applications server"));
	}

}
