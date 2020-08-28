
/*
*
* mohim pour 3efsa ta3e red number lazem dire sql query te verifai kima li rahom hena kol makan kaen rs.next vrai kol mayzid +1
* wedire label gire segire fog icon ta3e la cloche hadik mafiha walo
*
            if (nomuber_notifi > 0) {
                notification_cont.setText("" + nomuber_notifi);
                notification_cont.setStyle("-fx-background-color: #ff5646;-fx-background-radius: 30;-fx-font-size:14px;-fx-padding: 0 5 0 5");
            }

*
* we hadi ta3e min yeclicki teroh
*
* notificationImage.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                nomuber_notifi -= nomuber_notifi;
                notification_cont.setText("");
                notification_cont.setStyle("-fx-background-radius: 30;-fx-font-size:14px;-fx-padding: 0 5 0 5");
                try {
                    Region root1=FXMLLoader.load(getClass().getResource("/notifications/notification.fxml"));
                    JFXDialog dialog =new JFXDialog(mainStackPane,root1, JFXDialog.DialogTransition.RIGHT);
                    dialog.getStylesheets().add(getClass().getResource("/notifications/notification_style.css").toString());
                    dialog.show();
                } catch (IOException e) {
                    System.err.println(e);
                }
            });
*
*
*
*
*
*
*
* */
package notification;
import Connector.ConnectionClass;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class notificaitonController implements Initializable {
   @FXML public  JFXListView<notificationItem> listview= new JFXListView<>();
   @FXML public JFXTextField reherche_text;
   @FXML public HBox hbox;
   @FXML public ImageView close_image;
   @FXML public ImageView image_rechercher;
   @FXML public StackPane pane_of_close;
         private ObservableList<notificationItem> Oblist=FXCollections.observableArrayList();

    private LinkedList<notificationItem>  linkedList=new LinkedList<>();
         private Connection connection= ConnectionClass.getConnection();
         private String SQL=null;
         private String description="";
         private ResultSet rs, rs1,rs2;
         private Boolean exist;

 //================================================ notification cell ========================================================================
 static class notification_cell extends ListCell<notificationItem>{
            private  HBox hBox=new HBox(10);
             private VBox vBox=new VBox(5);
             private Text titel=new Text();
             private HBox HBOX =new HBox();
             private Text description1=new Text();
             private Text duration=new Text();
             private ImageView image_item=new ImageView();
             private ImageView image_delte=new ImageView();
             public notification_cell() {
                 super();
                 //================ notification text style ==========================
                 vBox.setId("vBox");
                 hBox.setId("hBox");
                 HBOX.setId("HBOX");
                 titel.setId("title");
                 description1.setId("description1");
                 duration.setId("duration");
                HBOX.setAlignment(Pos.CENTER);
                 HBOX.setPrefWidth(50);
                 HBOX.setPrefHeight(50);
                 HBOX.setStyle("-fx-background-radius:15");
                 titel.setStyle("-fx-font-size: 16;" +
                         "-fx-text-fill: #163143");
                 description1.setStyle("-fx-font-size: 12;" +
                         "-fx-text-fill: #266f8c");
                 duration.setStyle("-fx-font-size:8;"+
                                 "-fx-text-fill: #858585");
                 image_delte.setFitWidth(25);
                 image_delte.setFitHeight(25);
                 image_delte.setOnMouseClicked(new EventHandler<MouseEvent>() {
                     @Override
                     public void handle(MouseEvent mouseEvent) {
                       getListView().getItems().remove(getItem());
                     }
                 });

                     hBox.setOnMouseEntered(e -> {
                     image_delte.setImage(new Image("/resource/icons/delete.png"));
                     image_delte.setFitWidth(25);
                     image_delte.setFitHeight(25);
                         });
                 hBox.setOnMouseExited(e->{
                     image_delte.setFitWidth(1);
                     image_delte.setFitHeight(1);
                 });
                 hBox.setAlignment(Pos.CENTER);
                 //====================================================================
                 HBOX.getChildren().add(image_delte);
                 hBox.getChildren().addAll(image_item, vBox, HBOX);
                 vBox.getChildren().addAll(titel, description1, duration);
             }
     @Override
     protected void updateItem(notificationItem item, boolean empty) {
         super.updateItem(item, empty);
         setText(null);  // No text in label of super class
         if (item==null || empty) {
             setText("");
             setGraphic(null);
         }else {
             titel.setText(item.getTitel_text());
             description1.setText(item.getDescription_text());
             duration.setText(item.getDuration_text());
             image_item.setImage(item.getImage_item());
             setGraphic(hBox);
         }
     }

 }

//======================================================================================================================
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        //============================================Searching bar animation============================================
        image_rechercher.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> {
            hbox.setPrefWidth(250);
            hbox.setLayoutX(184.0);
            hbox.setLayoutY(11.0);
            close_image.setFitHeight(29);
            close_image.setFitWidth(29);
        });
        pane_of_close.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> {
            hbox.setPrefWidth(48);
            hbox.setLayoutX(386.0);
            close_image.setFitHeight(1);
            close_image.setFitWidth(1);
        });
