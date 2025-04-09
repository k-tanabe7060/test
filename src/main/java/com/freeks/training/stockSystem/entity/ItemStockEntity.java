package com.freeks.training.stockSystem.entity;

import java.util.Date;

import lombok.Data;

@Data
public class ItemStockEntity {
	private int storageStockId; //在庫ID
	private int itemId; //商品ID
	private int stockQuantity; //在庫数
	private int receiveQuantity; //入庫数
	private int dispatchQuantity; //出庫数
	private String storageLocation; //保管場所
	private String createUser; //作成者
	private Date createDate; //作成日時
	private String updateUser; //更新者
	private Date updateDate; //更新日時
	private boolean logicalDeleteFlg; //論理削除フラグ
	private int version; //バージョン
}