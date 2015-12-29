package IsbnJagaja;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;


import java.util.ArrayList;
import java.util.Iterator;



public class Main extends Application {
    Stage aken;
    Scene skeene;
    FlowPane paan;
    Andmebaas ab = new Andmebaas();
    @Override
    public void start(Stage primaryStage) {
        aken = primaryStage;
        paan = new FlowPane();
        skeene = new Scene(paan,500,500);
        aken.setTitle("ISBN Register");
        //ab.sisestaNaidisandmed(); //vajalik andmebaasi näidisandmetega populeerimiseks
        ArrayList kirjastajalist = ab.getAllKirjastajad();
        ComboBox<Kirjastaja> kirjastajabox = new ComboBox<Kirjastaja>();
        ObservableList<Kirjastaja> list = FXCollections.observableArrayList();
        Iterator kirjastajaIterator = kirjastajalist.iterator();
        while (kirjastajaIterator.hasNext()) {
            Kirjastaja kirjastaja = (Kirjastaja) kirjastajaIterator.next();
            list.add(kirjastaja);
        }
        kirjastajabox.setItems(list);
        kirjastajabox.setPromptText("Vali Kirjastaja");
        kirjastajabox.setOnAction((event) -> {
            Kirjastaja selectedKirjastaja = kirjastajabox.getSelectionModel().getSelectedItem();
            laadikirjastajapaan(selectedKirjastaja);
        });
        Button lisakirjastajanupp = new Button ("Uus kirjastaja");
        lisakirjastajanupp.setOnAction(event -> {
            HBox hbox = new HBox();
            //Pane uusk = new Pane();
            TextField uusnimi = new TextField();
            uusnimi.setPromptText("Sisesta nimi");
            TextField kontakt = new TextField();
            kontakt.setPromptText("Sisesta e-post");
            Button uuskirjastaja = new Button("OK");
            uuskirjastaja.setOnAction(e-> {
                int id = ab.lisaKirjastaja(uusnimi.getText(), kontakt.getText());
                Kirjastaja kirjastaja = new Kirjastaja();
                kirjastaja.setId(id);
                kirjastaja.setNimi(uusnimi.getText());
                kirjastaja.setKontakt(kontakt.getText());
                laadikirjastajapaan(kirjastaja);
                System.out.println(id);
            });
            hbox.getChildren().addAll(uusnimi, kontakt, uuskirjastaja);
            //uusk.getChildren().add(hbox);
            //Scene uuskirjastajascene = new Scene(uusk,500,500);
            //aken.setScene(uuskirjastajascene);
        });
        HBox hbox = new HBox();
        hbox.getChildren().addAll(kirjastajabox, lisakirjastajanupp);
        paan.getChildren().add(hbox);
        aken.setScene(skeene);
        aken.show();
    }

    private void laadikirjastajapaan(Kirjastaja selectedKirjastaja) {
        System.out.println("laadikirjastajapaan");
        ab.getJooksevPlokk(selectedKirjastaja);
        Label kontakt = new Label(selectedKirjastaja.getKontakt());
        paan.getChildren().add(kontakt);
        if (selectedKirjastaja.getPlokk() == 0) {
            Button seoPlokk = new Button("Vali kirjastajatunnus");
            seoPlokk.setOnAction(event -> {
                valiplokk(selectedKirjastaja);
            });
            paan.getChildren().add(seoPlokk);
        } else {
            Label jooksevplokk = new Label(Integer.toString(selectedKirjastaja.getPlokk()));
            paan.getChildren().add(jooksevplokk);
            laadiraamatuvorm(selectedKirjastaja);
        }
    }

    private void laadiraamatuvorm(Kirjastaja kirjastaja) {
        Label pklabel = new Label("Pealkiri");
        TextField pealkiri = new TextField();
        Label alabel = new Label("Autor");
        TextField autor = new TextField();
        Label ilmlabel = new Label("Ilmub aastal");
        TextField ilmub = new TextField();
        Button taotlenupp = new Button("Taotle ISBN");
        taotlenupp.setOnAction(e -> {
             Raamat raamat = new Raamat(pealkiri.getText(), autor.getText(), Integer.parseInt(ilmub.getText()));
             Plokk plokk = new Plokk(kirjastaja.getPlokk());
             ab.getPlokkdata(plokk);
             if (plokk.getVabu() > 0) {
                 int raamatuTunnus = plokk.getLast() + 1;
                    Isbn isbn = new Isbn(plokk.getTunnus(), raamatuTunnus);
                    String isbnstring = isbn.getIsbn();
                    Label isbnlabel = new Label("Raamat sai isbni " + isbnstring);
                    paan.getChildren().add(isbnlabel);
                }
            });
        paan.getChildren().addAll(pklabel,pealkiri,alabel,autor,ilmlabel,ilmub,taotlenupp);
    }

    private void valiplokk(Kirjastaja kirjastaja) {
        ArrayList vahemikud = ab.getVahemikud();
        ToggleGroup numbrid = new ToggleGroup();
        Iterator vahemikudIterator = vahemikud.iterator();
        //System.out.println(kirjastajalist.size());
        while (vahemikudIterator.hasNext()) {
            int[] vahemik = (int[]) vahemikudIterator.next();
            RadioButton rb = new RadioButton(vahemik[2] + " " + vahemik[1] +" "+vahemik[0]);
            rb.setUserData(vahemik[2]);
            rb.setToggleGroup(numbrid);
            paan.getChildren().add(rb);
        }
        //oracle.com
        numbrid.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                System.out.println(numbrid.getSelectedToggle());
                System.out.println(numbrid.getSelectedToggle().getUserData());
                int plokisuurus = (Integer) numbrid.getSelectedToggle().getUserData();
                int nextplokk = ab.getNextPlokk(plokisuurus);
                System.out.println("toggle sees nextplokk " + nextplokk);
                ab.seoplokk(kirjastaja,nextplokk);
                kirjastaja.setPlokk(nextplokk);
                laadikirjastajapaan(kirjastaja);
            }
        });
    }
}