//============================================== notification controlle=================================================
       /*
       * hena create a new table name it notification bach nestoko les notification le9dam mohim
       * manich 3erf nemhom wela nekhalohm
       * derha haka bach magazine 9ader tefakrah we9tach dak produit kamal
       *
       *
       * mohim table fihe 3 column
       * notification titel we decription li hiya les produit rahom ma9sodin wela kach haja we date li jat fiha
       * kimahaka kima rahom mesatfin
       * */

        try {

            rs1=connection.createStatement().executeQuery("SELECT * FROM notification");

            while(rs1.next()){
                String URL=null;
                if (rs1.getString(1).contains("Expired Products")) URL="/resource/icons/expired_product_white.png";
                else
                    if (rs1.getString(1).contains("Expired date comming")) URL="/resource/icons/remand_product_White.png";
                linkedList.addFirst(new notificationItem(rs1.getString(1),rs1.getString(2),durationDate(rs1.getString(3)),URL));
            }
//            ======================================== test expired products =========================================================
           /*
           * hena hebibi dire nom ta3e table ta3e les produit
           * tehtaj fichier tezid les icons li yejo me3a notification
           *
           * kitsib ? ma3entha dire asem ta3e table de produit
           * we kitsib ! DIRE ASEM TA3E TABLE DE NOTIFICATION
           *
           *
           * */
           /*
           *
           * ????????????????????
           * */

            rs2= connection.createStatement().executeQuery("SELECT * FROM user.stock WHERE datediff(now(),expirationdate) <= 0");
            while (rs2.next() ) {
                description += rs2.getString(2) + " ";
            }
            if(description!="") {
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                rs1 = connection.createStatement().executeQuery("SELECT * FROM user.notification");
                exist = true;
                while (rs1.next()) {
                    if (rs1.getString(1).equals("Expired Products")&&(rs1.getString(2).equals(description))) exist = false;
                }
                if (exist) {
                    linkedList.addFirst(new notificationItem("Expired Products",description,"few seconds","/resource/icons/expired_product_white.png"));
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    SQL = "INSERT INTO user.notification VALUES ('Expired Products','"+description+"',"+"now())";
                    connection.createStatement().executeUpdate(SQL);
                }
            }
            //????????????????????????????????????????????????????????????????????????,
            SQL ="SELECT*FROM user.stock WHERE DATEDIFF(expirationdate,now()) BETWEEN 1 AND 7";
            description = "";
            rs2= connection.createStatement().executeQuery(SQL);
            while (rs2.next()) {
              description += rs2.getString(2) + " ";
            }
            if(description!="") {
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                rs1 = connection.createStatement().executeQuery("SELECT * FROM user.notification");
                exist = true;
                while (rs1.next()) {
                    if (rs1.getString(1).equals("Expired date comming")&&(rs1.getString(2).equals(description))) exist = false;
                }
                if (exist) {
                    linkedList.addFirst(new notificationItem("Expired date comming",description,"few seconds","/resource/icons/remand_product_White.png"));
                    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    SQL = "INSERT INTO user.notification VALUES ('Expired date comming','"+description+"',"+"now())";
                    connection.createStatement().executeUpdate(SQL);
                }
            }
           Oblist.addAll(linkedList);
            listview.setItems(Oblist);

            listview.setCellFactory(new Callback<ListView<notificationItem>, ListCell<notificationItem>>() {
                @Override
                public ListCell<notificationItem> call(ListView<notificationItem> item_typeListView) {
                    return new notification_cell();
                }
            });

      FilteredList<notificationItem> filteredData = new FilteredList<>(Oblist, b -> true);
            reherche_text.textProperty().addListener((observable, oldValue, newValue) -> {
                ObservableList<notificationItem> searching=FXCollections.observableArrayList();
                filteredData.setPredicate(employee -> {
                    if (newValue == null || newValue.isEmpty() || newValue =="") {
                        return true;
                    }
                    String lowercase=newValue.toLowerCase();
                    if (employee.getDescription_text().toLowerCase().contains(lowercase)) return true;
                    else if (employee.getDuration_text().toLowerCase().contains(lowercase)) return true;
                    else if (employee.getTitel_text().toLowerCase().contains(lowercase)) return true;
                    else return false;
                });
                searching.addAll(filteredData);
                listview.setItems(searching);
            });


        } catch (SQLException e) {
            System.err.println(e);
        }

    }
    public String durationDate(String time) throws SQLException {
        String duration_sql = null;
        int proide = 0;
        rs = connection.createStatement().executeQuery("SELECT DATEDIFF(NOW(),'" + time + "')");
        while (rs.next()) proide = rs.getInt(1);
        if (proide == 0) {
            rs = connection.createStatement().executeQuery("SELECT hour(TIMEDIFF(TIME(NOW()),TIME('"+time+"')))");
            while (rs.next()) proide = rs.getInt(1);
            if (proide == 0) {
                rs = connection.createStatement().executeQuery("SELECT minute((TIMEDIFF(TIME(NOW()),TIME('" + time + "'))))");
                while (rs.next()) proide = rs.getInt(1);
                if (proide==0){
                    rs = connection.createStatement().executeQuery("SELECT second((TIMEDIFF(TIME(NOW()),TIME('" + time + "'))))");
                    while (rs.next()) proide = rs.getInt(1);
                    if (proide==0) duration_sql="just now";
                    else if (proide==1) duration_sql="1 second ago ";
                          else duration_sql=proide+" seconds ago";
                }else if (proide==1) duration_sql="1 minute ago";
                 else duration_sql=proide+"minutes ago";
            } else if (proide == 1) duration_sql = "1 hour ago";
            else duration_sql = proide + " hours ago";
        }
        else if (proide < 365){
            if (proide == 1) duration_sql="1 day ago";
            else if (proide < 30) duration_sql= proide +" days ago";
                  else if (proide/30==1) duration_sql="1 month ago";
                        else duration_sql=(proide/30)+" months ago";
      }
        else{
            if ((proide/365) == 1) duration_sql="1 year ago";
            else duration_sql=(proide/365) +" years ago";}
          return duration_sql ;
        }
    }

