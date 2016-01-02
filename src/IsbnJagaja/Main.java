package IsbnJagaja;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
        //ab.sisestaNaidisandmed(); //vajalik andmebaasi näidisandmetega populeerimiseks
        aken = primaryStage;
        paan = new BorderPane();
        //paan.setPadding(new Insets(20,20, 20, 20));
        skeene = new Scene(paan,600,500);
        aken.setTitle("ISBN Register");
        VBox leftvbox = makeLeftvbox();
        paan.setLeft(leftvbox);
        HBox topbox = new HBox();
        topbox.setPrefHeight(60);
        paan.setTop(topbox);
        aken.setScene(skeene);
        aken.show();
    }

    //teeb vboxi, milles on kirjastajate valiku ComboBox ja uue kirjastaja lisamise box
    private VBox makeLeftvbox() {
        //küsib ab-st kõik kirjastajad, vastuseks Kirjastaja objektide arraylist (alfabeetiliselt sorteeritud)
        ArrayList kirjastajalist = ab.getAllKirjastajad();
        //teeb saadud listi alusel comboboxi
        ComboBox<Kirjastaja> kirjastajabox = new ComboBox<Kirjastaja>();
        kirjastajabox.setPrefWidth(150);
        ObservableList<Kirjastaja> list = FXCollections.observableArrayList();
        Iterator kirjastajaIterator = kirjastajalist.iterator(); //Iterator on vajalik Arraylistist liikmete kättesaamiseks
        while (kirjastajaIterator.hasNext()) {
            Kirjastaja kirjastaja = (Kirjastaja) kirjastajaIterator.next();
            list.add(kirjastaja);
        }
        kirjastajabox.setItems(list);
        kirjastajabox.setPromptText("Vali Kirjastaja");
        //Comboboxis uue väärtuse valimisel laaditakse põhitegevuste vorm
        kirjastajabox.setOnAction((event) -> {
            Kirjastaja kirjastaja = kirjastajabox.getSelectionModel().getSelectedItem();
            //laadikirjastajapaan(kirjastaja);
            laaditegevused(kirjastaja);
        });
        //nupp uue kirjastaja lisamiseks
        Button lisakirjastajanupp = makeLisaKirjastajaNupp();
        lisakirjastajanupp.setMaxWidth(Double.MAX_VALUE);
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(50,20,10,20));
        //vbox.setStyle("-fx-background-color: #336699;");
        vbox.getChildren().addAll(lisakirjastajanupp,kirjastajabox);
        return vbox;
    }

    private void laaditegevused(Kirjastaja kirjastaja) {
        //küsime topboxi
        HBox topbox = makeTopbox(kirjastaja);
        paan.setTop(topbox);
        //kui kirjastajaga pole seotud ühtegi plokki, näita plokivalimisvormi ja seo uus plokk
        if (kirjastaja.getPlokk() == 0) {
            valiplokk(kirjastaja);
        } else {
           //kas plokis on vabu numbreid
            Plokk kirjastajaplokk = new Plokk(kirjastaja.getPlokk());
            int last = ab.getLastinPlokk(kirjastaja.getPlokk());
            kirjastajaplokk.setLast(last);
            if (kirjastajaplokk.getVabu() < 1) {
                ab.vanaplokk(kirjastaja.getPlokk());
                valiplokk(kirjastaja);
            } else {
                //kui plokis on vabu numbreid, siis laadi raamatuvorm
                System.out.println("on hoopis siin");
                laadiraamat(kirjastaja);
            }
        }
    }

    private void laadiraamat(Kirjastaja kirjastaja) {
        Plokk kirjastajaplokk = new Plokk(kirjastaja.getPlokk());
        int last = ab.getLastinPlokk(kirjastaja.getPlokk());
        kirjastajaplokk.setLast(last);
        int vabu = kirjastajaplokk.getVabu();
        VBox raamatubox = new VBox();
        TextField pealkiri = new TextField();
        pealkiri.setPromptText("Pealkiri");
        TextField autor = new TextField();
        autor.setPromptText("Autor");
        Label ilmlabel = new Label("Ilmub aastal");
        ChoiceBox ilmub = new ChoiceBox(FXCollections.observableArrayList(
                "2016", "2017", "2018"
        ));
        ilmub.getSelectionModel().selectFirst();
        HBox ilmubbox = new HBox(ilmlabel,ilmub);
        ilmubbox.setSpacing(10);
        Button annanupp = new Button("Anna ISBN");
        annanupp.setStyle("-fx-background-color:pink");
        annanupp.setOnAction(e -> {
            Raamat raamat = new Raamat(pealkiri.getText(), autor.getText(), Integer.parseInt(ilmub.getValue().toString()));
            if (vabu > 0) {
                int raamatuTunnus = kirjastajaplokk.getLast() + 1;
                Isbn isbn = new Isbn(kirjastajaplokk.getKirjastajaplokk(), raamatuTunnus);
                String isbnstring = isbn.getIsbn();
                raamat.setIsbn(isbn);
                raamat.setIsbnstring(isbnstring);
                ab.insertRaamat(raamat);
                VBox vbox = new VBox();
                Label isbnlabel = new Label("Raamat sai isbni " + isbnstring);
                vbox.getChildren().addAll(isbnlabel);
                vbox.setPadding(new Insets(50,0,0,20));
                paan.setCenter(vbox);
                HBox topbox = makeTopbox(kirjastaja);
                paan.setTop(topbox);
            } else {
                Label viga = new Label("viga");
                paan.setCenter(viga);
            }
        });
        raamatubox.getChildren().addAll(pealkiri,autor,ilmubbox, annanupp);
        raamatubox.setSpacing(10);
        raamatubox.setPadding(new Insets(50,20,20,20));
        paan.setCenter(raamatubox);
    }

    private Button makeUusraamatnupp(Kirjastaja kirjastaja) {
        Button uusraamat = new Button("Uus raamat");
        uusraamat.setOnAction(event -> {
            laaditegevused(kirjastaja);
        });
        return uusraamat;
    }

    private HBox makeTopbox (Kirjastaja kirjastaja){
        HBox topbox = new HBox();
        VBox kirjastajaandmed = new VBox(new Label("Kirjastaja ID:    " + kirjastaja.getId()),
                new Label("Kirjastaja nimi:  " + kirjastaja.getNimi()),
                new Label("Kirjastaja e-post: " + kirjastaja.getKontakt()));
        topbox.getChildren().add(kirjastajaandmed);
        VBox plokiandmed = new VBox(new Label("Kirjastajaplokk: -"),
                new Label("Vabu numbreid: -"));
        int jooksevplokk = ab.getJooksevPlokk(kirjastaja.getId());
        if (jooksevplokk != 0) {
            kirjastaja.setPlokk(jooksevplokk);
            Plokk plokk = new Plokk(kirjastaja.getPlokk());
            System.out.println("plokk last " + plokk.getLast());
            //küsib ab-st viimase raamatutunnuse, mis selles plokis hõivatud on ja annab väärtuse Ploki muutujale last
            int last = ab.getLastinPlokk(plokk.getKirjastajaplokk());
            plokk.setLast(last);
            plokiandmed = new VBox(new Label("Kirjastajaplokk: " + plokk.getKirjastajaplokk()),
                    new Label("Vabu numbreid: " + plokk.getVabu()));
        }
        //topbox.getChildren().add(plokiandmed);
        kirjastajaandmed.getChildren().add(plokiandmed);
        Button uusraamat = makeUusraamatnupp(kirjastaja);
        topbox.getChildren().add(uusraamat);
        Button statistikanupp = makeStatistikaNupp(kirjastaja);
        topbox.getChildren().add(statistikanupp);
        topbox.setSpacing(30);
        topbox.setPadding(new Insets(5,0,0,20));
        return topbox;
    }

    private Button makeStatistikaNupp(Kirjastaja kirjastaja) {
        Button statistikanupp = new Button("Ajalugu");
        statistikanupp.setOnAction(event -> {
            showstatistika(kirjastaja);
        });
        return statistikanupp;
    }

    private void showstatistika(Kirjastaja kirjastaja) {
        VBox rmtbox = new VBox();
        //küsib ab-st selle kirjastaja raamatud alfabeetiliselt sorteeritud
        ArrayList rmtud = ab.getRmtud(kirjastaja.getId());
        ObservableList<String> list = FXCollections.observableArrayList();
        if (rmtud.isEmpty()) {
            rmtbox.getChildren().add(new Label("Pole isbn-e määratud"));
        } else {
            Iterator rmtudIterator = rmtud.iterator();
            while (rmtudIterator.hasNext()) {
                Raamat rmt = (Raamat) rmtudIterator.next();
                list.add(rmt.getIsbnstring() + " " + rmt.getPealkiri() + " / "
                        + rmt.getAutor() + " (" + rmt.getIlmub() + ")");
            }
            ListView<String> listView = new ListView<String>(list);
            rmtbox.getChildren().add(listView);
        }
        rmtbox.setPadding(new Insets(20,20,20,20));
        paan.setCenter(rmtbox);
    }

    private Button makeLisaKirjastajaNupp () {
        Button lisakirjastajanupp = new Button ("Uus kirjastaja");
        lisakirjastajanupp.setOnAction(event -> {
            //nupu vajutamisel laaditakse kirjastaja lisamise vorm
            lisakirjastajavorm();
        });
        return lisakirjastajanupp;
    }

    private void lisakirjastajavorm() {
        //setTop - paneb ülemisse ossa ühe sildi
        HBox topbox = new HBox();
        Label lisalabel = new Label("Lisa uus kirjastaja");
        lisalabel.setPadding(new Insets(20,0,0,225));
        topbox.setPrefHeight(50);
        topbox.getChildren().add(lisalabel);
        paan.setTop(topbox);
        //setTop
        //setCenter - paneb keskele kirjastaja lisamise vormi
        VBox vbox = new VBox();
        TextField nimi = new TextField();
        nimi.setPromptText("Kirjastuse nimi");
        nimi.setPrefWidth(50);
        TextField kontakt = new TextField();
        kontakt.setPromptText("Kirjastaja e-post");
        kontakt.setPrefWidth(100);
        Button uuskrstjanupp = new Button("OK");
        uuskrstjanupp.setOnAction(e -> {
            //sisestab ab-sse uue kirjastaja nime ja kontakti, vastu saab kirjastaja id
            int id = ab.lisaKirjastaja(nimi.getText(), kontakt.getText());
            System.out.println("uus kirjastaja id on "+id);
            //loob kirjastaja objekti
            Kirjastaja kirjastaja = new Kirjastaja();
            kirjastaja.setId(id);
            kirjastaja.setNimi(nimi.getText());
            kirjastaja.setKontakt(kontakt.getText());
            //laadib kirjastaja
            laaditegevused(kirjastaja);
            //laadib uuesti vasaku vboxi, et seal kajastuks ka uus kirjastaja
            VBox leftvbox = makeLeftvbox();
            paan.setLeft(leftvbox);
            System.out.println("tegi uue kirjastaja id " + id);
        });
        vbox.getChildren().addAll(nimi,kontakt,uuskrstjanupp);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(50,50,0,20));
        vbox.setPrefWidth(100);
        paan.setCenter(vbox);
        //setCenter
    }

    private void valiplokk(Kirjastaja kirjastaja) {
        VBox togglebox = new VBox();
        togglebox.setSpacing(10);
        togglebox.setPadding(new Insets(50,0,0,20));
        //küsib ab-st plokkide vahemikud
        ArrayList vahemikud = ab.getVahemikud();
        ToggleGroup numbrid = new ToggleGroup();
        Iterator vahemikudIterator = vahemikud.iterator();
        //System.out.println(kirjastajalist.size());
        while (vahemikudIterator.hasNext()) {
            int[] vahemik = (int[]) vahemikudIterator.next();
            RadioButton rb = new RadioButton("Plokimaht: " + vahemik[2] + " Vabu plokke: " + vahemik[1] +" ("+vahemik[0]+"-st)");
            rb.setUserData(vahemik[2]);
            rb.setToggleGroup(numbrid);
            togglebox.getChildren().add(rb);
        }
        paan.setCenter(togglebox);
        //oracle.com - addListener
        numbrid.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                int plokimaht = (Integer) numbrid.getSelectedToggle().getUserData();
                int nextplokk = ab.getNextPlokk(plokimaht);
                System.out.println("toggle sees nextplokk " + nextplokk);
                //seob kirjastajaga uue ploki
                ab.seoplokk(kirjastaja,nextplokk);
                kirjastaja.setPlokk(nextplokk);
                //laadikirjastajapaan(kirjastaja);
                laaditegevused(kirjastaja);
                //laadiraamatuvorm(kirjastaja);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}


