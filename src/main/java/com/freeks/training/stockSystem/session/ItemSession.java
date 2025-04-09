package com.freeks.training.stockSystem.session;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.freeks.training.stockSystem.form.ItemInfoForm;
import com.freeks.training.stockSystem.form.ItemStockForm;

import lombok.Data;

//このクラスは、「更新前のフォームを格納する変数」定義用クラス的な感じ
@Data
@Component

@SessionScope
//セッションが続いている間はBeanを保持しといて！って時に付けるやつ
//ユーザーがショッピングカートにアイテムを入れてその後ページを巡回している間は
//カートにアイテムが入ったまま保持させておいて！みたいな。
//ページから離脱したらセッションアウトする！

public class ItemSession {
	private ItemInfoForm infoForm; //商品情報のフォーム
	private ItemInfoForm beforeForm; //商品情報更新前のフォーム
	private ItemStockForm stockForm; //在庫情報のフォーム
	private ItemStockForm beforeStockForm; //在庫情報更新前のフォーム
	
	public void infoFormToNull() {
		this.setInfoForm(null);
		this.setBeforeForm(null);
	}
	public void stockFormToNull() {
		this.setStockForm(null);
		this.setBeforeStockForm(null);
	}
	
}
