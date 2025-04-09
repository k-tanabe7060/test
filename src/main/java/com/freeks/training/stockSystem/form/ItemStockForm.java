package com.freeks.training.stockSystem.form;

import com.freeks.training.stockSystem.validate.CommonValidate;
import com.freeks.training.stockSystem.validate.RelatedStockValidate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ItemStockForm extends BaseForm {
	//在庫情報フォーム
	private int storageStockId; //在庫ID
	private int itemId; //商品ID
	private String stockQuantity; //在庫数
	@RelatedStockValidate
	private String receiveQuantity; //入庫数
	@RelatedStockValidate
	private String dispatchQuantity; //出庫数
	@CommonValidate
	private String storageLocation; //保管場所
	private String createUser; //作成者
	private String createDate; //作成日時
	private String updateUser; //更新者
	private String updateDate; //更新日時
}
