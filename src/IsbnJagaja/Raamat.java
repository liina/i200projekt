package IsbnJagaja;

/**
 * Created by liina on 29.12.15.
 */
public class Raamat {
    String pealkiri;
    String autor;
    int ilmub;
    Isbn isbn;
    String isbnstring;
    public Raamat(String pealkiri, String autor, int ilmub) {
        this.pealkiri = pealkiri; //this.pealkiri tähendab klassi muutujat  - lihtsalt pealkiri on meetodi argumendist tulev lokaalne muutuja
        this.autor = autor;
        this.ilmub = ilmub;
    }

    public void setIsbn(Isbn isbn) {
       this.isbn = isbn;
    }
    public Isbn getIsbn() {
        return isbn;
    }
    public String getPealkiri() {
        return pealkiri;
    }
    public String getAutor() {
        return autor;
    }
    public void setIsbnstring(String isbnstring) {
        this.isbnstring = isbnstring;
    }
    public String getIsbnstring() {
        return isbnstring;
    }

    public int getIlmub() {
        return ilmub;
    }
}
