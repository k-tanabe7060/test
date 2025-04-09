package com.freeks.training.stockSystem.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.freeks.training.stockSystem.entity.ItemInfoEntity;
import com.freeks.training.stockSystem.entity.ItemStockEntity;
import com.freeks.training.stockSystem.form.ItemInfoForm;
import com.freeks.training.stockSystem.form.ItemStockForm;
import com.freeks.training.stockSystem.util.CommonUtil;

@Component
public class ItemConverter {
	@Autowired
	private CommonUtil commonUtil;

	/**
	 * entity型リストをform型リストに変換する
	 * @param entityList entity型リスト
	 * @return 変換後のform型リストを返却
	 */
	public List<ItemInfoForm> entityListToFormList(List<ItemInfoEntity> entityList) {
		List<ItemInfoForm> itemInfoForm = new ArrayList<>();

		for (ItemInfoEntity entity : entityList) {
			
			ItemInfoForm form = new ItemInfoForm();
			
			form.setItemId(entity.getItemId());
			form.setItemName(entity.getItemName());
			form.setItemKubun(entity.getItemKubun());
			form.setMaker(entity.getMaker());
			form.setJancd(entity.getJancd());
			form.setPurchaseUnitPrice(String.valueOf(entity.getPurchaseUnitPrice()));
			form.setSalesUnitPrice(String.valueOf(entity.getSalesUnitPrice()));
			form.setStorageLocation(entity.getStorageLocation());
			form.setReceiptDate(commonUtil.convertDateToStrDate(entity.getReceiptDate()));
			form.setCreateUser(entity.getCreateUser());
			form.setCreateDate(commonUtil.convertDateToStrDate(entity.getCreateDate()));
			form.setUpdateUser(entity.getUpdateUser());
			form.setUpdateDate(commonUtil.convertDateToStrDate(entity.getUpdateDate()));
			
			itemInfoForm.add(form);

		}

		return itemInfoForm;
	}

	/**
	 * 商品情報詳細用コンバーター
	 * @param entity entyty型の商品情報リスト
	 * @return itemInfoFormにentity型の商品情報リストを設定し返却
	 */
	public ItemInfoForm entityToForm(ItemInfoEntity entity) {
		//戻り値ItemInfoFormに返す用の変数が必要
		ItemInfoForm itemInfoForm = new ItemInfoForm();

		itemInfoForm.setItemId(entity.getItemId());
		itemInfoForm.setItemName(entity.getItemName());
		itemInfoForm.setItemKubun(entity.getItemKubun());
		itemInfoForm.setMaker(entity.getMaker());
		itemInfoForm.setJancd(entity.getJancd());

		itemInfoForm.setPurchaseUnitPrice(String.valueOf(entity.getPurchaseUnitPrice()));
		itemInfoForm.setSalesUnitPrice(String.valueOf(entity.getSalesUnitPrice()));
		itemInfoForm.setStorageLocation(entity.getStorageLocation());
		itemInfoForm.setReceiptDate(commonUtil.convertDateToStrDate(entity.getReceiptDate()));
		itemInfoForm.setCreateUser(entity.getCreateUser());
		itemInfoForm.setCreateDate(commonUtil.convertDateToStrDate(entity.getCreateDate()));
		itemInfoForm.setUpdateUser(entity.getUpdateUser());
		itemInfoForm.setUpdateDate(commonUtil.convertDateToStrDate(entity.getUpdateDate()));

		//変換後のentityToItemInfoFormを格納したitemInfoFormを返却
		return itemInfoForm;

	}

	/**
	 * 在庫情報取得用コンバーター
	 * @param entity entity型の在庫情報リスト
	 * @return entity型在庫情報リストを変換し、戻り値に設定し返却
	 */
	public ItemStockForm entityToForm(ItemStockEntity entity) {
		//戻り値ItemInfoFormに返す用の変数が必要
		ItemStockForm itemStockForm = new ItemStockForm();

		itemStockForm.setStorageStockId(entity.getStorageStockId());
		itemStockForm.setItemId(entity.getItemId());
		itemStockForm.setStockQuantity(String.valueOf(entity.getStockQuantity()));
		itemStockForm.setReceiveQuantity(String.valueOf(entity.getReceiveQuantity()));
		itemStockForm.setDispatchQuantity(String.valueOf(entity.getDispatchQuantity()));
		itemStockForm.setStorageLocation(entity.getStorageLocation());
		itemStockForm.setCreateUser(entity.getCreateUser());
		itemStockForm.setCreateDate(commonUtil.convertDateToStrDate(entity.getCreateDate()));
		itemStockForm.setUpdateUser(entity.getUpdateUser());
		itemStockForm.setUpdateDate(commonUtil.convertDateToStrDate(entity.getUpdateDate()));

		//変換後のitemStockFormを返却                                                                                                 x
		return itemStockForm;

	}

