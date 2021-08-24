package agh.edu.pl.diet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan({"agh.edu.pl"})
public class DietApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DietApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }

}