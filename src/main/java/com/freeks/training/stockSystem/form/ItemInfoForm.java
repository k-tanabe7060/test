package com.freeks.training.stockSystem.form;

import com.freeks.training.stockSystem.validate.CommonValidate;
import com.freeks.training.stockSystem.validate.Limit10Validate;
import com.freeks.training.stockSystem.validate.NumDigitsValidate;
import com.freeks.training.stockSystem.validate.NumberValidate;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class ItemInfoForm extends BaseForm {
	private int itemId; //商品ID
	@NumberValidate
	private String itemName; //商品名称
	@CommonValidate
	private String itemKubun; //区分
	@NumberValidate
	private String maker; //メーカー名
	@NumDigitsValidate
	private String jancd; //JANコード
	@Limit10Validate
	private String purchaseUnitPrice; //購入単価
	@Limit10Validate
	private String salesUnitPrice; //販売単価
	@NumberValidate
	private String storageLocation; //保管場所
	private String receiptDate; //入庫日
	private String createUser; //作成者
	private String createDate; //作成日時
	private String updateUser; //更新者
	private String updateDate; //更新日時

}
