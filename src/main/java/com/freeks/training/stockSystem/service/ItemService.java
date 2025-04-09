package com.freeks.training.stockSystem.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freeks.training.stockSystem.converter.ItemConverter;
import com.freeks.training.stockSystem.entity.ItemInfoEntity;
import com.freeks.training.stockSystem.entity.ItemStockEntity;
import com.freeks.training.stockSystem.form.ItemInfoForm;
import com.freeks.training.stockSystem.form.ItemListForm;
import com.freeks.training.stockSystem.form.ItemStockForm;
import com.freeks.training.stockSystem.mapper.ItemInfoMapper;
import com.freeks.training.stockSystem.util.CommonUtil;
import com.freeks.training.stockSystem.util.MessageEnum;

@Service
public class ItemService {
	@Autowired
	private ItemInfoMapper itemInfoMapper;

	@Autowired
	private ItemConverter itemConverter;

	@Autowired
	private CommonUtil commonUtil;

	private String LINE_SEPARATOR = System.lineSeparator();

	/**
	 * 商品情報一覧を取得する
	 * @return 商品情報一覧を返却
	 */
	public ItemListForm getItemListAll() {
		ItemListForm itemListForm = new ItemListForm();
		try {
			List<ItemInfoEntity> getFindAllInfo = itemInfoMapper.getFindAll();
			if (getFindAllInfo.isEmpty()) {
				itemListForm.setErrMsg(MessageEnum.ITEM_INFO_NOT_FOUND.getMessage());
			} else {
				List<ItemInfoForm> cvList = itemConverter.entityListToFormList(getFindAllInfo);
				itemListForm.setItemInfoFormList(cvList);
			}
		} catch (Exception e) {
			itemListForm.setErrMsg(e + MessageEnum.SYSTEM_ERROR_GET_DB_DATA.getMessage());
		}
		return itemListForm;
	}

	/**
	 * ページング機能付き、商品情報一覧を取得するメソッド
	 * @param page ページ番号（ユーザーが見ているページ）
	 * @param size （1ページに何件表示するか）
	 * @return
	 */
	public Map<String, Object> getItemListAllForPaging(int page, int size) {

		ItemListForm itemListForm = new ItemListForm();
		//取得開始位置の計算（オフセット）
		int offset = (page - 1) * size;
		List<ItemInfoEntity> itemListEntity = itemInfoMapper.getPagingInfo(size, offset);
		List<ItemInfoForm> itemList = itemConverter.entityListToFormList(itemListEntity);
		itemListForm.setItemInfoFormList(itemList);
		//全商品情報数を取得
		int totalCount = itemInfoMapper.getTotalCount();
		//総ページ数を計算
		int totalPage = totalCount / size;

		if (totalCount % size != 0) {
			++totalPage;
		}

		Map<String, Object> result = new HashMap<>();
		result.put("itemListForm", itemListForm);
		result.put("totalPage", totalPage);
		result.put("currentPage", page);

		return result;
	}

	/**
	 * 商品名称重複チェック
	 * @param requestItemName ユーザーが入力した商品の名前を格納
	 * @return  null（正常） !null（異常）
	 */
	public String checkDuplicateItemName(String requestItemName) {
		//boolResultの初期値をnullに設定
		String checkResult = null;
		try {
			ItemInfoEntity existCheck = itemInfoMapper.findByItemName(requestItemName);
			if (existCheck != null) {
				checkResult = MessageEnum.DUPLICATE_ITEM_NAME.getMessage();
			}
		} catch (Exception e) {
			checkResult = e + MessageEnum.SYSTEM_ERROR_GET_DB_DATA.getMessage();
		}
		return checkResult;
	}

	/**
	 * 商品詳細情報を取得する
	 * @param itemId 商品id
	 * @return 商品idに紐づく商品詳細フォームを返却
	 */
	public ItemInfoForm getItemDetail(int itemId) {
		ItemInfoForm itemInfoForm = new ItemInfoForm();
		//商品情報が存在しない場合のEnumメッセージを出力
		try {
			ItemInfoEntity itemInfoEntity = itemInfoMapper.findByItemId(itemId);
			if (itemInfoEntity == null) {
				itemInfoForm.setErrMsg(MessageEnum.ITEM_INFO_NOT_FOUND.getMessage());
			} else {
				itemInfoForm = itemConverter.entityToForm(itemInfoEntity);
			}
		} catch (Exception e) {
			itemInfoForm.setErrMsg(e + MessageEnum.SYSTEM_ERROR_GET_DB_DATA.getMessage());
		}
		return itemInfoForm;
	}

