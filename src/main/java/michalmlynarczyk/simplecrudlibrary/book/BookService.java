package michalmlynarczyk.simplecrudlibrary.book;

import java.util.List;

public interface BookService {
    Book saveBook(Book book);
    List<Book> getAllBooks();
    List<Book> getBooksByAuthor(String firstName, String lastName);
    Book getBookById(Long id);
    void updateBook(Long id, Book book);
    void deleteBookById(Long id);
}
