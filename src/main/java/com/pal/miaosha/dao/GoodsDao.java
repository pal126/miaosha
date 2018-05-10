package com.pal.miaosha.dao;

import com.pal.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsDao {

    /**
     * 商品列表
     * @return
     */
    @Select("select g.*,mg.stock_count,mg.miaosha_price,mg.start_date,mg.end_date from miaosha_goods mg left join goods g on mg.goods_id = g.id")
    List<GoodsVo> listGoodsVo();

    /**
     * 商品详情
     * @param id
     * @return
     */
    @Select("select g.*,mg.stock_count,mg.miaosha_price,mg.start_date,mg.end_date from miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id = #{id}")
    GoodsVo getGoodsVo(@Param("id") Long id);

}
