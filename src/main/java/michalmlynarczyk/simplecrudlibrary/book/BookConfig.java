package michalmlynarczyk.simplecrudlibrary.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;


@Configuration
public class BookConfig {
    private final BookRepository bookRepository;

    @Autowired
    public BookConfig(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Bean
    CommandLineRunner run() {
        return args -> bookRepository.saveAll(
                Arrays.asList(
                        new Book("In Desert and Wilderness",
                                "Henryk",
                                "Sienkiewicz",
                                1911),
                        new Book("Quo Vadis",
                                "Henryk",
                                "Sienkiewicz",
                                1896),
                        new Book("Pan Tadeusz",
                                "Adam",
                                "Mickiewicz",
                                1834))
        );
    }
}