	/**
	 * 在庫詳細情報を取得する
	 * @param itemId 商品id
	 * @return 商品idに紐づく在庫情報フォームを返却
	 */
	public ItemStockForm getItemStock(int itemId) {
		ItemStockForm itemStockForm = new ItemStockForm();
		//在庫情報が存在しない場合のEnumメッセージを出力
		try {
			ItemStockEntity itemStockEntity = itemInfoMapper.findStockByItemId(itemId);
			if (itemStockEntity == null) {
				itemStockForm.setErrMsg(MessageEnum.STOCK_INFO_NOT_FOUND.getMessage());
			} else {
				itemStockForm = itemConverter.entityToForm(itemStockEntity);
			}
		} catch (Exception e) {
			itemStockForm.setErrMsg(e + MessageEnum.SYSTEM_ERROR_GET_DB_DATA.getMessage());
		}
		return itemStockForm;
	}

	/**
	 * 更新の可否確認をする
	 * @param beforeForm 既存のフォーム
	 * @param afterForm 新しいフォーム
	 * @return null（正常） !null（異常）
	 */
	public String updateCheck(ItemStockForm beforeForm, ItemStockForm afterForm) {
		String errorMsg = null;
		//更新箇所が無い場合のエラーメッセージ
		//以下、先に必要な材料を変数に詰めておく
		int beforeStockQuantity = Integer.parseInt(beforeForm.getStockQuantity());
		int afterReceiveQuantity = Integer.parseInt(afterForm.getReceiveQuantity());
		int afterFormDispatchQuantity = Integer.parseInt(afterForm.getDispatchQuantity());
		//最新の在庫数
		int latestStockQuantity = beforeStockQuantity + afterReceiveQuantity - afterFormDispatchQuantity;
		//（最新の在庫数が以前の在庫数と同じ、または最新の在庫数が0）かつ倉庫場所が同じ
		if ((latestStockQuantity == beforeStockQuantity || latestStockQuantity == 0)
				&& beforeForm.getStorageLocation().equals(afterForm.getStorageLocation())) {
			errorMsg = MessageEnum.STOCK_NOUPDATE_INFO.getMessage();
		} else if (latestStockQuantity < 0) {
			errorMsg = MessageEnum.STOCK_QUANTITY_SHORTAGE.getMessage();
		}

		return errorMsg;
	}

	/**
	 * 商品情報と在庫情報の更新
	 * @param requestForm 新規入力したフォーム
	 * @param beforeForm 既存のフォーム
	 * @param loginUser 固定値
	 * @return 更新された新しい商品情報リストを取得し返却
	 */
	public ItemListForm updateItemStock(ItemStockForm requestForm, ItemStockForm beforeForm, String loginUser) {
		ItemListForm itemListForm = new ItemListForm();
		String errMsg = null;
		String sysMsg = null;
		try {
			//formをentityに変換
			ItemStockEntity itemStockEntity = itemConverter.formToEntity(requestForm, beforeForm, loginUser);
			//商品情報の更新（更新成功 = 1・失敗 = 0）
			int updateItemInfo = itemInfoMapper.updateItemInfo(itemStockEntity);
			int updateCount = itemInfoMapper.updateStock(itemStockEntity);

			if (updateItemInfo == 1 && updateCount == 1) {
				sysMsg = MessageEnum.SUCCESS_STOCK_DATA_BASE.getMessage();
			} else {
				errMsg = MessageEnum.FAILED_STOCK_DATABASE.getMessage();
			}
			//商品一覧情報を取得
			itemListForm = this.getItemListAll();

			if (itemListForm.getErrMsg() != null && errMsg != null) {
				errMsg = errMsg + LINE_SEPARATOR + itemListForm.getErrMsg();
			} else if (itemListForm.getErrMsg() != null) {
				errMsg = itemListForm.getErrMsg();
			}
			itemListForm.setErrMsg(errMsg);
			itemListForm.setSysMsg(sysMsg);

		} catch (Exception e) {
			itemListForm.setErrMsg(e + MessageEnum.SYSTEM_ERROR_GET_DB_DATA.getMessage());
		}
		return itemListForm;
	}

