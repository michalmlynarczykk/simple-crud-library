package michalmlynarczyk.simplecrudlibrary.config;

import michalmlynarczyk.simplecrudlibrary.appuser.AppUser;
import michalmlynarczyk.simplecrudlibrary.appuser.AppUserService;
import michalmlynarczyk.simplecrudlibrary.book.Book;
import michalmlynarczyk.simplecrudlibrary.book.BookService;
import michalmlynarczyk.simplecrudlibrary.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class InitConfig {
    private final BookService bookService;
    private final AppUserService appUserService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public InitConfig(BookService bookService, AppUserService appUserService, PasswordEncoder passwordEncoder) {
        this.bookService = bookService;
        this.appUserService = appUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner run() {
        return args -> {

            bookService.saveBook(new Book("In Desert and Wilderness",
                    "Henryk",
                    "Sienkiewicz",
                    1911));
            bookService.saveBook(new Book("Quo Vadis",
                    "Henryk",
                    "Sienkiewicz",
                    1896));
            bookService.saveBook(new Book("Pan Tadeusz",
                    "Adam",
                    "Mickiewicz",
                    1834));

            String password = passwordEncoder.encode("password");

            appUserService.saveRole(new Role("ROLE_ADMIN"));
            appUserService.saveRole(new Role("ROLE_READER"));

            appUserService.saveUser(new AppUser("admin", password));
            appUserService.saveUser(new AppUser("reader", password));

            appUserService.connectUserWithRole("admin", "ROLE_ADMIN");
            appUserService.connectUserWithRole("reader", "ROLE_READER");
        };
    }
}
