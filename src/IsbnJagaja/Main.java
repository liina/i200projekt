package IsbnJagaja;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;


import java.util.ArrayList;
import java.util.Iterator;



public class Main extends Application {
    Stage aken;
    Scene skeene;
    BorderPane paan;
    Andmebaas ab = new Andmebaas();
    @Override
    public void start(Stage primaryStage) {
        aken = primaryStage;
        paan = new BorderPane();
        skeene = new Scene(paan,500,500);
        aken.setTitle("ISBN Register");


        //ab.sisestaNaidisandmed(); //vajalik andmebaasi näidisandmetega populeerimiseks

        ArrayList kirjastajalist = ab.getAllKirjastajad();
        //ComboBox<KeyValuePair> kirjastajabox = new ComboBox<KeyValuePair>();
        ComboBox<Kirjastaja> kirjastajabox = new ComboBox<Kirjastaja>();
        ObservableList<Kirjastaja> list = FXCollections.observableArrayList();
        Iterator kirjastajaIterator = kirjastajalist.iterator();
        //System.out.println(kirjastajalist.size());
        while (kirjastajaIterator.hasNext()) {
            Kirjastaja kirjastaja = (Kirjastaja) kirjastajaIterator.next();
            //kirjastajabox.getItems().add(new KeyValuePair(kirjastaja.getId(),kirjastaja.getNimi()));
            list.add(kirjastaja);
        }
        kirjastajabox.setItems(list);
        kirjastajabox.setPromptText("Vali Kirjastaja");
        /*cellFactory
        kirjastajabox.setCellFactory(new Callback<ListView<Kirjastaja>, ListCell<Kirjastaja>>() {
            @Override
            public ListCell<Kirjastaja> call(ListView<Kirjastaja> param) {

                return new ListCell<Kirjastaja>(){
                    @Override
                    public void updateItem(Kirjastaja item, boolean empty){
                        super.updateItem(item, empty);
                        if(!empty) {
                            setText(item.getNimi());
                            setGraphic(null);
                        }
                    }
                };
            }
        });
        cellFactory*/
        kirjastajabox.setOnAction((event) -> {
            Kirjastaja selectedKirjastaja = kirjastajabox.getSelectionModel().getSelectedItem();
            System.out.println("kirjastajaboxvalitud + selectedKirjastaja.toString()");
            laadikirjastajapaan(selectedKirjastaja);
        });
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

    private void laadikirjastajapaan(Kirjastaja selectedKirjastaja) {
        ab.getJooksevPlokk(selectedKirjastaja);
        HBox hbox = new HBox();
        Label kontakt = new Label(selectedKirjastaja.getKontakt());
        hbox.getChildren().add(kontakt);
        if (selectedKirjastaja.getPlokk() == 0) {
            Button seoPlokk = new Button("Vali kirjastajatunnus");
            hbox.getChildren().add(seoPlokk);
        } else {
            Label jooksevplokk = new Label(Integer.toString(selectedKirjastaja.getPlokk()));
            hbox.getChildren().add(jooksevplokk);
        }
        paan.setCenter(hbox);
    }

}

