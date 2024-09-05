package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.ProductBo;
import com.devstack.pos.bo.custom.impl.ProductBoImpl;
import com.devstack.pos.dto.ProductDto;
import com.devstack.pos.enums.BoType;
import com.devstack.pos.view.tm.ProductTm;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ProductMainFormController {
    public TextArea txtProductDescription;
    public JFXButton btnSaveUpdate;
    public TextField txtSearch;
    public TableView<ProductTm> tblProduct;
    public JFXTextField txtProductCode;
    public TableColumn colDelete;
    public TableColumn colShowMore;
    public TableColumn colDescription;
    public TableColumn colCode;
    public TextArea txtSelectedProdDescription;
    public TextField txtSelectedProdCode;
    public AnchorPane contextProduct;
    private String searchText = "";

    ProductBo bo = BoFactory.getInstance().getBo(BoType.PRODUCT);

    public void initialize() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colShowMore.setCellValueFactory(new PropertyValueFactory<>("showMore"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));
        loadProductId();
        loadAllProducts(searchText);

        tblProduct.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setData(newValue);
        });
    }

    private void setData(ProductTm newValue) {
        txtSelectedProdCode.setText(String.valueOf(newValue.getCode()));
        txtSelectedProdDescription.setText(newValue.getDescription());
    }

    private void loadProductId() {
        try {
            txtProductCode.setText(String.valueOf(bo.getLastProductId()));
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public void btnBackToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }

    public void btnNewProductOnAction(ActionEvent actionEvent) {
        try {
            if (btnSaveUpdate.getText().equals("Save Product")) {

                if (bo.saveProduct(new ProductDto(Integer.parseInt(txtProductCode.getText()), txtProductDescription.getText()))) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Product Saved!").show();
                    clearFields();
                    loadAllProducts(searchText);
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                }
            } else {
                if (bo.saveProduct(new ProductDto(Integer.parseInt(txtProductCode.getText()), txtProductDescription.getText()))) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Product Updated!").show();
                    clearFields();
                    loadAllProducts(searchText);
                    //---------
                    btnSaveUpdate.setText("Save Product");
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
    

    public void btnSaveUpdateOnAction(ActionEvent actionEvent) {
    }

    private void loadAllProducts(String searchText) {
    }

    private void clearFields() {
        txtProductCode.clear();
        txtProductDescription.clear();
        loadProductId();
    }

    public void setUi(String url) throws IOException {
        Stage stage = (Stage)contextProduct.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+url+".fxml"))));
        stage.centerOnScreen();

    }
}
