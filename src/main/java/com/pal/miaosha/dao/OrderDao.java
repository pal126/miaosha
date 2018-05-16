package com.pal.miaosha.dao;

import com.pal.miaosha.domain.MiaoshaOrder;
import com.pal.miaosha.domain.OrderInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderDao {

    /**
     * 判断是否已经秒杀
     * @param userId
     * @param goodsId
     * @return
     */
    @Select("select * from miaosha_order where user_id = #{userId} and goods_id = #{goodsId}")
    MiaoshaOrder getOrderByUserIdGoodsId(@Param("userId") Long userId, @Param("goodsId") Long goodsId);

    /**
     * 下订单 orderInfo
     * @param orderInfo
     * @return
     */
    @Insert("insert into order_info (user_id, goods_id, goods_name, goods_count, goods_price, status, create_date) values " +
            "(#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{status}, #{createDate})")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = Long.class, before = false, statement = "select last_insert_id()")
    Long insertOrder(OrderInfo orderInfo);

    /**
     * 下订单 miaoshaOrder
     * @param miaoshaOrder
     * @return
     */
    @Insert("insert into miaosha_order (user_id, order_id, goods_id) values (#{userId}, #{orderId}, #{goodsId})")
    int insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);

    @Select("select * from order_info where id = #{orderId}")
    OrderInfo getOrderById(@Param("orderId") Long orderId);

}
