package com.freeks.training.stockSystem.validate;

import jakarta.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

@Component
public class ValidationItem {
	
	//		* null判定チェック
	//		* @param obj 
	//		* @return
	public boolean isNull(Object obj) {
		return obj == null;
	}
	//		* 文字列が空かチェック 
	//		* @param value 
	//		* @return 

	public boolean isEmpty(String value) {
		return value.isEmpty();
	}
	//		* バリデーションエラー生成処理 
	//		* @param msg     エラーメッセージ 
	//		* @param context バリデータ情報 
	//		* @return 常にfalseを返す

	public boolean createErrorWithMsg(String msg, ConstraintValidatorContext context) {
		// デフォルトのエラーメッセージを無効化 
		context.disableDefaultConstraintViolation();
		// バリデーションの独自エラーメッセージ設定 
		context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
		return false;
	}
	
	//未入力（空白）の場合
	public boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}
	
	// 入力が全て半角または全角スペースのみの場合
	public boolean isOnlySpaces(String value) {
	    return value != null && value.matches("[ 　]+");
	}
	
	//開始文字が半角空白または全角空白の場合
	public boolean isFirstSpace(String value) {
		return value.startsWith(" ") || value.startsWith("　");
	}
	
	//終了文字が半角空白または全角空白の場合
	public boolean isLastSpace(String value) {
		return value.endsWith(" ") || value.endsWith("　");
	}
	
	//最小文字数未満または最高文字数を上回る場合
	public boolean isBetween(String value, int min, int max) {
		return value.length() < min || value.length() > max;
	}
	
	//13桁で入力して下さい
	public boolean is13Digits(String value, int min, int max) {
		return value.length() < min || value.length() >= max;
	}

	//半角数字のみで入力して下さい
	public boolean isHalfWithNum(String value) {
		return !value.matches("\\d+");
	}
	
	//半角英数字のみで入力して下さい
	public boolean isHalfWithChar(String value) {
		return !value.matches("^[a-zA-Z0-9]+$");
	}

}