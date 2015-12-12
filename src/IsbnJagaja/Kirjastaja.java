package IsbnJagaja;

/**
 * Created by labner on 12.12.15.
 */
public class Kirjastaja {
    int id;
    String nimi;
    String kontakt;
    int jooksevplokk;
    public void setId (int uusid) {
        id=uusid;
    }
    public int getId () {
        return id;
    }
    public void setNimi (String uusnimi) {
        nimi=uusnimi;
    }
    public void setKontakt(String uuskontakt) {
        kontakt=uuskontakt;
    }
    public String getNimi () {
        return nimi;
    }
    public String getKontakt() {
        return kontakt;
    }
    public void setPlokk(int uusplokk) {
        jooksevplokk = uusplokk;
    }
    public int getPlokk () {
        return jooksevplokk;
    }
}
