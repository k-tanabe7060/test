package com.freeks.training.stockSystem.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.freeks.training.stockSystem.entity.ItemInfoEntity;
import com.freeks.training.stockSystem.entity.ItemStockEntity;

@Repository
@Mapper
public interface ItemInfoMapper {
	//抽象メソッド
	public List<ItemInfoEntity> getFindAll();
	
	//ページング機能用（1ページの表示件数と取得開始位置を取得）
	public List<ItemInfoEntity> getPagingInfo(int size, int offset);
	
	@Select("SELECT COUNT(*) FROM stock_system.m_item")
	int getTotalCount();

	//商品情報取得
	public ItemInfoEntity findByItemId(int itemId);

	//商品情報取得 
	public ItemInfoEntity findByItemName(String itemName);

	//在庫情報取得 
	public ItemStockEntity findStockByItemId(int itemId);

	//商品情報登録 
	public int insert(ItemInfoEntity entity);

	//在庫情報登録 
	public int stockInsert(ItemStockEntity entity);

	//商品情報更新 
	public int updateItemInfo(ItemStockEntity entity);

	//在庫情報更新 
	public int updateStock(ItemStockEntity entity);

	//商品情報削除
	public int delete(int itemId);

	//在庫情報削除 
	public int deleteStock(int itemId);
}
