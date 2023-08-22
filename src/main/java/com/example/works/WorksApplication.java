package com.example.works;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WorksApplication {

	public static void main(String[] args) {

			SpringApplication.run(WorksApplication.class, args);

		/*ValidadorService validadorService = new ValidadorService();
		ProcesadorService procesadorService = new ProcesadorService(validadorService);
		procesadorService.procesarArchivo("C:/Users/garde/Downloads/people.csv");*/
	}
}
