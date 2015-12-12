package IsbnJagaja;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.Iterator;



public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root,500,500);
        stage.setTitle("ISBN Register");

        Andmebaas ab = new Andmebaas();
        //ab.sisestaNaidisandmed(); //vajalik andmebaasi näidisandmetega populeerimiseks
        ArrayList kirjastajalist = ab.getAllKirjastajad();
        ComboBox<String> kirjastajabox = new ComboBox<String>();
        Iterator kirjastajaIterator = kirjastajalist.iterator();
        System.out.println(kirjastajalist.size());
        while (kirjastajaIterator.hasNext()) {
            Kirjastaja kirjastaja = (Kirjastaja) kirjastajaIterator.next();
            kirjastajabox.getItems().add(kirjastaja.getNimi());
        }
        kirjastajabox.setPromptText("Vali Kirjastaja");

        Button taotlenupp = new Button ("Taotle ISBN");
        /*taotlenupp.setOnAction(event -> {
            int kirjastajaid = Integer.parseInt(kirjastajabox.getId());
            System.out.println("kirjastajaid" + kirjastajaid);

        });*/
        GridPane grid = new GridPane();
        grid.add(kirjastajabox, 0, 2, 4, 1);
        grid.add(taotlenupp, 0, 3);
        root.getChildren().add(grid);

        stage.setScene(scene);
        stage.show();
        //kirjastaja
        //setCurrentBlock
        //getCurrentBlock
        //ilmutabRaamatu
        //tyhistabRaamatu
        //getIsbnforRaamat
        //
        //Raamat
        //pealkiri
        //autor
        //ilmub
        //setIsbn

        //ISBN
        //prefix
        //maatunnus
        //kirjastustunnus
        //raamatutunnus
        //kontrollnumber
        //arvutaKontrollnumber

        //Plokk
        //vabad
        //broneeritud
        //ilmunud
        //maht
        //getNextISBN
        stage.show();
        //ab.naitaAndmeid();
    }

}

