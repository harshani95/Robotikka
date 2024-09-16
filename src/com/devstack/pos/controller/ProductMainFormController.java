package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.ProductBo;
import com.devstack.pos.bo.custom.ProductDetailBo;
import com.devstack.pos.dto.ProductDetailDto;
import com.devstack.pos.dto.ProductDto;
import com.devstack.pos.enums.BoType;
import com.devstack.pos.view.tm.ProductDetailTm;
import com.devstack.pos.view.tm.ProductTm;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
    public AnchorPane context;
    public JFXButton btnNewBatch;
    public TextArea txtSelectedProductDescription;
    public TextField txtSelectedProductCode;
    public TableColumn colPDCode;
    public TableColumn colPDQty;
    public TableColumn colPDSellingPrice;
    public TableColumn colPDBuyingPrice;
    public TableColumn colPDDiscount;
    public TableColumn colPDShowPrice;
    public TableColumn colPDDelete;
    public TableView<ProductDetailTm> tblProductDetail;
    private String searchText = "";

    ProductBo bo = BoFactory.getInstance().getBo(BoType.PRODUCT);
    ProductDetailBo detailBo = BoFactory.getInstance().getBo(BoType.PRODUCT_DETAIL);

    public void initialize() throws SQLException, ClassNotFoundException {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colShowMore.setCellValueFactory(new PropertyValueFactory<>("showMore"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));

        colPDCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colPDQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colPDSellingPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        colPDBuyingPrice.setCellValueFactory(new PropertyValueFactory<>("buyingPrice"));
        colPDDiscount.setCellValueFactory(new PropertyValueFactory<>("discountAvailability"));
        colPDShowPrice.setCellValueFactory(new PropertyValueFactory<>("showPrice"));
        colPDDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));

        loadProductId();
        loadAllProducts(searchText);

        tblProduct.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setData(newValue);
        });

        tblProductDetail.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    try{
                        loadExternalUi(true, newValue);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                });
    }

    private void loadExternalUi(boolean state, ProductDetailTm tm) throws IOException {
        if (!txtSelectedProductCode.getText().isEmpty()){
            Stage stage = new Stage();
            FXMLLoader fxmlLoader =
                    new FXMLLoader(getClass()
                            .getResource("../view/NewBatchForm.fxml"));
            Parent parent = fxmlLoader.load();
            NewBatchFormController controller = fxmlLoader.getController();
            controller.setDetails(Integer.parseInt(txtSelectedProductCode.getText())
                    ,txtSelectedProductDescription.getText(),stage,state,tm);
            stage.setScene(new Scene(parent));
            stage.show();
            stage.centerOnScreen();
        }else{
            new Alert(Alert.AlertType.WARNING,"Please select a valid one!");
        }
    }

    private void setData(ProductTm newValue)  {
        txtSelectedProductCode.setText(String.valueOf(newValue.getCode()));
        txtSelectedProductDescription.setText(newValue.getDescription());
        btnNewBatch.setDisable(false);
        try {
            loadBatchData(newValue.getCode());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadBatchData(int code) throws SQLException, ClassNotFoundException {
        ObservableList<ProductDetailTm> obList = FXCollections.observableArrayList();
        for (ProductDetailDto p:detailBo.findAllProductDetails(code)
        ) {
            Button btn = new Button("Delete");
            ProductDetailTm tm = new ProductDetailTm(
                    p.getCode(),p.getQtyOnHand(),p.getSellingPrice(),
                    p.getBuyingPrice(),p.isDiscountAvailability(),
                    p.getShowPrice(),btn
            );
            obList.add(tm);
        }
        tblProductDetail.setItems(obList);
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

    public void btnSaveUpdateOnAction(ActionEvent actionEvent) {
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
    

    public void btnNewProductOnAction(ActionEvent actionEvent) throws IOException {

    }

    private void loadAllProducts(String searchText) throws SQLException, ClassNotFoundException {
        ObservableList<ProductTm> tms = FXCollections.observableArrayList();
        for (ProductDto dto : bo.findAllProducts()
        ) {
            Button showMore = new Button("Show more");
            Button delete = new Button("Delete");
            ProductTm tm = new ProductTm(dto.getCode(), dto.getDescription(), showMore, delete);
            tms.add(tm);
        }
        tblProduct.setItems(tms);
    }

    public void newBatchOnAction(ActionEvent actionEvent) throws IOException {
        loadExternalUi(false, null);
    }

    private void clearFields() {
        txtProductCode.clear();
        txtProductDescription.clear();
        loadProductId();
    }

    public void setUi(String url) throws IOException {
        Stage stage = (Stage)context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+url+".fxml"))));
        stage.centerOnScreen();
    }


}
