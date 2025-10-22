package vasilis.vasilis;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@SpringBootApplication
public class VasilisApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(VasilisApplication.class, args);
//        fdsf
    }
    @Override
    public void run(String... args) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        SecretKey key = keyGen.generateKey();
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("Your secret key: " + base64Key);
        System.out.println("---- ENV VAR DEBUG ----");
        System.out.println("MYSQLHOST=" + System.getenv("MYSQLHOST"));
        System.out.println("MYSQLUSER=" + System.getenv("MYSQLUSER"));
        System.out.println("MYSQLDATABASE=" + System.getenv("MYSQLDATABASE"));
        System.out.println("MYSQLPORT=" + System.getenv("MYSQLPORT"));
        System.out.println("SPRING_PROFILES_ACTIVE=" + System.getenv("SPRING_PROFILES_ACTIVE"));
        System.out.println("------------------------");
    }

}
