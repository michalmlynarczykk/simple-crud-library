package michalmlynarczyk.simplecrudlibrary.book;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Slf4j
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book saveBook(Book book) {
        log.info("Saving book {} to database", book.getTitle());
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAllBooks() {
        log.info("Getting all books");
        return bookRepository.findAll();
    }

    @Override
    public List<Book> getBooksByAuthor(String firstName, String lastName) {
        log.info("Searching for books written by author: {} {}", firstName, lastName);
        List<Book> books = bookRepository.findByAuthorFirstNameAndAuthorLastName(firstName, lastName);
        if (books.isEmpty()) {
            log.error("Books searched by author not found");
            throw new EntityNotFoundException(String.format("Books written by: %s %s not found", firstName, lastName));
        }
        return books;
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> {
            log.error("Book searched by Id not found");
            return new EntityNotFoundException(String.format("Book with id: %d not found", id));
        });
    }

    @Transactional
    @Override
    public void updateBook(Long id, Book book) {
        Book bookToUpdate = bookRepository.findById(id).orElseThrow(() -> {
            log.error("Book {} not found", id);
            return new EntityNotFoundException(String.format("Book with id: %d not found", id));
        });
        if (book.getTitle() != null &&
                !book.getTitle().isEmpty() &&
                !book.getTitle().equals(bookToUpdate.getTitle())) {
            log.info("Title changed from {} to {}",
                    bookToUpdate.getTitle(),
                    book.getTitle());
            bookToUpdate.setTitle(book.getTitle());
        }
        if (book.getAuthorFirstName() != null &&
                !book.getAuthorFirstName().isEmpty() &&
                !book.getAuthorFirstName().equals(bookToUpdate.getAuthorFirstName())) {
            log.info("Author first name changed from {} to {}",
                    bookToUpdate.getAuthorFirstName(),
                    book.getAuthorFirstName());
            bookToUpdate.setAuthorFirstName(book.getAuthorFirstName());
        }
        if (book.getAuthorLastName() != null &&
                !book.getAuthorLastName().isEmpty() &&
                !book.getAuthorLastName().equals(bookToUpdate.getAuthorLastName())) {
            log.info("Author last name changed from {} to {}",
                    bookToUpdate.getAuthorLastName(),
                    book.getAuthorLastName());
            bookToUpdate.setAuthorLastName(book.getAuthorLastName());
        }
        if (!(book.getPublicationYear() == 0) &&
                !(book.getPublicationYear() == bookToUpdate.getPublicationYear())) {
            log.info("Publication year changed from {} to {}",
                    bookToUpdate.getPublicationYear(),
                    book.getPublicationYear());
            bookToUpdate.setPublicationYear(book.getPublicationYear());
        }
    }

    @Override
    public void deleteBookById(Long id) {
        if (bookRepository.existsById(id)) {
            log.info("Deleting book with id: {}", id);
            bookRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(String.format("Book with id: %d not found", id));
        }
    }
}
