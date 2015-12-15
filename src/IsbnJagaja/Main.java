package IsbnJagaja;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.Iterator;



public class Main extends Application {
    Stage aken;
    Scene skeene;
    BorderPane paan;

    @Override
    public void start(Stage primaryStage) {
        aken = primaryStage;
        paan = new BorderPane();
        skeene = new Scene(paan,500,500);
        aken.setTitle("ISBN Register");

        Andmebaas ab = new Andmebaas();
        //ab.sisestaNaidisandmed(); //vajalik andmebaasi näidisandmetega populeerimiseks

        ArrayList kirjastajalist = ab.getAllKirjastajad();
        ComboBox<KeyValuePair> kirjastajabox = new ComboBox<KeyValuePair>();
        //ComboBox<Kirjastaja> kirjastajabox = new ComboBox<Kirjastaja>();
        Iterator kirjastajaIterator = kirjastajalist.iterator();
        //System.out.println(kirjastajalist.size());
        while (kirjastajaIterator.hasNext()) {
            Kirjastaja kirjastaja = (Kirjastaja) kirjastajaIterator.next();
            kirjastajabox.getItems().add(new KeyValuePair(kirjastaja.getId(),kirjastaja.getNimi()));

        }
        kirjastajabox.setPromptText("Vali Kirjastaja");

        Button lisakirjastajanupp = new Button ("Uus kirjastaja");
        lisakirjastajanupp.setOnAction(event -> {
            HBox hbox = new HBox();
            Pane uusk = new Pane();
            TextField uusnimi = new TextField();
            uusnimi.setPromptText("Sisesta nimi");
            TextField kontakt = new TextField();
            kontakt.setPromptText("Sisesta e-post");
            Button uuskirjastaja = new Button("OK");
            uuskirjastaja.setOnAction(e-> {
                int id = ab.lisaKirjastaja(uusnimi.getText(), kontakt.getText());
                System.out.println(id);
            });
            hbox.getChildren().addAll(uusnimi, kontakt, uuskirjastaja);
            uusk.getChildren().add(hbox);
            Scene uuskirjastajascene = new Scene(uusk,500,500);
            aken.setScene(uuskirjastajascene);
        });
        HBox hbox = new HBox();
        hbox.getChildren().addAll(kirjastajabox, lisakirjastajanupp);
        paan.setTop(hbox);

        aken.setScene(skeene);
        aken.show();
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
        aken.show();
        //ab.naitaAndmeid();
    }

}

