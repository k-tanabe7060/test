package com.freeks.training.stockSystem.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ItemInfoEntity {
	private int itemId; //商品ID
	private String itemName; //商品名称
	private String itemKubun; //区分
	private String maker; //メーカー名
	private String jancd; //JANコード
	private int purchaseUnitPrice; //購入単価
	private int salesUnitPrice; //販売単価
	private String storageLocation; //保管場所
	 @JsonFormat(pattern = "yyyy/MM/dd")
	private Date receiptDate; //入庫日
	private String createUser; //作成者
	private Date createDate; //作成日時
	private String updateUser; //更新者
	private Date updateDate; //更新日時
	private boolean logicalDeleteFlg; //論理削除フラグ
	private int version; //バージョン
}
