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
    BorderPane paan;
    Andmebaas ab = new Andmebaas();

    @Override
    public void start(Stage primaryStage) {
        aken = primaryStage;
        paan = new BorderPane();
        skeene = new Scene(paan,500,500);
        aken.setTitle("ISBN Register");
        //ab.sisestaNaidisandmed(); //vajalik andmebaasi näidisandmetega populeerimiseks
        //vbox, milles on kirjastajate valiku ComboBox ja uue kirjastaja lisamis box
        VBox paremvbox = makeParemvbox();
        paan.setLeft(paremvbox);
        aken.setScene(skeene);
        aken.show();
    }

    private VBox makeParemvbox() {
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
        Button lisakirjastajanupp = makeLisaKirjastajaNupp();
        VBox vbox = new VBox();
        vbox.getChildren().addAll(lisakirjastajanupp,kirjastajabox);
        return vbox;
    }

    private Button makeLisaKirjastajaNupp () {
        Button lisakirjastajanupp = new Button ("Uus kirjastaja");
        lisakirjastajanupp.setOnAction(event -> {
            paan.setTop(new Label("Lisa uus kirjastaja"));
            lisakirjastajavorm();
        });
        return lisakirjastajanupp;
    }

    private void lisakirjastajavorm() {
        System.out.println("jõuab lisakirjastajavormi meetodisse");
        VBox vbox = new VBox();
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
            VBox parembox = makeParemvbox();
            paan.setLeft(parembox);
            System.out.println("uue kirjastaja id " + id);
        });
        vbox.getChildren().addAll(uusnimi,kontakt,uuskirjastaja);
        paan.setCenter(vbox);
    }

    private VBox valiplokkvorm() {
        ArrayList vahemikud = ab.getVahemikud();
        VBox togglebox = new VBox();
        ToggleGroup numbrid = new ToggleGroup();
        Iterator vahemikudIterator = vahemikud.iterator();
        while (vahemikudIterator.hasNext()) {
            int[] vahemik = (int[]) vahemikudIterator.next();
            RadioButton rb = new RadioButton(vahemik[2] + " " + vahemik[1] +" "+vahemik[0]);
            togglebox.getChildren().add(rb);
            rb.setUserData(vahemik[2]);
            rb.setToggleGroup(numbrid);
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
            }
        });
        //oracle.com
        return togglebox;
    }

    private void laadikirjastajapaan(Kirjastaja kirjastaja) {
        System.out.println("laadikirjastajapaan");
        ab.getJooksevPlokk(kirjastaja);
        HBox hbox = new HBox();
        Label kontakt = new Label(kirjastaja.getNimi() + " " + kirjastaja.getKontakt());
        hbox.getChildren().add(kontakt);
        if (kirjastaja.getPlokk() == 0) {
            valiplokk(kirjastaja);
        } else {
            System.out.println("laadikirjastajapaan kutsub välja Plokk instantsi");
            Plokk plokk = new Plokk(kirjastaja.getPlokk());
            System.out.println("plokk last " + plokk.getLast());
            ab.getPlokkdata(plokk);
            System.out.println("laadikirjastajapaan kontroll" + plokk.getVabu());
            System.out.println("get plokkdata plokk last " + plokk.getLast());
            Label jooksevplokk = new Label("plokk - kirjastajatunnus: " + plokk.getTunnus() + " Vabu numbreid: " + plokk.getVabu());
            hbox.getChildren().add(jooksevplokk);
            if (plokk.getVabu() < 1) {
                System.out.println("kas on siin?");
                valiplokk(kirjastaja);
            } else {
                System.out.println("on hoopis siin");
                laadiraamatuvorm(kirjastaja);
            }
        }
        paan.setTop(hbox);
    }

    private void laadiTopbox(Kirjastaja kirjastaja){
        ab.getJooksevPlokk(kirjastaja);
        HBox hbox = new HBox();
        Label kontakt = new Label(kirjastaja.getNimi() + " " + kirjastaja.getKontakt());
        hbox.getChildren().add(kontakt);

            Plokk plokk = new Plokk(kirjastaja.getPlokk());
            System.out.println("plokk last "+plokk.getLast());
            ab.getPlokkdata(plokk);
            System.out.println("get plokkdata plokk last "+plokk.getLast());
            Label jooksevplokk = new Label("plokk - kirjastajatunnus: " + plokk.getTunnus()+ " Vabu numbreid: "+plokk.getVabu());
            hbox.getChildren().add(jooksevplokk);

        paan.setTop(hbox);
    }

    private void laadiraamatuvorm(Kirjastaja kirjastaja) {
        Plokk plokk = new Plokk(kirjastaja.getPlokk());
        ab.getPlokkdata(plokk);
        System.out.println("laadiraamatuvorm esimene kontroll"+plokk.getVabu());
        if (plokk.getVabu() < 1) {

            laadikirjastajapaan(kirjastaja);
        } else {
            VBox raamatubox = new VBox();
            Label pklabel = new Label("Pealkiri");
            TextField pealkiri = new TextField();
            Label alabel = new Label("Autor");
            TextField autor = new TextField();
            Label ilmlabel = new Label("Ilmub aastal");
            ChoiceBox ilmub = new ChoiceBox(FXCollections.observableArrayList(
                    "2016", "2017", "2018"
            )
            );
            ilmub.getSelectionModel().selectFirst();
            //TextField ilmub = new TextField();
            Button taotlenupp = new Button("Anna ISBN");
            taotlenupp.setOnAction(e -> {
                Raamat raamat = new Raamat(pealkiri.getText(), autor.getText(), Integer.parseInt(ilmub.getValue().toString()));
                //Plokk plokk = new Plokk(kirjastaja.getPlokk());
                //ab.getPlokkdata(plokk);
                System.out.println("Plokk.getLast" + plokk.getLast());
                if (plokk.getVabu() > 0) {
                    int raamatuTunnus = plokk.getLast() + 1;
                    Isbn isbn = new Isbn(plokk.getTunnus(), raamatuTunnus);
                    String isbnstring = isbn.getIsbn();
                    raamat.setIsbn(isbn);
                    raamat.setIsbnstring(isbnstring);
                    ab.insertRaamat(raamat);
                    VBox vbox = new VBox();
                    Button uusraamat = new Button("Uus raamat");
                    uusraamat.setOnAction(event -> {
                        laadiraamatuvorm(kirjastaja);
                    });
                    Button statistika = new Button("Statistika");
                    statistika.setOnAction(event -> {
                        System.out.println("statistikat vajutati");
                        VBox rmtbox = new VBox();
                        ArrayList rmtud = ab.getRmtud(kirjastaja.getId());
                        Iterator rmtudIterator = rmtud.iterator();
                        while (rmtudIterator.hasNext()) {
                            Raamat rmt = (Raamat) rmtudIterator.next();
                            System.out.println(rmt.getIsbnstring()+" "+rmt.getPealkiri()+" / "+rmt.getAutor() + " ("+rmt.getIlmub()+")");
                            rmtbox.getChildren().add(new Label(rmt.getIsbnstring()+" "+rmt.getPealkiri()+" / "+rmt.getAutor() + " ("+rmt.getIlmub()+")"));
                            paan.setCenter(rmtbox);
                        }
                    });
                        Label isbnlabel = new Label("Raamat sai isbni " + isbnstring);
                        vbox.getChildren().addAll(isbnlabel, uusraamat, statistika);
                        paan.setCenter(vbox);

                        laadiTopbox(kirjastaja);
                }else{
                        Label viga = new Label("viga");
                        paan.setCenter(viga);
                    }
                });
                raamatubox.getChildren().addAll(pklabel, pealkiri, alabel, autor, ilmlabel, ilmub, taotlenupp);
                paan.setCenter(raamatubox);
            }
        }

    private void valiplokk(Kirjastaja kirjastaja) {
        System.out.println("jõuab valiplokki");
        VBox togglebox = new VBox();
        ArrayList vahemikud = ab.getVahemikud();
        ToggleGroup numbrid = new ToggleGroup();
        Iterator vahemikudIterator = vahemikud.iterator();
        //System.out.println(kirjastajalist.size());
        while (vahemikudIterator.hasNext()) {
            int[] vahemik = (int[]) vahemikudIterator.next();
            RadioButton rb = new RadioButton(vahemik[2] + " " + vahemik[1] +" "+vahemik[0]);
            rb.setUserData(vahemik[2]);
            rb.setToggleGroup(numbrid);
            togglebox.getChildren().add(rb);
        }
        paan.setCenter(togglebox);
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
                laadiraamatuvorm(kirjastaja);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}