	/**
	 * 商品情報登録フォーム
	 * @param requestForm 新規入力したフォーム
	 * @param loginUser 固定値
	 * @return 新規登録したフォームを積んだ商品情報リストを返却
	 */
	public ItemListForm createItem(ItemInfoForm requestForm, String loginUser) {
		//登録ができた場合とできなかった場合のエラー処理
		ItemListForm itemListForm = new ItemListForm();

		String errMsg = null;
		String sysMsg = null;

		try {
			//商品情報を登録する
			//このメソッドに入ってきたユーザーが入力したフォームをentityに変換
			ItemInfoEntity itemInfoEntity = itemConverter.insertFormToEntity(requestForm, loginUser);
			int updateCountInfo = itemInfoMapper.insert(itemInfoEntity);
			//商品登録後、在庫情報の登録
			//さっき登録した商品情報の「itemName」を使って商品情報を取得※itemIdは自動採番のため使えない
			ItemInfoEntity newItemInfoEntity = itemInfoMapper.findByItemName(requestForm.getItemName());
			ItemStockEntity itemStockEntity = new ItemStockEntity();

			Date today = commonUtil.nowDate();

			itemStockEntity.setItemId(newItemInfoEntity.getItemId());
			itemStockEntity.setStockQuantity(0);
			itemStockEntity.setReceiveQuantity(0);
			itemStockEntity.setDispatchQuantity(0);
			itemStockEntity.setStorageLocation(newItemInfoEntity.getStorageLocation());
			itemStockEntity.setCreateUser(loginUser);
			itemStockEntity.setCreateDate(today);
			itemStockEntity.setUpdateUser(loginUser);
			itemStockEntity.setUpdateDate(today);
			itemStockEntity.setLogicalDeleteFlg(false);
			itemStockEntity.setVersion(0);

			int updateCountStock = itemInfoMapper.stockInsert(itemStockEntity);

			if (updateCountInfo == 1 && updateCountStock == 1) {
				//商品情報を登録できた場合のシステムメッセージ
				sysMsg = MessageEnum.SUCCESS_ITEM_REGISTER.getMessage();
			} else {
				errMsg = MessageEnum.FAILED_ITEM_DATABASE.getMessage();
			}
			//商品一覧情報を取得
			itemListForm = this.getItemListAll();

			if (itemListForm.getErrMsg() != null && errMsg != null) {
				errMsg = errMsg + LINE_SEPARATOR + itemListForm.getErrMsg();
			} else if (itemListForm.getErrMsg() != null) {
				errMsg = itemListForm.getErrMsg();
			}
			itemListForm.setErrMsg(errMsg);
			itemListForm.setSysMsg(sysMsg);

		} catch (Exception e) {
			itemListForm.setErrMsg(e + MessageEnum.SYSTEM_ERROR_GET_DB_DATA.getMessage());
		}
		return itemListForm;
	}

	/**
	 * 商品情報を削除する
	 * @param itemInfoForm 消去する商品情報
	 * @return itemListForm 消去後の全リストを取得し返却
	 */
	public ItemListForm deleteItem(ItemInfoForm itemInfoForm) {
		//itemInfoMapperに「任意のitemIdに紐づく商品情報・在庫情報削除メソッド」を設定
		//正常に削除されたらdeletedItemIdに「1」が格納される
		//削除できた場合とできなかった場合のエラー処理
		ItemListForm itemListForm = new ItemListForm();
		String errMsg = null;
		String sysMsg = null;
		try {
			int deletedItemId = itemInfoMapper.delete(itemInfoForm.getItemId());
			int deletedStock = itemInfoMapper.deleteStock(itemInfoForm.getItemId());

			if (deletedItemId == 1 && deletedStock == 1) {
				sysMsg = MessageEnum.SUCCESS_DELETE_ITEM.getMessage();
			} else {
				errMsg = MessageEnum.FAILED_DELETE_ITEM.getMessage();
			}
			//商品一覧情報を取得
			itemListForm = this.getItemListAll();

			//商品一覧でエラーが出た場合のエラー文
			if (itemListForm.getErrMsg() != null && errMsg != null) {
				errMsg = errMsg + LINE_SEPARATOR + itemListForm.getErrMsg();
			} else if (itemListForm.getErrMsg() != null) {
				errMsg = itemListForm.getErrMsg();
			}
			itemListForm.setErrMsg(errMsg);
			itemListForm.setSysMsg(sysMsg);
		} catch (Exception e) {
			//その他のシステムエラー
			itemListForm.setErrMsg(MessageEnum.SYSTEM_ERROR_GET_DB_DATA.getMessage());
		}
		return itemListForm;
	}

}
