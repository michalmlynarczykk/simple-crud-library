package michalmlynarczyk.simplecrudlibrary.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RequestMapping("api/library")
@RestController
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_READER')")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok().body(books);
    }

    @PostMapping("/books")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Book> saveBook(
            @Valid @RequestBody Book book) {
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/books")
                .toUriString());
        return ResponseEntity.created(uri).body(bookService.saveBook(book));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_READER')")
    @GetMapping("/books/author")
    public ResponseEntity<List<Book>> getAllBooksByAuthor(
            @RequestParam("first-name") String firstName,
            @RequestParam("last-name") String lastName) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(firstName, lastName));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_READER')")
    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/books/{id}")
    public ResponseEntity<?> updateBookById(
            @PathVariable("id") Long id,
            @RequestBody Book updatedBook) {
        bookService.updateBook(id, updatedBook);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/books/{id}")
    public ResponseEntity<?> deleteBookById(
            @PathVariable("id") Long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.ok().build();
    }

}
