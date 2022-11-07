package michalmlynarczyk.simplecrudlibrary.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private BookRepository repository;

    private BookService underTest;

    @BeforeEach
    void setUp() {
        underTest = new BookServiceImpl(repository);
    }

    @Test
    void shouldSaveBook() {
        //given
        Book book = new Book(
                "Test title",
                "John",
                "Doe",
                1992);

        //when
        underTest.saveBook(book);

        //then
        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(repository).save(captor.capture());
        Book capturedBook = captor.getValue();

        assertThat(book).isEqualTo(capturedBook);
    }

    @Test
    void shouldGetAllBooks() {
        //given
        List<Book> books = List.of(
                new Book("Test title",
                        "John",
                        "Doe",
                        1999),
                new Book("Test title 2",
                        "Jan",
                        "Kowalski",
                        1992));

        given(repository.findAll()).willReturn(books);

        //when
        List<Book> expected = underTest.getAllBooks();

        //then
        assertThat(books).isEqualTo(expected);
    }

    @Test
    void shouldGetBooksByAuthor() {
        //given
        String firstName = "John";
        String lastName = "Doe";
        List<Book> books = List.of(
                new Book("Test title",
                        firstName,
                        lastName,
                        1999),
                new Book("Test title 2",
                        firstName,
                        lastName,
                        1992));

        given(repository.findByAuthorFirstNameAndAuthorLastName(firstName, lastName))
                .willReturn(books);

        //when
        List<Book> expected = underTest.getBooksByAuthor(firstName, lastName);

        //then
        assertThat(books).isEqualTo(expected);
    }

    @Test
    void shouldNotGetBooksByAuthor() {
        //given
        String firstName = "John";
        String lastName = "Doe";
        given(repository.findByAuthorFirstNameAndAuthorLastName(firstName, lastName))
                .willReturn(new ArrayList<>());

        //when
        //then
        assertThatThrownBy(() -> underTest.getBooksByAuthor(firstName, lastName))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format("Books written by: %s %s not found", firstName, lastName));
    }

    @Test
    void shouldGetBookById() {
        //given
        Book book = new Book("Test title",
                "John",
                "Doe",
                1999);
        given(repository.findById(any())).willReturn(Optional.of(book));

        //when
        Book expected = underTest.getBookById(any());
        //then
        assertThat(book).isEqualTo(expected);
    }

    @Test
    void shouldNotGetBookById() {
        //given
        Long id = 2L;
        given(repository.findById(any())).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.getBookById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format(String.format("Book with id: %d not found", id)));
    }


    @Test
    void shouldUpdateBook() {
        //given
        Book bookOriginal = new Book("Test title",
                "John",
                "Doe",
                1999);
        Book bookUpdated = new Book("This is title after update",
                "Jan",
                "Kowalski",
                2009);
        given(repository.findById(any())).willReturn(Optional.of(bookOriginal));

        //when
        underTest.updateBook(any(), bookUpdated);

        //then
        assertThat(bookOriginal.getTitle())
                .isEqualTo(bookUpdated.getTitle());
        assertThat(bookOriginal.getAuthorFirstName())
                .isEqualTo(bookUpdated.getAuthorFirstName());
        assertThat(bookOriginal.getAuthorLastName())
                .isEqualTo(bookUpdated.getAuthorLastName());
        assertThat(bookOriginal.getPublicationYear())
                .isEqualTo(bookUpdated.getPublicationYear());

    }

    @Test
    void shouldNotUpdateBook() {
        //given
        Long id = 2L;
        given(repository.findById(id)).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.updateBook(id, any()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format("Book with id: %d not found", id));
    }

    @Test
    void shouldDeleteBookById() {
        //given
        Long id = 2L;
        given(repository.existsById(id)).willReturn(true);

        //when
        underTest.deleteBookById(id);

        //then
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(repository).deleteById(captor.capture());
        Long captured = captor.getValue();

        assertThat(captured).isEqualTo(id);
    }

    @Test
    void shouldNotDeleteBookById() {
        //given
        Long id = 2L;
        given(repository.existsById(id)).willReturn(false);

        //when
        //then
        assertThatThrownBy(() -> underTest.deleteBookById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format("Book with id: %d not found", id));
    }
}