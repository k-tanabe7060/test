package com.freeks.training.stockSystem.util;

public enum MessageEnum {
	SYSTEM_ERROR_GET_DB_DATA("システムエラー：DB接続に失敗しました"),
	ITEM_INFO_NOT_FOUND("システムエラー：商品情報が見つかりません"),
	STOCK_INFO_NOT_FOUND("在庫情報が見つかりません"), 
	DUPLICATE_ITEM_NAME("商品名称が重複しています"),
	FAILED_ITEM_DATABASE("商品情報の更新に失敗しました"),
	SUCCESS_ITEM_DATA_BASE("商品情報を更新しました"),
	STOCK_NOUPDATE_INFO("更新情報に変更箇所がありません"),
	STOCK_QUANTITY_SHORTAGE("在庫数が不足しています"),
	FAILED_STOCK_DATABASE("在庫情報の更新に失敗しました"),
	SUCCESS_STOCK_DATA_BASE("在庫情報を更新しました"),
	FAILED_DELETE_ITEM("商品情報の削除に失敗しました"),
	SUCCESS_DELETE_ITEM("商品情報を削除しました"),
	//自作したやつ↓
	SUCCESS_ITEM_REGISTER("商品情報を登録しました"),
	FAILD_USERID_OR_PASSWORD("ユーザーIDまたはパスワードが異なります"),
	UNEXPECTED_ERROR("予期せぬエラーが発生しました");
	
	//フィールド変数を定義
	private String message; //メッセージ
	
	//コンストラクタを定義
	MessageEnum(String message) {
		this.message = message;
	}

	//ゲッターを定義
	public String getMessage() {
		return message;
	}
}
