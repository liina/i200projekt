package IsbnJagaja;

/**
 * Created by liina on 29.12.15.
 */
public class Plokk {
    int kirjastajaTunnus;
    private int last = -1;
    int vabu;
    public Plokk(int plokk) {
        kirjastajaTunnus = plokk;
        vabu = arvutaVabu();
    }

    public int getTunnus() {
        return kirjastajaTunnus;
    }

    public void setLast(int last) {
        this.last = last;
        vabu = arvutaVabu();
    }

    private int arvutaVabu() {
        System.out.println("Plokk.arvutaVabu"+last);
        int kpikkus = (Integer.toString(kirjastajaTunnus)).length();
        //see info on ka ab-s, aga teeme siin niimoodi
        //isbn kokku 13 numbrit, prefix+riigitunnus v�tab 7 kohta, kontrollnumber v�tab 1 koha, kirjastustunnuse ja raamatutunnse peale kokku j��b 5 kohta
        int rpikkus = 5 - kpikkus; //raamatutunnuse pikkuseks j��b 5-kirjastustnnus
        //mitu raamatutunnust on st. kui raamatutunnuse pikkus on x, siis saab �ldse olla 10 astmel x numbrit. N�iteks kui raamatutunnuse pikkus on 3, siis on v�imalikke tunnuseid 1000
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
