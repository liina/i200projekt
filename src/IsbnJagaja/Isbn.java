package IsbnJagaja;

/**
 * Created by liina on 29.12.15.
 */
public class Isbn {
    String isbn;
    public Isbn(int tunnus, int raamatuTunnus) {
        int prefix = 978;
        int riik = 9985;
        String rmt = lisaNullid(raamatuTunnus,tunnus);
        String number = "" + prefix + riik + tunnus + rmt; //kui ei pane "" ette, siis teeb mingi huvitava liitmise
        int kontrollnumber = arvutaktrlnr(number);
        isbn = prefix+"-"+riik+"-"+tunnus+"-"+rmt+"-"+kontrollnumber;
    }

    private String lisaNullid(int raamatuTunnus, int tunnus) {
        //tuleb leida mitmekohaline number ktunnus on, sellest s�ltub mitmekohaline saab olla raamatutunnus
        //variant 1 - parsida ktunnus stringiks ja leida stringi pikkus
        int kpikkus = (Integer.toString(tunnus)).length();
        //isbn kokku 13 numbrit, prefix+riigitunnus v�tab 7 kohta, kontrollnumber v�tab 1 koha, kirjastustunnuse ja raamatutunnse peale kokku j��b 5 kohta
        int rpikkus = 5 - kpikkus; //raamatutunnuse pikkuseks j��b 5-kirjastustnnus
        System.out.println("rpikkus " + rpikkus);
        int lisanull; //rtunnuse ette on vaja lisada nulle, et raamatutunnuses oleks �ige arv m�rke
        lisanull = rpikkus-Integer.toString(raamatuTunnus).length();
        String rmt = Integer.toString(raamatuTunnus);
        if (lisanull > 0) { //kui on v�hem kohti kui rmttunnuse jaoks etten�htud, siis
            String nll = "0";
            lisanull--;
            while (lisanull > 0){
                nll = nll + "0";
                lisanull--;
            }
            rmt = nll+ rmt; //lisab ette niimitu 0, kuipalju on vaja, et raamatutunnuses oleks �ige arv m�rke
        }
        return rmt;
    }

    private int arvutaktrlnr(String number) {
        int ktrlnr;
        int summa = 0;
        //ktrl numbri algoritm
        //korrutab iga paarispositsioonil oleva numbri 1-ga
        //ja paaritul positsioonil 3-ga. Liidab kokku,
        //leiab j��gi summa jagamisel 10-ga,
        //lahutab saadud j��gi 10-st,
        //saadud arvu 10-ga jagamisel saadud j��k ongi kontrollnumber
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
}
