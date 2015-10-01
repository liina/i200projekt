import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Math;

/**
 * Created by Liina Abner on 30.09.2015.
 Programm palub sisestada kirjastustunnuse õigest vahemikust.
 Genereerib vastuseks kõik sinna vahemiku kuuluvad ISBN numbrid.
 ISBN number on 13-kohaline: prefix-riigitunnus-rühmatunnus-raamatutunnus-kontrollnumber.
 Programm peab kontrollima, kas kasutaja sisestatud number kuulub Eestile eraldatud rühmatunnuste hulka.
 Programm peab genereerima kõik isbn numbrid, mis antud rühmatunnuse sees võimalikud on st. leidma raamatutunnused ja kontrollnumbri.
 Teeks GUI. Osa siseandmeid loeks xml failist. Väljundi kirjutaks faili.
 Kui mingi rühmatunnus on juba võetud, siis selle paneks lukku. Päriselus ab, aga praegu lihtsalt csv-s.
 Lisaks suvalise isbn numbri valiidsuse kontroll.
 Triipkoodi genereerimine?
 Lk. 27 Kontrollnumbri arvutamise algoritm https://www.isbn-international.org/sites/default/files/Manual_Estonian_2013.pdf
 ISBN vahemikud saab siit https://www.isbn-international.org/export_rangemessage.xml
 978-9985 - prefix ja Eesti riigitunnus
 Võimalikud kirjastustunnused:
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
        System.out.println("Sain kätte" + ktunnus);
        String[] rtunnus = genRmt(ktunnus);
        for (int i=0;i<rtunnus.length;i++) {
            System.out.println(rtunnus[i]);
        }
    }
    public static String[] genRmt (int ktunnus) throws IOException {
        //genereeri numbrid
        String[] rtunnus;
        //tuleb leida mitmekohaline number ktunnus on, sellest sõltub mitmekohaline saab olla raamatutunnus
        //variant 1 - parsida ktunnus stringiks ja leida stringi pikkus
        int kpikkus = (Integer.toString(ktunnus)).length();
        //isbn kokku 13 numbrit, prefix+riigitunnus võtab 7 kohta, kontrollnumber võtab 1 koha, kirjastustunnuse ja raamatutunnse peale kokku jääb 5 kohta
        int rpikkus = 5 - kpikkus; //raamatutunnuse pikkuseks jääb 5-kirjastustnnus
        System.out.println("rpikkus " + rpikkus);
        int count; //mitu raamatutunnust on st. kui raamatutunnuse pikkus on x, siis saab üldse olla 10 astmel x numbrit. Näiteks kui raamatutunnuse pikkus on 3, siis on võimalikke tunnuseid 1000
        count = (int)Math.pow((double) 10, (double) rpikkus);
        rtunnus = new String[count];
        for (int i=0; i<count; i++) {
            int lisanull; //rtunnuse ette on vaja lisada nulle, et raamatutunnuses oleks õige arv märke
            lisanull = rpikkus-Integer.toString(i).length();
            String rmt = Integer.toString(i);
            if (lisanull > 0) { //kui i-s on vähem kohti kui rmttunnuse jaoks ettenähtud, siis
                String nll = "0";
                lisanull--;
                while (lisanull > 0){
                    nll = nll + "0";
                    lisanull--;
                }
                rmt = nll+ rmt; //lisab ette niimitu 0, kuipalju on vaja, et raamatutunnuses oleks õige arv märke
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
