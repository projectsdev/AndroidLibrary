package domain.project1;

public class HistoryDetails {
    String Transaction_id,Subject,author,issue_date,last_date,Book_name,serial_No,book_id,dept,course,semester;
    int published_year,booked_no;
    boolean renewable,return_status;

    public HistoryDetails(String transaction_id,String serial_No,String book_id,String book_name, String subject, String author, String issue_date,
                          String last_date, int published_year, int booked_no, boolean renewable,boolean return_status,String dept,String course,String semester) {
        Transaction_id = transaction_id;
        Subject = subject;
        this.Book_name = book_name;
        this.author = author;
        this.issue_date = issue_date;
        this.last_date = last_date;
        this.published_year = published_year;
        this.booked_no = booked_no;
        this.renewable = renewable;
        this.return_status = return_status;
        this.serial_No = serial_No;
        this.book_id = book_id;
        this.dept = dept;
        this.course = course;
        this.semester = semester;
    }
    public boolean getReturnStatus(){
        return return_status;
    }

    public String getDept() {
        return dept;
    }

    public String getCourse() {
        return course;
    }

    public String getSemester() {
        return semester;
    }

    public String getSerial_No(){
        return serial_No;
    }
    public String getBook_id(){
        return book_id;
    }
    public String getTransaction_id() {
        return Transaction_id;
    }
    public String getBook_name(){
        return Book_name;
    }
    public String getSubject() {
        return Subject;
    }

    public String getAuthor() {
        return author;
    }

    public String getIssue_date() {
        return issue_date;
    }

    public String getLast_date() {
        return last_date;
    }

    public int getPublished_year() {
        return published_year;
    }

    public int getBooked_no() {
        return booked_no;
    }

    public boolean isRenewable() {
        return renewable;
    }
}
