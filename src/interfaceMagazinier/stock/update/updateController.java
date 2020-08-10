package interfaceMagazinier.stock.update;

import Connector.ConnectionClass;
import basicClasses.product;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class updateController implements Initializable {

    @FXML private  JFXTextField productname;
    @FXML private  JFXTextField barcode;
    @FXML private  JFXTextField sellprice;
    @FXML private  JFXTextField buyprice;
    @FXML private  JFXTextField quantity;
    @FXML private  JFXDatePicker expirationdate;
    @FXML private  Label errorLabel;
    public static product productSelected;


    public static product getProductSelected() {
        return productSelected;
    }

    public static void setProductSelected(product productSelected) {
        updateController.productSelected = productSelected;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productname.setText(productSelected.getProductName());
        barcode.setText((String.valueOf(productSelected.getBarcode())));
        sellprice.setText(String.valueOf(productSelected.getSellPrice()));
        buyprice.setText(String.valueOf(productSelected.getBuyPrice()));
        quantity.setText(String.valueOf(productSelected.getQuantity()));
        if (productSelected.getExpirationDate() == "") expirationdate.setValue(null); else expirationdate.setValue(LocalDate.parse(productSelected.getExpirationDate()));
    }

    @FXML private void updateProduct(ActionEvent event) throws SQLException {
        if (errorCheck()) return;

        //update product
        Connection connection = ConnectionClass.getConnection();
        String query = "Update stock set name=?, barcode=? , buyprice=? , sellprice=? , quantity=? ,expirationdate=? where barcode='"+productSelected.getBarcode()+"'" ;
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1,productname.getText());
        System.out.println(barcode.getText());
        pst.setString(2,barcode.getText());
        pst.setString(3,buyprice.getText());
        pst.setString(4,String.valueOf(sellprice.getText()));
        pst.setString(5,String.valueOf(quantity.getText()));
        if (expirationdate.getValue() == null) {pst.setNull(6, Types.DATE);}
        else { pst.setString(6,expirationdate.getValue().toString());}
        pst.execute() ;

        productSelected.setBarcode(Integer.parseInt(barcode.getText()));
        productSelected.setProductName(productname.getText());
        productSelected.setBuyPrice(Float.parseFloat(buyprice.getText()));
        productSelected.setSellPrice(Float.parseFloat(sellprice.getText()));
        productSelected.setQuantity(Integer.parseInt(quantity.getText()));
        if (expirationdate.getValue() == null) productSelected.setExpirationDate(""); else productSelected.setExpirationDate(expirationdate.getValue().toString());
    }


    private boolean errorCheck() throws SQLException {


        if (productname.getText().isEmpty() || barcode.getText().isEmpty() || sellprice.getText().isEmpty() || buyprice.getText().isEmpty() || quantity.getText().isEmpty()){
            errorLabel.setTextFill(Paint.valueOf("red"));
            errorLabel.setText("Fill all the text fields");
            return true;
        }
        if (!barcode.getText().matches("[0-9]*") || !quantity.getText().matches("[0-9]*") || !sellprice.getText().matches("[0-9]*\\.?[0-9]+") || !buyprice.getText().matches("[0-9]*\\.?[0-9]+")){
            errorLabel.setTextFill(Paint.valueOf("red"));
            errorLabel.setText("Some text fields must be numbers only");
            return true;
        }
        Connection connection = ConnectionClass.getConnection();
        String query = "SELECT * FROM stock where barcode= ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, barcode.getText());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            errorLabel.setText("This barcode "+ barcode.getText() + " already exists");
            return true;
        }
        errorLabel.setTextFill(Paint.valueOf("green"));
        errorLabel.setText("product updated succefully");
        return false;
    }
}