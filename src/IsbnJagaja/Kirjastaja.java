package IsbnJagaja;

/**
 * Created by labner on 12.12.15.
 */
public class Kirjastaja {
    int id;
    String nimi;
    String kontakt;
    int jooksevplokk;
    public void setId (int id) {
        this.id=id;
    }
    public int getId () {
        return id;
    }
    public void setNimi (String nimi) {
        this.nimi=nimi;
    }
    public void setKontakt(String kontakt) {
        this.kontakt=kontakt;
    }
    public String getNimi () {
        return nimi;
    }
    public String getKontakt() {
        return kontakt;
    }
    public void setPlokk(int jooksevplokk) {
        this.jooksevplokk = jooksevplokk;
    }
    public int getPlokk () {
        return jooksevplokk;
    }
    //toString meetod, et oleks selge, millist Stringi kirjastajate combobox peab näitama
    @Override
    public String toString() {
        return nimi;
    }
}
