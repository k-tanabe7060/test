package com.freeks.training.stockSystem.validate.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.freeks.training.stockSystem.validate.Limit10Validate;
import com.freeks.training.stockSystem.validate.ValidationItem;

@Component
public class Limit10ValidateImpl implements ConstraintValidator<Limit10Validate, Object> {

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

		} else if (item.isBetween(str, 1, 10)) {
			//1文字以上10字以下で入力してください
			return item.createErrorWithMsg(msg.getMessage("Valied.Range", new String[] { "1", "10" }, null), context);
			
		} else if (item.isHalfWithNum(str)) {
			//半角数字のみで入力して下さい
			return item.createErrorWithMsg(msg.getMessage("Valied.Integer", null, null), context);
		}
		return true;
	}
}