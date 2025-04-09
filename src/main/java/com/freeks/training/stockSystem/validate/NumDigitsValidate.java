package com.freeks.training.stockSystem.validate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

import com.freeks.training.stockSystem.validate.impl.NumDigitsValidateImpl;

@Target(ElementType.FIELD) // フィールドが適用対象
@Retention(RetentionPolicy.RUNTIME) // 実行時までアノテーション情報を保持する
@Constraint(validatedBy = { NumDigitsValidateImpl.class }) // バリデータクラスを登録
@ReportAsSingleViolation
public @interface NumDigitsValidate {

	String message() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}