package michalmlynarczyk.simplecrudlibrary.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookRepositoryTest {
    private final BookRepository underTest;

    @Autowired
    BookRepositoryTest(BookRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    void shouldFindBooksByAuthorFirstNameAndAuthorLastName() {
        //given
        String firstName = "John";
        String lastName = "Doe";
        List<Book> books = Arrays.asList(
                new Book("Test title",
                        firstName,
                        lastName,
                        1999),
                new Book("Test title 2",
                        firstName,
                        lastName,
                        2002));
        underTest.saveAll(books);

        //when
        List<Book> booksFromDb = underTest
                .findByAuthorFirstNameAndAuthorLastName(firstName, lastName);

        //then
        assertThat(booksFromDb).isNotEmpty();
        assertThat(booksFromDb.size()).isEqualTo(2);
        assertThat(booksFromDb.get(0).getAuthorFirstName()).isEqualTo(firstName);
        assertThat(booksFromDb.get(0).getAuthorLastName()).isEqualTo(lastName);
        assertThat(booksFromDb.get(1).getAuthorFirstName()).isEqualTo(firstName);
        assertThat(booksFromDb.get(1).getAuthorLastName()).isEqualTo(lastName);
    }

    @Test
    void shouldNotFindBooksByAuthorFirstNameAndAuthorLastNameWhileWrongInputProvided() {
        //given
        String firstName = "John";
        String lastName = "Doe";
        List<Book> books = Arrays.asList(
                new Book("Test title",
                        firstName,
                        lastName,
                        1999),
                new Book("Test title 2",
                        firstName,
                        lastName,
                        2002));
        underTest.saveAll(books);

        //when
        List<Book> booksFromDb = underTest
                .findByAuthorFirstNameAndAuthorLastName("NotJohn", "NotDoe");

        //then
        assertThat(booksFromDb).isEmpty();
    }

    @Test
    void shouldNotFindBooksByAuthorFirstNameAndAuthorLastNameWhileDatabaseIsEmpty() {
        //given
        String firstName = "John";
        String lastName = "Doe";

        //when
        List<Book> booksFromDb = underTest
                .findByAuthorFirstNameAndAuthorLastName(firstName, lastName);

        //then
        assertThat(booksFromDb).isEmpty();
    }
}