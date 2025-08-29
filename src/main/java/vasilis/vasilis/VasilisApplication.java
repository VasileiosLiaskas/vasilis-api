package vasilis.vasilis;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VasilisApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(VasilisApplication.class, args);
//        fdsf
    }
    @Override
    public void run(String... args) {
        System.out.println("---- ENV VAR DEBUG ----");
        System.out.println("MYSQLHOST=" + System.getenv("MYSQLHOST"));
        System.out.println("MYSQLUSER=" + System.getenv("MYSQLUSER"));
        System.out.println("MYSQLDATABASE=" + System.getenv("MYSQLDATABASE"));
        System.out.println("MYSQLPORT=" + System.getenv("MYSQLPORT"));
        System.out.println("SPRING_PROFILES_ACTIVE=" + System.getenv("SPRING_PROFILES_ACTIVE"));
        System.out.println("------------------------");
    }

}
