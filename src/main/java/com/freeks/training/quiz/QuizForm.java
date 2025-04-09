package com.freeks.training.quiz;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class QuizForm {
	private String title1;//設問１
	private String body1;//問題文１
	private List<String> choiceList1 = new ArrayList<String>();//選択肢１
	private int selected1;//選択した回答１
	private String title2;//設問２
	private String body2;//問題文２
	private List<String> choiceList2 = new ArrayList<String>();//選択肢２
	private int selected2;//選択した回答２
	private String title3;//設問３
	private String body3;//問題文３
	private List<String> choiceList3 = new ArrayList<String>();//選択肢３
	private int selected3;//選択した回答３
	private String resultMessage;//結果メッセージ

	public String getTitle1() {
		return title1;
	}

	public void setTitle1(String title1) {
		this.title1 = title1;
	}

	public String getBody1() {
		return body1;
	}

	public void setBody1(String body1) {
		this.body1 = body1;
	}

	public List<String> getChoiceList1() {
		return choiceList1;
	}

	public void setChoiceList1(List<String> choiceList1) {
		this.choiceList1 = choiceList1;
	}

	public int getSelected1() {
		return selected1;
	}

	public void setSelected1(int selected1) {
		this.selected1 = selected1;
	}

	public String getTitle2() {
		return title2;
	}

	public void setTitle2(String title2) {
		this.title2 = title2;
	}

	public String getBody2() {
		return body2;
	}

	public void setBody2(String body2) {
		this.body2 = body2;
	}

	public List<String> getChoiceList2() {
		return choiceList2;
	}

	public void setChoiceList2(List<String> choiceList2) {
		this.choiceList2 = choiceList2;
	}

	public int getSelected2() {
		return selected2;
	}

	public void setSelected2(int selected2) {
		this.selected2 = selected2;
	}

	public String getTitle3() {
		return title3;
	}

	public void setTitle3(String title3) {
		this.title3 = title3;
	}

	public String getBody3() {
		return body3;
	}

	public void setBody3(String body3) {
		this.body3 = body3;
	}

	public List<String> getChoiceList3() {
		return choiceList3;
	}

	public void setChoiceList3(List<String> choiceList3) {
		this.choiceList3 = choiceList3;
	}

	public int getSelected3() {
		return selected3;
	}

	public void setSelected3(int selected3) {
		this.selected3 = selected3;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
}
