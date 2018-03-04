package friend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import friend.api.RelationshipApi;
import friend.api.RelationshipApiImpl;

@SpringBootApplication
public class ServerStarter {
	
	@Bean
	public RelationshipApi getRelationshipApi(){
		return new RelationshipApiImpl();
	}

	public static void main(String[] args) {
		SpringApplication.run(ServerStarter.class, args);
	}

}
