package com.devstack.pos.dao.custom.impl;

import com.devstack.pos.dao.CrudUtil;
import com.devstack.pos.dao.custom.OrderDetailDao;
import com.devstack.pos.entity.OrderDetail;

import java.sql.SQLException;
import java.util.List;

public class OrderDetailDaoImpl implements OrderDetailDao {
    @Override
    public boolean save(OrderDetail orderDetail) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO order_detail VALUES(?,?,?,?,?,?)",
                orderDetail.getCode(),
                orderDetail.getIssuedDate(),
                orderDetail.getTotalCost(),
                orderDetail.getCustomerEmail(),
                orderDetail.getDiscount(),
                orderDetail.getOperatorEmail());
    }

    @Override
    public boolean update(OrderDetail orderDetail) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(Integer integer) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public OrderDetail find(Integer integer) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public List<OrderDetail> findAll() throws SQLException, ClassNotFoundException {
        return null;
    }
}
