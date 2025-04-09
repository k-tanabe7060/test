package com.freeks.training.stockSystem.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.freeks.training.stockSystem.form.ItemInfoForm;
import com.freeks.training.stockSystem.form.ItemListForm;
import com.freeks.training.stockSystem.form.ItemStockForm;
import com.freeks.training.stockSystem.service.ItemService;
import com.freeks.training.stockSystem.session.ItemSession;

@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;

	//autowired をつけると、componentなどでbeanに登録されたクラスをnewなしで使える
	@Autowired
	private ItemSession itemSession;

	//messages.propertiesのエラーメッセージを使用するためにBeanを注入
	@Autowired
	private MessageSource messageSource;

	final String loginUser = "freeks";

	/**
	 * 商品情報一覧画面を表示する
	 * @param model 画面に渡すデータを格納するオブジェクト
	 * @return 商品一覧画面のHTMLテンプレート（stockSystem/itemList）
	 */
	@GetMapping(value = "/itemList")
	public String itemList(Model model) {
		ItemListForm itemListAll = itemService.getItemListAll();
		model.addAttribute("itemListForm", itemListAll);
		return "stockSystem/itemList";
	}

	/**
	 * ページング機能ボタン押下→商品情報リストへ遷移
	 * @param page 現在のページ
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/items")
	public String items(@RequestParam(defaultValue = "1") int page, Model model) {
		int perPage = 10; //1ページの表示件数
		Map<String, Object> data = itemService.getItemListAllForPaging(page, perPage);
		model.addAttribute("itemListForm", data.get("itemListForm")); //Objectのキーを使って取得
		model.addAttribute("currentPage", data.get("currentPage"));
		model.addAttribute("totalPage", data.get("totalPage"));
		return "stockSystem/itemListPaging";
	}

	/**
	 * 商品情報詳細画面へ遷移する
	 * @param itemId 商品ID
	 * @param model 画面に渡すデータを格納するオブジェクト
	 * @return 商品情報詳細画面のHTMLテンプレート（stockSystem/itemDetail）
	 */
	@GetMapping(value = "/itemDetail/{itemId}")
	public String itemDetail(@PathVariable("itemId") String itemId, Model model) {
		//リダイレクトチェック
		if (!model.containsAttribute("itemInfoForm")) {
			model.addAttribute("itemInfoForm", new ItemInfoForm());
		}
		System.out.println(new ItemInfoForm());
		int itemIdInt = Integer.parseInt(itemId);//itemidがString型なのでint型に変換している
		ItemInfoForm itemInfoForm = itemService.getItemDetail(itemIdInt);
		model.addAttribute("itemInfoForm", itemInfoForm);
		return "stockSystem/itemDetail";
	}

	/**
	 * 在庫情報詳細画面へ遷移する
	 * @param itemId 商品ID
	 * @param model	画面に渡すデータを格納するオブジェクト
	 * @return 在庫情報詳細画面のHTMLテンプレート（stockSystem/itemStock）
	 */
	@GetMapping(value = "/itemStock/{itemId}")
	public String itemStock(@PathVariable("itemId") String itemId, Model model) {
		//		//リダイレクトチェック
		if (model.containsAttribute("itemStockForm")) {
			return "stockSystem/itemStock";
		}

		int itemIdInt = Integer.parseInt(itemId);//itemidがString型なのでint型に変換している

		//ItemStockformが存在する場合商品セッションのItemStockFormを返す
		ItemStockForm itemStockForm = new ItemStockForm();
		if (itemSession.getStockForm() != null) {
			itemStockForm = itemSession.getStockForm();
		} else {
			itemStockForm = itemService.getItemStock(itemIdInt);
			//if文の変数は、ifとelseで被ってもOK。どっちかの処理しか実行されないので
		}

		//商品セッション.在庫情報更新前Formチェック
		//在庫情報更新前Formが存在しない場合、先程取得したItemStockFormを設定する
		if (itemSession.getBeforeStockForm() == null) {
			itemSession.setBeforeStockForm(itemStockForm);
		}
		model.addAttribute("itemStockForm", itemStockForm);
		return "stockSystem/itemStock";

	}

	/**
	 * 在庫情報を更新する
	 * @param itemStockForm 在庫情報
	 * @param result バリデーションチェック結果格納オブジェクト
	 * @param re リダイレクト時のメッセージを格納するオブジェクト
	 * @param model 画面に渡すデータを格納するオブジェクト
	 * @return 商品情報一覧画面のHTMLテンプレート（stockSystem/itemList）
	 */
	@PostMapping(value = "/stockUpdate")
	public String stockUpdate(@ModelAttribute @Validated ItemStockForm itemStockForm, BindingResult result,
			RedirectAttributes re, Model model) {
		//バリデーションチェック
		if (result.hasErrors()) {
			//Formにエラーメッセージを設定
			itemStockForm.setErrMsg(messageSource.getMessage("Valied.FailInputValue", null, null));//MessagePropertyからエラーメッセージを取得
			re.addFlashAttribute("itemStockForm", itemStockForm);
			//RedirectAttributes（re）にバリデーション結果を設定
			re.addFlashAttribute("org.springframework.validation.BindingResult.itemStockForm", result);
			//RedirectAttributeにFormを設定
			re.addFlashAttribute("itemStockForm", itemStockForm);
			//リダイレクトする
			return "redirect:/itemStock/" + itemStockForm.getItemId();
		}

		ItemStockForm beforeForm = itemSession.getBeforeStockForm();
		//更新情報の確認を行う（サービスクラスのupdateCheckメソッド使用）

		String updateCheckResult = itemService.updateCheck(beforeForm, itemStockForm);
		//更新情報確認結果チェック

		if (updateCheckResult != null) {
			itemStockForm.setErrMsg(updateCheckResult);
			re.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "itemStockForm", result);
			re.addFlashAttribute("itemStockForm", itemStockForm);
			return "redirect:/itemStock/" + beforeForm.getItemId();
		}
		//在庫情報の更新
		ItemListForm updateItemStock = itemService.updateItemStock(itemStockForm, beforeForm, loginUser);
		//商品のセッションをクリア
		itemSession.infoFormToNull();
		itemSession.stockFormToNull();
		model.addAttribute("itemListForm", updateItemStock);
		return "stockSystem/itemList";
	}

	/**
	 * 商品情報登録画面へ遷移する
	 * @param model 画面に渡すデータを格納するオブジェクト
	 * @return 商品情報登録画面のHTMLテンプレート（stockSystem/itemList）
	 */
	@GetMapping(value = "/itemInsert")
	public String itemInsert(Model model) {

		//リダイレクトチェック
		if (model.containsAttribute("itemInfoForm")) {
			return "stockSystem/itemInsert";
		}
		//ItemInfoFormが存在する場合、商品セッションのItemInfoFormを取得する
		ItemInfoForm itemInfoForm = new ItemInfoForm();
		
		if (itemSession.getBeforeForm() != null) {
			itemInfoForm = itemSession.getBeforeForm();
		}
		model.addAttribute("itemInfoForm", itemInfoForm);
		return "stockSystem/itemInsert";
	}

	////登録
	/**
	 * 商品情報を登録をする
	 * @param itemInfoForm ユーザーが入力した商品情報を格納する
	 * @param result バリデーションチェック結果格納オブジェクト
	 * @param re リダイレクト時のメッセージを格納するオブジェクト
	 * @param model 画面に渡すデータを格納するオブジェクト
	 * @return 商品情報一覧画面のHTMLテンプレート（stockSystem/itemList）
	 */
	@PostMapping(value = "/itemCreate")
	public String itemCreate(@ModelAttribute @Validated ItemInfoForm itemInfoForm, BindingResult result,
			RedirectAttributes re, Model model) {

		//BindingResultを使用してバリデーションチェック
		if (result.hasErrors()) {
			//Formにエラーメッセージを設定
			itemInfoForm.setErrMsg(messageSource.getMessage("Valied.FailInputValue", null, null));//MessagePropertyからエラーメッセージを取得
			re.addFlashAttribute("itemInfoForm", itemInfoForm);
			//RedirectAttributes（re）にバリデーション結果を設定
			re.addFlashAttribute("org.springframework.validation.BindingResult.itemInfoForm", result);
			//RedirectAttributeにFormを設定
			re.addFlashAttribute("itemInfoForm", itemInfoForm);
			//リダイレクトする
			return "redirect:/itemInsert";
		}

		//商品名称重複チェック
		String itemName = itemInfoForm.getItemName();
		String checkResult = itemService.checkDuplicateItemName(itemName);

		if (checkResult != null) {
			itemInfoForm.setErrMsg(checkResult);
			//BindingResultのやつをつけると、入力した項目が保持される
			re.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "itemInfoForm", result);
			//RedirectAttributeにFormを設定
			re.addFlashAttribute("itemInfoForm", itemInfoForm);
			//リダイレクトする
			return "redirect:/itemInsert";
		}
		//商品登録処理
		ItemListForm itemListForm = new ItemListForm();
		itemListForm = itemService.createItem(itemInfoForm, loginUser);

		//商品のセッションをクリア
		itemSession.infoFormToNull();
		itemSession.stockFormToNull();
		model.addAttribute("itemListForm", itemListForm);
		return "stockSystem/itemList";
	}

	/**
	 * 商品情報を削除する
	 * @param itemInfoForm 削除する商品情報データ
	 * @param model 画面に渡すデータを格納するオブジェクト
	 * @return 商品情報一覧画面のHTMLテンプレート（stockSystem/itemList）
	 */
	@PostMapping(value = "/itemDelete")
	public String itemDelete(ItemInfoForm itemInfoForm, Model model) {
		ItemListForm itemListForm = new ItemListForm();
		//削除メソッド呼び出す
		itemListForm = itemService.deleteItem(itemInfoForm);
		//商品セッションのすべてのformをクリアにする
		itemSession.infoFormToNull();
		itemSession.stockFormToNull();
		model.addAttribute("itemListForm", itemListForm);
		return "stockSystem/itemList";
	}

	/**
	 * 一つ前のページへ戻るボタン
	 * 現在使ってない（フロント側も一旦コメントアウトしている）
	 * @param model 画面に渡すデータを格納するオブジェクト
	 * @return 商品情報一覧画面のHTMLテンプレート（stockSystem/itemList）
	 */
	@GetMapping(value = "/returnPageToItemList")
	public String returnPageToItemList(Model model) {
		ItemListForm itemListForm = itemService.getItemListAll();
		itemSession.infoFormToNull();
		itemSession.stockFormToNull();
		model.addAttribute("itemListForm", itemListForm);
		return "stockSystem/itemList";
	}

}
