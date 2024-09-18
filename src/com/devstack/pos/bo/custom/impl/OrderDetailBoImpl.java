package com.devstack.pos.bo.custom.impl;

import com.devstack.pos.bo.custom.OrderDetailBo;
import com.devstack.pos.dao.DaoFactory;
import com.devstack.pos.dao.custom.ItemDetailDao;
import com.devstack.pos.dao.custom.OrderDetailDao;
import com.devstack.pos.dao.custom.ProductDetailDao;
import com.devstack.pos.db.DbConnection;
import com.devstack.pos.dto.ItemDetailDto;
import com.devstack.pos.dto.OrderDetailDto;
import com.devstack.pos.entity.ItemDetail;
import com.devstack.pos.entity.OrderDetail;
import com.devstack.pos.enums.DaoType;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderDetailBoImpl implements OrderDetailBo {

    OrderDetailDao dao = DaoFactory.getInstance().getDao(DaoType.ORDER_DETAIL);
    ProductDetailDao productDetailDao = DaoFactory.getInstance().getDao(DaoType.PRODUCT_DETAIL);

    @Override
    public boolean makeOrder(OrderDetailDto orderDetailDto) throws SQLException {
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            if (saveOrder(orderDetailDto)) {
                boolean isItemsSaved = saveItemDetails(orderDetailDto.getItemDetailDto(), orderDetailDto.getCode());
                if (isItemsSaved) {
                    connection.commit();
                    return true;
                } else {
                    connection.rollback();
                    return false;
                }
            } else {
                connection.rollback();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.setAutoCommit(true);
        }
        return false;
    }

    private boolean saveItemDetails(List<ItemDetailDto> list, int orderCode) {
        for (ItemDetailDto dto : list
        ) {
            boolean isItemSaved = DetailDao.save(
                    new ItemDetail(dto.getDetailCode(), orderCode,
                            dto.getQty(), dto.getDiscount(), dto.getAmount())
            );
            if (isItemSaved) {
                if (!updateQty(dto.getDetailCode(), dto.getQty())) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean updateQty(String detailCode, int qty) {
        return productDetailDao.manageQty(productCode, qty);
    }

    private boolean saveOrder(OrderDetailDto orderDetailDto) throws SQLException, ClassNotFoundException {
        return dao.save(
                new OrderDetail(orderDetailDto.getCode(),
                        orderDetailDto.getIssuedDate(), orderDetailDto.getTotalCost(),
                        orderDetailDto.getCustomerEmail(),orderDetailDto.getDiscount(),
                        orderDetailDto.getOperatorEmail())
        );
    }
}
