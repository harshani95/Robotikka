package com.devstack.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class CustomerFormController {
    public AnchorPane contextCustomer;
    public JFXTextField txtEmail;
    public JFXTextField txtSalary;
    public JFXTextField txtContact;
    public JFXTextField txtName;
    public JFXButton btnSaveUpdate;
    public TableView tblCustomer;
    public TableColumn colId;
    public TableColumn colEmail;
    public TableColumn colName;
    public TableColumn colContact;
    public TableColumn colSalary;
    public TableColumn colOperate;
    public TextField txtSearch;

    public void btnBackToHomeOnAction(ActionEvent actionEvent) {
    }

    public void btnManageLoyaltyCardsOnAction(ActionEvent actionEvent) {
    }

    public void btnNewCustomerOnAction(ActionEvent actionEvent) {
    }


    public void btnSaveUpdateOnAction(ActionEvent actionEvent) {
    }
}
