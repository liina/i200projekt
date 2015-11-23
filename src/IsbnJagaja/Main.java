package IsbnJagaja;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Liina Abner on 23.11.2015.
 * ab-ga suhtlemise kirjutamisel oli abiks Derby demo.simple
 */
public class Main {
    Connection conn = null;
    static ArrayList<Statement> statements = new ArrayList<Statement>(); // list of Statements, PreparedStatements
    static PreparedStatement psInsert;
    static PreparedStatement psUpdate;
    static Statement s;
    static ResultSet rs = null;
    public static void main(String[] args) throws SQLException {
        try {
            Connection conn = DriverManager.getConnection("jdbc:derby:isbnbaas;create=true");
            s = conn.createStatement();
            statements.add(s);
            s.execute("create table ktunnus (tunnus integer)");
            psInsert = conn.prepareStatement(
                    "insert into ktunnus values (?)");
            statements.add(psInsert);

            psInsert.setInt(1, 1956);
            psInsert.executeUpdate();
            psUpdate = conn.prepareStatement(
                    "update ktunnus set tunnus=? where tunnus=?");
            statements.add(psUpdate);

            psUpdate.setInt(1, 1856);
            psUpdate.setInt(2, 1956);
            psUpdate.executeUpdate();
            rs = s.executeQuery(
                    "SELECT * FROM ktunnus ORDER BY tunnus");
            while(rs.next()) {
                System.out.println(rs.getString(1));
            }
            s.execute("drop table ktunnus");
            System.out.println("Dropped table location");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
