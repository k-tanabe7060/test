package com.freeks.training.stockSystem.validate.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.freeks.training.stockSystem.validate.NumDigitsValidate;
import com.freeks.training.stockSystem.validate.ValidationItem;

@Component
public class NumDigitsValidateImpl implements ConstraintValidator<NumDigitsValidate, Object> {

	@Autowired
	ValidationItem item;

	@Autowired
	MessageSource msg;

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {

		// バリデート対象となる値 
		String str = (String) value;

		if (item.isNull(value) || item.isEmpty(str)) {
			//必須項目です
			return item.createErrorWithMsg(msg.getMessage("Valied.Required", null, null), context);

		} else if (item.isBlank(str) || item.isOnlySpaces(str)) {
			//空白以外で入力して下さい
			return item.createErrorWithMsg(msg.getMessage("Valied.Blank", null, null), context);

		} else if (item.isFirstSpace(str)) {
			//文字の先頭が空白になっています
			return item.createErrorWithMsg(msg.getMessage("Valied.BlankFirst", null, null), context);

		} else if (item.isLastSpace(str)) {
			//文字の末尾が空白になっています
			return item.createErrorWithMsg(msg.getMessage("Valied.BlankLast", null, null), context);

		} else if (item.isHalfWithChar(str)) {
			//半角英数字のみで入力して下さい
			return item.createErrorWithMsg(msg.getMessage("Valied.AlphaDigit", null, null), context);
			
		} else if (item.is13Digits(str, 12, 14)) {
			//13桁で入力して下さい
			return item.createErrorWithMsg(msg.getMessage("Valied.Digit", new String[] { "13" }, null), context);

		}
		return true;
	}
}