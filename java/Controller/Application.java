package Controller;

import Services.PlannerService;
import Services.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;



import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"Services","Repository","Controller","SnapShot"})
public class Application {


	@Autowired
	private static PlannerService plannerService = new PlannerService();

	public static void main(String[] args) {
		plannerService.init();
		SpringApplication.run(Application.class, args);
	}



}
