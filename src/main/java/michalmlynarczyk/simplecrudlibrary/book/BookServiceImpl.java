package michalmlynarczyk.simplecrudlibrary.book;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void saveBook(Book book) {
        log.info("Saving book {} to database", book.getTitle());
        bookRepository.save(book);
    }

    @Override
    public List<Book> getAllBooks() {
        log.info("Getting all books");
        return bookRepository.findAll();
    }

    @Override
    public List<Book> getBooksByAuthor(String firstName, String lastName) {
        log.info("Searching for books written by author: {} {}", firstName, lastName);
        return bookRepository.findByAuthorFirstNameAndAuthorLastName(firstName, lastName)
                .orElseThrow(() -> {
                    log.error("Books searched by author not found");
                    return new IllegalStateException(String.format("Books written by: %s %s not found", firstName, lastName));
                });
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> {
            log.error("Book searched by Id not found");
            return new IllegalStateException(String.format("Book with id: %d not found", id));
        });
    }

    @Transactional
    @Override
    public void updateBook(Long id, String title) {
        Book bookToUpdate = bookRepository.findById(id).orElseThrow(() -> {
            log.error("Book to update not found");
            return new IllegalStateException(String.format("Book with id: %d not found", id));
        });
        if (title != null &&
                title.length() > 0
                && !title.equals(bookToUpdate.getTitle())) {
            bookToUpdate.setTitle(title);
            log.info("Book with id: {} updated successfully",id);
        }
    }

    @Override
    public void deleteBookById(Long id) {
        if (bookRepository.existsById(id)) {
            log.info("Deleting book with id: {}",id);
            bookRepository.deleteById(id);
        } else {
            throw new IllegalStateException(String.format("Book with id: %d not found", id));
        }
    }
}
