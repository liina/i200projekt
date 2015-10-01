import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Math;

/**
 * Created by Liina Abner on 30.09.2015.
 Programm palub sisestada kirjastustunnuse �igest vahemikust.
 Genereerib vastuseks k�ik sinna vahemiku kuuluvad ISBN numbrid.
 ISBN number on 13-kohaline: prefix-riigitunnus-r�hmatunnus-raamatutunnus-kontrollnumber.
 Programm peab kontrollima, kas kasutaja sisestatud number kuulub Eestile eraldatud r�hmatunnuste hulka.
 Programm peab genereerima k�ik isbn numbrid, mis antud r�hmatunnuse sees v�imalikud on st. leidma raamatutunnused ja kontrollnumbri.
 Teeks GUI. Osa siseandmeid loeks xml failist. V�ljundi kirjutaks faili.
 Kui mingi r�hmatunnus on juba v�etud, siis selle paneks lukku. P�riselus ab, aga praegu lihtsalt csv-s.
 Lisaks suvalise isbn numbri valiidsuse kontroll.
 Triipkoodi genereerimine?
 Lk. 27 Kontrollnumbri arvutamise algoritm https://www.isbn-international.org/sites/default/files/Manual_Estonian_2013.pdf
 ISBN vahemikud saab siit https://www.isbn-international.org/export_rangemessage.xml
 978-9985 - prefix ja Eesti riigitunnus
 V�imalikud kirjastustunnused:
 0-4
 50-79
 800-899
 9000-9999
 */
public class IsbnGen {
    public static void main(String[] args) throws IOException {
        String sisend;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Sisesta kirjastustunnus vahemikest:"); // siia vahemike spikker
        sisend = reader.readLine();
        int ktunnus = Integer.parseInt(sisend); //siia kontroll, kas sisend mahub etteantud vahemikesse
        System.out.println("Sain k�tte" + ktunnus);
        String[] rtunnus = genRmt(ktunnus);
        for (int i=0;i<rtunnus.length;i++) {
            System.out.println(rtunnus[i]);
        }
    }
    public static String[] genRmt (int ktunnus) throws IOException {
        //genereeri numbrid
        String[] rtunnus;
        //tuleb leida mitmekohaline number ktunnus on, sellest s�ltub mitmekohaline saab olla raamatutunnus
        //variant 1 - parsida ktunnus stringiks ja leida stringi pikkus
        int kpikkus = (Integer.toString(ktunnus)).length();
        //isbn kokku 13 numbrit, prefix+riigitunnus v�tab 7 kohta, kontrollnumber v�tab 1 koha, kirjastustunnuse ja raamatutunnse peale kokku j��b 5 kohta
        int rpikkus = 5 - kpikkus; //raamatutunnuse pikkuseks j��b 5-kirjastustnnus
        System.out.println("rpikkus " + rpikkus);
        int count; //mitu raamatutunnust on st. kui raamatutunnuse pikkus on x, siis saab �ldse olla 10 astmel x numbrit. N�iteks kui raamatutunnuse pikkus on 3, siis on v�imalikke tunnuseid 1000
        count = (int)Math.pow((double) 10, (double) rpikkus);
        rtunnus = new String[count];
        for (int i=0; i<count; i++) {
            int lisanull; //rtunnuse ette on vaja lisada nulle, et raamatutunnuses oleks �ige arv m�rke
            lisanull = rpikkus-Integer.toString(i).length();
            String rmt = Integer.toString(i);
            if (lisanull > 0) { //kui i-s on v�hem kohti kui rmttunnuse jaoks etten�htud, siis
                String nll = "0";
                lisanull--;
                while (lisanull > 0){
                    nll = nll + "0";
                    lisanull--;
                }
                rmt = nll+ rmt; //lisab ette niimitu 0, kuipalju on vaja, et raamatutunnuses oleks �ige arv m�rke
            }

            rtunnus[i] = rmt;
        }
        return rtunnus;
    }
   /*
    public static int genKtrl (int number) throws IOException {
          int ktrlnr;
        return ktrlnr;
    }
    */

}