	/**
	 * 在庫情報更新用コンバーター
	 * @param form 新規入力したフォーム
	 * @param beforeForm 既存のフォーム
	 * @param loginUser 固定値
	 * @return 在庫数・入庫数・出庫数を計算し結果を返却
	 */
	public ItemStockEntity formToEntity(ItemStockForm form, ItemStockForm beforeForm, String loginUser) {
		//↓戻り値用の変数は真っ先に定義しろ
		ItemStockEntity itemStockEntity = new ItemStockEntity();
		//指示書の指示通り計算式を作る

		//計算するときにint型に変換しないといけないので先に変換した
		int beforeFormReceiveQuantity = Integer.parseInt(beforeForm.getReceiveQuantity());
		int beforeFormStockQuantity = Integer.parseInt(beforeForm.getStockQuantity());
		int beforeFormDispatchQuantity = Integer.parseInt(beforeForm.getDispatchQuantity());

		int formReceiveQuantity = Integer.parseInt(form.getReceiveQuantity());
		int formDispatchQuantity = Integer.parseInt(form.getDispatchQuantity());

		//在庫数 = 更新前の在庫数 + ( 新入庫数 - 新出庫数 ）
		itemStockEntity.setStockQuantity(beforeFormStockQuantity + formReceiveQuantity - formDispatchQuantity);

		//入庫数 = 更新前入庫数 + 新入庫数
		itemStockEntity.setReceiveQuantity(beforeFormReceiveQuantity + formReceiveQuantity);

		//出庫数 = 更新前出庫数 + 新出庫数
		itemStockEntity.setDispatchQuantity(beforeFormDispatchQuantity + formDispatchQuantity);

		itemStockEntity.setUpdateUser(loginUser);

		itemStockEntity.setUpdateDate(commonUtil.nowDate());

		//itemStockEntityにstorageStockIdを格納
		itemStockEntity.setStorageStockId(form.getStorageStockId());
		itemStockEntity.setStorageLocation(form.getStorageLocation());
		itemStockEntity.setItemId(form.getItemId());

		return itemStockEntity;
	}

	/**
	 * 商品情報登録用コンバーター
	 * @param form 新規入力したフォーム
	 * @param loginUser 固定値
	 * @return 入力したフォームをentity型リストに変換及び設定し返却
	 */
	public ItemInfoEntity insertFormToEntity(ItemInfoForm form, String loginUser) {
		ItemInfoEntity itemInfoEntity = new ItemInfoEntity();
		//itemInfoEntity(itemInfoEntityクラスの変数にユーザーが入力したフォーム内容を設定）
		itemInfoEntity.setItemId(form.getItemId());
		itemInfoEntity.setItemName(form.getItemName());
		itemInfoEntity.setItemKubun(form.getItemKubun());
		itemInfoEntity.setMaker(form.getMaker());
		itemInfoEntity.setJancd(form.getJancd());
		itemInfoEntity.setPurchaseUnitPrice(Integer.parseInt(form.getPurchaseUnitPrice()));
		itemInfoEntity.setSalesUnitPrice(Integer.parseInt(form.getSalesUnitPrice()));
		itemInfoEntity.setStorageLocation(form.getStorageLocation());
		itemInfoEntity.setReceiptDate(commonUtil.nowDate());
		itemInfoEntity.setCreateUser(loginUser);
		itemInfoEntity.setCreateDate(commonUtil.nowDate());
		itemInfoEntity.setUpdateUser(loginUser);
		itemInfoEntity.setUpdateDate(commonUtil.nowDate());
		itemInfoEntity.setLogicalDeleteFlg(false);
		itemInfoEntity.setVersion(0);
		return itemInfoEntity;
	}

}
