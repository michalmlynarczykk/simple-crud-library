package michalmlynarczyk.simplecrudlibrary.book;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="book")
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String authorFirstName;
    private String authorLastName;
    private int publicationYear;

    public Book() {}

    public Book(String title, String authorFirstName, String authorLastName, int publicationYear) {
        this.title = title;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.publicationYear = publicationYear;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int dateOfRelease) {
        this.publicationYear = dateOfRelease;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }
}
