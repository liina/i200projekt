package IsbnJagaja;

/**
 * Created by liina on 29.12.15.
 */
public class Plokk {
    int kirjastajaplokk;
    private int last = -1;
    int vabu;
    public Plokk(int kirjastajaplokk) {
        this.kirjastajaplokk = kirjastajaplokk;
        vabu = arvutaVabu();
    }

    public int getKirjastajaplokk() {
        return kirjastajaplokk;
    }

    public void setLast(int last) {
        this.last = last;
        vabu = arvutaVabu(); //iga kord kui viimane number muutub, tuleb vabade arvümber arvutada
    }

    private int arvutaVabu() {
        int kpikkus = (Integer.toString(kirjastajaplokk)).length();
        //see info on ka ab-s, aga teeme siin niimoodi
        //isbn kokku 13 numbrit, prefix+riigitunnus võtab 7 kohta, kontrollnumber võtab 1 koha,
        // kirjastustunnuse ja raamatutunnse peale kokku jääb 5 kohta
        int rpikkus = 5 - kpikkus; //raamatutunnuse pikkuseks jääb 5-kirjastustnnus
        //mitu raamatutunnust on st. kui raamatutunnuse pikkus on x,
        // siis saab üldse olla 10 astmel x numbrit.
        // Näiteks kui raamatutunnuse pikkus on 3, siis on võimalikke tunnuseid 1000
        int count = (int)Math.pow((double) 10, (double) rpikkus);
        int vabu = count;
        if (last != -1) {
            vabu = count - last - 1;
        }
        return vabu;
    }

    public int getVabu() {
        return vabu;
    }

    public int getLast() {
        return last;
    }
}
