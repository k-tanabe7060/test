package com.freeks.training.quiz;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class QuizController {
	final int LOOP_COUNT = 3;
	final int DEFAULT_CHECKED = 1;
	@Autowired
	MessageSource msg;

	@GetMapping("/quizQuestion")
	public String getQuestion(Model model) {
		QuizForm form = new QuizForm();
		for (int i = 1; i <= LOOP_COUNT; i++) {
			String title = msg.getMessage("quiz.question.title" + i, null, null);
			String body = msg.getMessage("quiz.question.body" + i, null, null);
			String choice = msg.getMessage("quiz.question.choice" + i, null, null);

			List<String> choiceList = Arrays.asList(choice.split("\n"));

			switch (i) {
			case 1:
				form.setTitle1(title);
				form.setBody1(body);
				form.setChoiceList1(choiceList);
				form.setSelected1(DEFAULT_CHECKED);
				break;
			case 2:
				form.setTitle2(title);
				form.setBody2(body);
				form.setChoiceList2(choiceList);
				form.setSelected2(DEFAULT_CHECKED);
				break;
			case 3:
				form.setTitle3(title);
				form.setBody3(body);
				form.setChoiceList3(choiceList);
				form.setSelected3(DEFAULT_CHECKED);
				break;
			default:
				break;
			}
		}
		model.addAttribute("quizForm", form);
		return "quiz/quizQuestion";
	}

	@PostMapping("/quizResult")
	public String result(QuizForm form, Model model) {

		int correctCount = 0;

		for (int i = 1; i <= LOOP_COUNT; i++) {
			String correctStr = msg.getMessage("quiz.question.correct" + i, null, null);
			int correct = Integer.parseInt(correctStr);
			switch (i) {
			case 1:
				if (form.getSelected1() == correct) {
					correctCount++;
				}
				break;
			case 2:
				if (form.getSelected2() == correct) {
					correctCount++;
				}
				break;
			case 3:
				if (form.getSelected3() == correct) {
					correctCount++;
				}
				break;
			default:
				break;
			}
		}
		form.setResultMessage(msg.getMessage("quiz.msg.correct" + correctCount, null, null));
		model.addAttribute("quizForm", form);
		return "quiz/quizResult";
	}
}
