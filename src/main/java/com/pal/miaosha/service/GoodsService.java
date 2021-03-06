package com.pal.miaosha.service;

import com.pal.miaosha.dao.GoodsDao;
import com.pal.miaosha.domain.MiaoshaGoods;
import com.pal.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;

    /**
     * 商品列表
     *
     * @return
     */
    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }

    /**
     * 商品详情
     *
     * @return
     */
    public GoodsVo getGoodsVo(Long id) {
        return goodsDao.getGoodsVo(id);
    }

    /**
     * 减库存
     *
     * @param goodsVo
     * @return
     */
    public boolean reduceStock(GoodsVo goodsVo) {
        MiaoshaGoods miaoshaGoods = new MiaoshaGoods();
        miaoshaGoods.setGoodsId(goodsVo.getId());
        int count = goodsDao.reduceStock(miaoshaGoods);
        return count > 0;
    }

}
