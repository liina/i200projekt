package IsbnJagaja;

import javafx.scene.control.TextField;
import org.apache.derby.iapi.sql.*;

import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by liina on 2.12.15.
 */
public class Andmebaas {
    static Connection conn = null;
    //konstruktor
    public Andmebaas() {
        looYhendus();

    }

    private void looYhendus() { // loob Connectioni ja teeb ab, kui seda veel ei ole
        try {
            //territory  ja collation on vajalikud, et sorteerimine käiks õigesti
            conn = DriverManager.getConnection("jdbc:derby:isbnbaas;create=true;territory=et_EE;collation=TERRITORY_BASED");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sisestaNaidisandmed() {
        //teeb ab ja sisestab näidisandmed
        int[] kirjastustunnused = {50, 79, 800, 899, 9000, 9999};
        int prefix = 978;
        int maatunnus = 9985;
        try {
            Statement s = conn.createStatement();
            s.execute("CREATE TABLE plokivahemikud " +
                    "(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)" +
                    ", prefix integer, maatunnus integer, esimene integer, viimane integer, plokimaht integer, " +
                    "PRIMARY KEY (id))");
            PreparedStatement psInsert = conn.prepareStatement("INSERT INTO plokivahemikud (prefix,maatunnus,esimene,viimane,plokimaht) VALUES (?,?,?,?,?)");
            for (int i = 0; i < (kirjastustunnused.length); i++) {
                if (i % 2 == 0) {
                    int esimene = kirjastustunnused[i];
                    int viimane = kirjastustunnused[i + 1];
                    int plokipikkus = (Integer.toString(esimene)).length(); //mitmekohaline number on
                    int rmtpikkus = 5 - plokipikkus; //raamatutunnuse pikkuseks jääb 5-kirjastustnnus
                    int plokimaht = (int) Math.pow(10, rmtpikkus); //st mitu raamatutunnust ühe kirjastustunnuse kohta tuleb, 10 [0-9] astmel kohtade arv
                    psInsert.setInt(1, prefix);
                    psInsert.setInt(2, maatunnus);
                    psInsert.setInt(3, esimene);
                    psInsert.setInt(4, viimane);
                    psInsert.setInt(5, plokimaht);
                    psInsert.executeUpdate();
                } else {
                    continue;
                }
            }
            s.execute("CREATE TABLE kirjastaja " +
                    "(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)" +
                    ", nimi varchar(40), kontakt varchar(40), " +
                    " PRIMARY KEY (id))");
            s.execute("INSERT INTO kirjastaja(nimi,kontakt) VALUES('Kuu ja Päike','kuu@paike.com')");

            s.execute("CREATE TABLE plokk " +
                    "(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)" +
                    ", kirjastaja_id int, plokk int, status boolean, " +
                    " PRIMARY KEY (id))");
            s.execute("INSERT INTO plokk(kirjastaja_id,plokk,status) VALUES(1,800,TRUE)");

            s.execute("CREATE TABLE raamat " +
                    "(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)" +
                    ", pealkiri varchar(100), autor varchar(100), ilmub int," +
                    " PRIMARY KEY (id))");
            //s.execute("INSERT INTO raamat (pealkiri,autor) VALUES ('Taevatähed','Saar, Ants')");

            s.execute("CREATE TABLE isbn " +
                    "(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)" +
                    ", raamat_id int, plokk int, raamat int, isbn bigint, isbnstr varchar(20)," +
                    " PRIMARY KEY (id))");
            //s.execute("INSERT INTO isbn(raamat_id,plokk,raamat,isbn,isbnstr) VALUES (1,800,'00',9789985800004,'978-9985-800-00-4')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList getAllKirjastajad() {
        //tagastab listi kõikide ab-s olevate kirjastajatega
        ArrayList<Kirjastaja> kirjastajalist = new ArrayList<Kirjastaja>();
        try {
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM kirjastaja ORDER by nimi");
            while(rs.next()) {
                Kirjastaja kirjastaja = new Kirjastaja();
                kirjastaja.setId(rs.getInt("id"));
                kirjastaja.setNimi(rs.getString("nimi"));
                kirjastaja.setKontakt(rs.getString("kontakt"));
                kirjastajalist.add(kirjastaja);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kirjastajalist;
    }

    public int lisaKirjastaja(String nimi, String kontakt) {
        //siia peaks tulema kontroll, et nimi ja kontakt ei ületaks ab-s lubatud pikkust
        nimi = lyhendaKuiVaja(40,nimi);
        kontakt = lyhendaKuiVaja(40,kontakt);
        try {
            //Statement.RETURN_GENERATED_KEYS - tahame tagasi saada sisestatud kirjastaja id (autoincrement)
            PreparedStatement ps = conn.prepareStatement("INSERT INTO kirjastaja(nimi,kontakt) VALUES(?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,nimi);
            ps.setString(2,kontakt);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt(1);
            }
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getJooksevPlokk(int kirjastaja_id) {
        int jooksevplokk = 0;
        try {
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT plokk FROM plokk WHERE kirjastaja_id=" + kirjastaja_id +" AND status=TRUE");
            while(rs.next()) {
                jooksevplokk = (rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jooksevplokk;
    }

    public ArrayList getVahemikud() {
        //tagastab kirjastajaplokkide vahemikud ja vabade plokkide arvu igas vahemikus
        ArrayList<int[]> vahemikud = new ArrayList<int[]>();
        try {
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT viimane,esimene,plokimaht FROM plokivahemikud ORDER BY plokimaht DESC");
            while(rs.next()) {
                int kokku = rs.getInt(1) - rs.getInt(2) + 1;
                int plokimaht = rs.getInt(3);
                int vabu = kokku;
                //pärib plokk tabelist antud vahemiku plokkide arvu(need on juba mõne kirjastajaga seotud plokid
                // ja arvestab saadud arvu vabade plokkide hulgast maha
                Statement ss = conn.createStatement();
                ResultSet rrs = ss.executeQuery("SELECT count(*) FROM plokk WHERE plokk >= "+
                        rs.getInt(2)+" AND plokk <= "+rs.getInt(1));
                while(rrs.next()) {
                    vabu -= rrs.getInt(1);
                }
                rrs.close();
                int[] vahemik = {kokku,vabu,plokimaht};
                vahemikud.add(vahemik);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vahemikud;
    }

    public int getNextPlokk(int plokisuurus) {
        //tagastab järgmise kirjastajaploki küsitud suurusega vahemikust või feili korral tagastab 0-i,
        // nt. kui kõik selle vahemiku plokid on juba võetud
        try {
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT esimene,viimane FROM plokivahemikud WHERE plokimaht=" + plokisuurus);
            int esimene = 0;
            int viimane= 0;
            int nextplokk = 0;
            while (rs.next()) {
                esimene = rs.getInt(1);
                viimane = rs.getInt(2);
                nextplokk = esimene;
            }
            s.setMaxRows(1);
            rs = s.executeQuery("SELECT plokk FROM plokk WHERE plokk >="+esimene+" AND plokk <="+viimane+" ORDER by plokk DESC");
            while (rs.next()) {
                nextplokk = rs.getInt(1)+1;
            }
            return nextplokk;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public void vanaplokk(int plokk) {
        //muudab ploki staatuse
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE plokk SET status=false WHERE plokk=" + plokk);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void seoplokk(Kirjastaja kirjastaja, int nextplokk) {
        //sisestab tabelisse plokk uue kirjastaja-plokk seose
        try {
            PreparedStatement psInsert = conn.prepareStatement("INSERT INTO plokk(kirjastaja_id,plokk,status) VALUES(?,?,?)");
            psInsert.setInt(1,kirjastaja.getId());
            psInsert.setInt(2,nextplokk);
            psInsert.setBoolean(3,true);
            psInsert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getLastinPlokk(int plokk) {
        //küsib viimasena välja antud raamatutunnuse antud plokist
        int last = -1;
        try {
            Statement s = conn.createStatement();
            s.setMaxRows(1);
            ResultSet rs = s.executeQuery("SELECT raamat FROM isbn WHERE plokk=" + plokk + " ORDER BY raamat DESC");
            while (rs.next()) {
                last = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return last;
    }

    public void insertRaamat(Raamat raamat) {
        //sisestab raamatuandmed sh isbn-i tabelitesse raamat ja isbn
        Isbn isbn = raamat.getIsbn();
        try {
            PreparedStatement psInsert = conn.prepareStatement("INSERT INTO raamat (pealkiri,autor,ilmub) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            String pealkiri = lyhendaKuiVaja(100,raamat.getPealkiri());
            String autor = lyhendaKuiVaja(100,raamat.getAutor());
            psInsert.setString(1,pealkiri);
            psInsert.setString(2,autor);
            psInsert.setInt(3,raamat.getIlmub());
            psInsert.executeUpdate();
            ResultSet rs = psInsert.getGeneratedKeys();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt(1);
                psInsert = conn.prepareStatement("INSERT INTO isbn (plokk,raamat,isbnstr,raamat_id) VALUES(?,?,?,?)");
                psInsert.setInt(1,isbn.getKirjastajaplokk());
                psInsert.setInt(2,isbn.getRmttunnus());
                psInsert.setString(3,isbn.getIsbn());
                psInsert.setInt(4,id);
                psInsert.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList getRmtud(int id) {
        //tagastab kõik raamatud antud kirjastajaga seotud plokkidest
        ArrayList<Raamat> rmtlist = new ArrayList<Raamat>();
        try {
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM plokk LEFT JOIN isbn ON isbn.plokk=plokk.plokk " +
                    "INNER JOIN raamat ON raamat.id=isbn.raamat_id WHERE kirjastaja_id="+id);
            while(rs.next()) {
                /*
                for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {
                    System.out.println(" " + rs.getMetaData().getColumnName(i) + "=" + rs.getObject(i));
                }
                */
                Raamat rmt = new Raamat(rs.getString(12),rs.getString(13),rs.getInt(14));
                rmt.setIsbnstring(rs.getString(10));
                rmtlist.add(rmt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rmtlist;
    }

    public String lyhendaKuiVaja(int i, String str) {
        String parasstring = str;
        if (str.length() > i) {
            parasstring = str.substring(0,i);
        }
        return parasstring;
    }
}
/*DriverManager.getConnection("jdbc:derby:"+"db"+";shutdown=true");*/