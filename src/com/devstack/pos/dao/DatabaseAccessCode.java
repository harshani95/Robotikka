package com.devstack.pos.dao;

import com.devstack.pos.dto.Customer;
import com.devstack.pos.dto.UserDto;
import com.devstack.pos.util.PasswordManager;

import java.sql.*;
import java.util.List;

public class DatabaseAccessCode {

    //========user management==========//

    public static boolean createUser(
            String email,
            String password
    ) throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/robotikka", "root", "1218");
        String sql = "INSERT INTO user VALUES (?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, PasswordManager.encryptPassword(password));
        return preparedStatement.executeUpdate()>0;
    }

    public static UserDto findUser(String email) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/robotikka", "root", "1218");
        String sql = "SELECT * FROM user WHERE email=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, email);

        ResultSet set = preparedStatement.executeQuery();
        if (set.next()) {
            return new UserDto(
                    set.getString(1),
                    set.getString(2));
        }
        return null;
    }

    //========user management==========//


    //========customer management==========//

    public static boolean createCustomer(String email, String name, String contact, double salary){
        return false;
    }

    public static boolean updateCustomer(String email, String name, String contact, double salary){
        return false;
    }

    public static Customer findCustomer(String email){
        return null;
    }

    public static List<Customer> findAllCustomers(){
        return null;
    }

    public static boolean searchCustomer(String searchText){
        return false;
    }

    //========customer management==========//
}
