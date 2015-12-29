package IsbnJagaja;

/**
 * Created by liina on 29.12.15.
 */
public class Raamat {
    String Pealkiri;
    String Autor;
    int Ilmub;
    String Isbn;
    public Raamat(String Pealkiri, String Autor, int Ilmub) {
        this.Pealkiri = Pealkiri;
        this.Autor = Autor;
        this.Ilmub = Ilmub;
    }

    public void setIsbn(int plokk) {
        Plokk jooksevPlokk = new Plokk(plokk);
    }
}
