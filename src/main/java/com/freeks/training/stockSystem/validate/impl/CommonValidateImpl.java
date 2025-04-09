package com.freeks.training.stockSystem.validate.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.freeks.training.stockSystem.validate.CommonValidate;
import com.freeks.training.stockSystem.validate.ValidationItem;

@Component
public class CommonValidateImpl implements ConstraintValidator<CommonValidate, Object> {

	@Autowired
	ValidationItem item;

	@Autowired
	MessageSource msg;

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {

		// バリデート対象となる値 
		String str = (String) value;

		if (item.isNull(value) || item.isEmpty(str)) {
			return item.createErrorWithMsg(msg.getMessage("Valied.Required", null, null), context);

		} else if (item.isBlank(str) || item.isOnlySpaces(str)) {
			return item.createErrorWithMsg(msg.getMessage("Valied.Blank", null, null), context);

		} else if (item.isFirstSpace(str)) {
			return item.createErrorWithMsg(msg.getMessage("Valied.BlankFirst", null, null), context);

		} else if (item.isLastSpace(str)) {
			return item.createErrorWithMsg(msg.getMessage("Valied.BlankLast", null, null), context);

		} else if (item.isBetween(str, 1, 30)) {
			return item.createErrorWithMsg(msg.getMessage("Valied.Range", new String[] { "1", "30" }, null), context);
		}
		return true;
	}
}