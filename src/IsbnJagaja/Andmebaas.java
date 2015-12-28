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
            conn = DriverManager.getConnection("jdbc:derby:isbnbaas;create=true;territory=et_EE;collation=TERRITORY_BASED");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void sisestaNaidisandmed() {
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
                    int plokimaht = (int) Math.pow(10, rmtpikkus); //st mitu raamatutunnust ühe kirjastustunnuse kohta tuleb
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
                    ",plokk_id int, pealkiri varchar(100), autor varchar(100), " +
                    " PRIMARY KEY (id))");
            s.execute("INSERT INTO raamat (pealkiri,autor) VALUES ('Taevatähed','Saar, Ants')");

            s.execute("CREATE TABLE isbn " +
                    "(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)" +
                    ", raamat_id int, plokk int, raamat varchar(5), isbn bigint, isbnstr varchar(20)," +
                    " PRIMARY KEY (id))");
            s.execute("INSERT INTO isbn(raamat_id,plokk,raamat,isbn,isbnstr) VALUES (1,800,'00',9789985800004,'978-9985-800-00-4')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void naitaAndmeid() {
        Statement s = null;
        try {
            s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM plokivahemikud");
            while(rs.next()) {
                for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {
                    System.out.print(" " + rs.getMetaData().getColumnName(i) + "=" + rs.getObject(i));
                }
                System.out.println("");
            }
            rs = s.executeQuery("SELECT * FROM kirjastaja");
            while(rs.next()) {
                for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {

                    System.out.print(" " + rs.getMetaData().getColumnName(i) + "=" + rs.getObject(i));
                }
                System.out.println("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public ArrayList getAllKirjastajad() {
        Statement s = null;
        ArrayList<Kirjastaja> kirjastajalist = new ArrayList<Kirjastaja>();
        try {
            s = conn.createStatement();
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

    public int lisaKirjastaja(String uusnimi, String kontakt) {
        try {
            PreparedStatement psInsert = conn.prepareStatement("INSERT INTO kirjastaja(nimi,kontakt) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
            psInsert.setString(1,uusnimi);
            psInsert.setString(2,kontakt);
            int rows = psInsert.executeUpdate();

            ResultSet rs = psInsert.getGeneratedKeys();
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


    public void getJooksevPlokk(Kirjastaja kirjastaja) {
        Statement s = null;
        try {
            s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT plokk FROM plokk WHERE kirjastaja_id="+kirjastaja.getId()+" AND status=TRUE");
            while(rs.next()) {
                kirjastaja.setPlokk(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
/*DriverManager.getConnection("jdbc:derby:"+"db"+";shutdown=true");*/