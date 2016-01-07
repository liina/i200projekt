package IsbnJagaja;

/**
 * Created by liina on 29.12.15.
 */
public class Isbn {
    String isbn;
    int prefix;
    int riik;
    int kirjastajaplokk;
    int rmttunnus;
    int ktrl;
    public Isbn(int kirjastajaplokk, int rmttunnus) {
        //prefix ja riik võiks ka olla ab-s, aga
        //prefix ja riik on põhimõtteliselt peaaegu konstandid - muutuvad aastate pärast võib-olla
        prefix = 978;
        riik = 9985;
        this.kirjastajaplokk = kirjastajaplokk;
        this.rmttunnus = rmttunnus;
        //raamatutunnusele tuleb vajalik arv nulle lisada
        String rmt = lisaNullid(rmttunnus,kirjastajaplokk);
        //paneb kõik tunnused kokku üheks stringiks - kui ei pane "" ette, siis teeb mingi huvitava liitmise
        String number = "" + prefix + riik + kirjastajaplokk + rmt;
        //arvutab kontrollnumbri
        ktrl = arvutaktrlnr(number);
        isbn = prefix+"-"+riik+"-"+kirjastajaplokk+"-"+rmt+"-"+ktrl;
    }

    private String lisaNullid(int rmttunnus, int kirjastajaplokk) {
        //tuleb leida mitmekohaline number ktunnus on, sellest sõltub mitmekohaline saab olla raamatutunnus
        //variant 1 - parsida ktunnus stringiks ja leida stringi pikkus
        int kpikkus = (Integer.toString(kirjastajaplokk)).length();
        //isbn kokku 13 numbrit, prefix+riigitunnus võtab 7 kohta, kontrollnumber võtab 1 koha,
        // kirjastustunnuse ja raamatutunnse peale kokku jääb 5 kohta
        int rpikkus = 5 - kpikkus; //raamatutunnuse pikkuseks jääb 5-kirjastustunnus
        int lisanull; //rtunnuse ette on vaja lisada nulle, et raamatutunnuses oleks õige arv märke
        int varunr = rmttunnus;
        lisanull = rpikkus-Integer.toString(rmttunnus).length();
        int l = lisanull+1;
        String s = String.format("%0"+l+"d",rmttunnus);
        System.out.println("s "+s+" lisanull "+lisanull);
        String rmt = Integer.toString(rmttunnus);
        if (lisanull > 0) { //kui on vähem kohti kui rmttunnuse jaoks ettenähtud, siis
            String nll = "0";
            lisanull--;
            while (lisanull > 0){
                nll = nll + "0";
                lisanull--;
            }
            rmt = nll+ rmt; //lisab ette niimitu 0, kuipalju on vaja, et raamatutunnuses oleks õige arv märke
        }
        //ilmselt saaks eelneva tsükli asemel kasutada mingit stringiformattimis funktsiooni

        return rmt;
    }

    private int arvutaktrlnr(String number) {
        int ktrlnr;
        int summa = 0;
        //ktrl numbri algoritm
        //korrutab iga paarispositsioonil oleva numbri 1-ga
        //ja paaritul positsioonil 3-ga. Liidab kokku,
        //leiab jäägi summa jagamisel 10-ga,
        //lahutab saadud jäägi 10-st,
        //saadud arvu 10-ga jagamisel saadud jääk ongi kontrollnumber
        for (int i = 0;i < number.length(); i++){
            int t = Character.getNumericValue(number.charAt(i));
            if (i%2 == 0) {
                t *= 1;
            } else {
                t *= 3;
            }
            summa += t;
        }
        ktrlnr = (10-(summa % 10)) % 10;
        return ktrlnr;
    }

    public String getIsbn() {
        return isbn;
    }
    public int getKirjastajaplokk() { return kirjastajaplokk;}
    public int getRmttunnus() {
        return rmttunnus;
    }
}
