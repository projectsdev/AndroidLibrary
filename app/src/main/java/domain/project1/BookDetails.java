package domain.project1;

public class BookDetails {
    String book_name,course,department,author,semester,book_id;
    boolean renewable;
    int volume,available,published_year;

    public BookDetails(String book_id,String book_name, String course, String department,
                       String author, String semester, boolean renewable, int volume, int available, int published_year) {
        this.book_id = book_id;
        this.book_name = book_name;
        this.course = course;
        this.department = department;
        this.author = author;
        this.semester = semester;
        this.renewable = renewable;
        this.volume = volume;
        this.available = available;
        this.published_year = published_year;
    }
    public String getBook_id() {
        return book_id;
    }
    public String getBook_name() {
        return book_name;
    }

    public String getCourse() {
        return course;
    }

    public String getDepartment() {
        return department;
    }

    public String getAuthor() {
        return author;
    }

    public String getSemester() {
        return semester;
    }

    public boolean isRenewable() {
        return renewable;
    }

    public int getVolume() {
        return volume;
    }

    public int getAvailable() {
        return available;
    }

    public int getPublished_year() {
        return published_year;
    }
}
