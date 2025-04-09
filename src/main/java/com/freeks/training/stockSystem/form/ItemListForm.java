package com.freeks.training.stockSystem.form;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ItemListForm extends BaseForm {
	private List<ItemInfoForm> itemInfoFormList; //商品情報Formリスト
}
