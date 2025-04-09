package com.freeks.training.stockSystem.service;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.freeks.training.stockSystem.converter.ItemConverter;
import com.freeks.training.stockSystem.entity.ItemInfoEntity;
import com.freeks.training.stockSystem.entity.ItemStockEntity;
import com.freeks.training.stockSystem.form.ItemInfoForm;
import com.freeks.training.stockSystem.form.ItemListForm;
import com.freeks.training.stockSystem.mapper.ItemInfoMapper;
import com.freeks.training.stockSystem.util.CommonUtil;

class ItemServiceTest {

	//日付フォーマット
	private SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");

	// テストデータ
	private int itemId = 9990;
	private int itemId2 = 9991;
	private String itemName = "testName1";
	private String itemName2 = "testName2";
	private String itemKubun = "test1";
	private String itemKubun2 = "test2";
	private String maker = "テストメーカー1";
	private String maker2 = "テストメーカー2";
	private String jancd = "test1234";
	private String jancd2 = "test5678";
	private int purchaseUnitPrice = 1234;
	private int purchaseUnitPrice2 = 5678;
	private int salesUnitPrice = 5678;
	private int salesUnitPrice2 = 1234;
	private String storageLocation = "テスト保管場所";
	private String storageLocation2 = "テスト保管場所2";
	private Date receiptDate = nowDate();
	private String createUser = "createTest";
	private Date createDate = nowDate();
	private String updateUser = "updateTest";
	private Date updateDate = nowDate();
	private boolean logicalDeleteFlg = false;
	private int version = 999;
	

	@Mock
	ItemInfoMapper itemInfoMapper;

	@Mock
	ItemConverter itemConverter;

	@Mock
	CommonUtil commonUtil;

	@InjectMocks
	@Spy
	ItemService itemService;

	/**
	 * @Mockを付与したメンバ変数にモックオブジェクトを注入
	 * @Testを付与したメソッドの実行前に都度実行される
	 */
	@BeforeEach
	public void initMocks() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * getItemListAll
	 * 項番1
	 * 正常系
	 * 
	 */
	@Test
	void getItemListAll_1() {

		// Mock戻り値の設定
		List<ItemInfoEntity> entList = createItemInfoEntityList();
		List<ItemInfoForm> formList = entityListToFormList(entList);

		// Mockの設定
		when(itemInfoMapper.getFindAll()).thenReturn(entList);
		when(itemConverter.entityListToFormList(entList)).thenReturn(formList);

		// 対象メソッド実行
		ItemListForm result = itemService.getItemListAll();

		// 結果確認
		assertThat(result.getItemInfoFormList().size(), is(2));
		assertThat(result.getErrMsg(), is(nullValue()));
		assertThat(result.getItemInfoFormList().get(0).getItemId(), is(itemId));
		assertThat(result.getItemInfoFormList().get(0).getItemName(), is(itemName));
		assertThat(result.getItemInfoFormList().get(0).getItemKubun(), is(itemKubun));
		assertThat(result.getItemInfoFormList().get(0).getMaker(), is(maker));
		assertThat(result.getItemInfoFormList().get(0).getJancd(), is(jancd));
		assertThat(result.getItemInfoFormList().get(0).getPurchaseUnitPrice(), is(String.valueOf(purchaseUnitPrice)));
		assertThat(result.getItemInfoFormList().get(0).getSalesUnitPrice(), is(String.valueOf(salesUnitPrice)));
		assertThat(result.getItemInfoFormList().get(0).getStorageLocation(), is(storageLocation));
		assertThat(result.getItemInfoFormList().get(1).getItemId(), is(itemId2));
		assertThat(result.getItemInfoFormList().get(1).getItemName(), is(itemName2));
		assertThat(result.getItemInfoFormList().get(1).getItemKubun(), is(itemKubun2));
		assertThat(result.getItemInfoFormList().get(1).getMaker(), is(maker2));
		assertThat(result.getItemInfoFormList().get(1).getJancd(), is(jancd2));
		assertThat(result.getItemInfoFormList().get(1).getPurchaseUnitPrice(), is(String.valueOf(purchaseUnitPrice2)));
		assertThat(result.getItemInfoFormList().get(1).getSalesUnitPrice(), is(String.valueOf(salesUnitPrice2)));
		assertThat(result.getItemInfoFormList().get(1).getStorageLocation(), is(storageLocation2));

	}

	/**
	 * getItemListAll
	 * 項番2
	 * List<ItemInfoEntity>のSizeが0
	 * 正常系
	 * 
	 */
	@Test
	void getItemListAll_2() {

		// Mock戻り値の設定
		List<ItemInfoEntity> entList = new ArrayList<>();

		// Mockの設定
		when(itemInfoMapper.getFindAll()).thenReturn(entList);

		// 対象メソッド実行
		ItemListForm result = itemService.getItemListAll();

		// 結果確認
		assertThat(result.getItemInfoFormList(), is(nullValue()));
		assertThat(result.getErrMsg(), is("システムエラー：商品情報が見つかりません"));

	}

	/**
	 * getItemListAll
	 * 項番3
	 * itemInfoMapper.getFindAll()で予期せぬエラー
	 * 異常系
	 * 
	 */
	@Test
	void getItemListAll_3() {

		// Mockの設定
		when(itemInfoMapper.getFindAll()).thenThrow(new RuntimeException());

		// 対象メソッド実行
		ItemListForm result = itemService.getItemListAll();
		System.out.println(result.getErrMsg());
		// 結果確認
		assertThat(result.getItemInfoFormList(), is(nullValue()));
		assertThat(result.getErrMsg(), is("java.lang.RuntimeException" + "システムエラー：DB接続に失敗しました"));

	}

	/**
	 * List<ItemInfoEntity>テストデータ作成メソッド
	 * 
	 * @return List<ItemInfoEntity>
	 */
	public List<ItemInfoEntity> createItemInfoEntityList() {

		List<ItemInfoEntity> list = new ArrayList<>();
		ItemInfoEntity ent1 = new ItemInfoEntity();
		ItemInfoEntity ent2 = new ItemInfoEntity();

		ent1.setItemId(itemId);
		ent1.setItemName(itemName);
		ent1.setItemKubun(itemKubun);
		ent1.setMaker(maker);
		ent1.setJancd(jancd);
		ent1.setPurchaseUnitPrice(purchaseUnitPrice);
		ent1.setSalesUnitPrice(salesUnitPrice);
		ent1.setStorageLocation(storageLocation);
		ent1.setReceiptDate(receiptDate);
		ent1.setCreateUser(createUser);
		ent1.setCreateDate(createDate);
		ent1.setUpdateUser(updateUser);
		ent1.setUpdateDate(updateDate);
		ent1.setLogicalDeleteFlg(logicalDeleteFlg);
		ent1.setVersion(version);

		ent2.setItemId(itemId2);
		ent2.setItemName(itemName2);
		ent2.setItemKubun(itemKubun2);
		ent2.setMaker(maker2);
		ent2.setJancd(jancd2);
		ent2.setPurchaseUnitPrice(purchaseUnitPrice2);
		ent2.setSalesUnitPrice(salesUnitPrice2);
		ent2.setStorageLocation(storageLocation2);
		ent2.setReceiptDate(receiptDate);
		ent2.setCreateUser(createUser);
		ent2.setCreateDate(createDate);
		ent2.setUpdateUser(updateUser);
		ent2.setUpdateDate(updateDate);
		ent2.setLogicalDeleteFlg(logicalDeleteFlg);
		ent2.setVersion(version);

		list.add(ent1);
		list.add(ent2);

		return list;

	}

	/**
	 * entityリストをformリストに変換
	 * @param entityList
	 * @return
	 */
	public List<ItemInfoForm> entityListToFormList(List<ItemInfoEntity> entityList) {

		List<ItemInfoForm> formList = new ArrayList<>();

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
			form.setReceiptDate(convertDateToStrDate((entity.getReceiptDate())));
			form.setCreateUser(entity.getCreateUser());
			form.setCreateDate(convertDateToStrDate((entity.getCreateDate())));
			form.setUpdateUser(entity.getUpdateUser());
			form.setUpdateDate(convertDateToStrDate((entity.getUpdateDate())));

			formList.add(form);
		}
		return formList;

	}

	/**
	 * 現在の日付を生成
	 * @return
	 */
	public Date nowDate() {
		Date nowDate = new Date();
		return nowDate;
	}

	/**
	 * Date型をString型に変換
	 * @param date
	 * @return
	 */
	public String convertDateToStrDate(Date date) {
		// nullチェック
		if (date == null) {
			return null;
		}
		// フォーマットに成型
		String strDate = sdFormat.format(date);
		return strDate;
	}

	/**
	 * checkDuplicateItemName
	 * 項番4
	 * 正常系
	 */
	@Test
	void checkDuplicateItemName_1() {
		//Mockの設定
		when(itemInfoMapper.findByItemName(itemName)).thenReturn(null);
		//対象メソッドを実行
		String result = itemService.checkDuplicateItemName(itemName);
		System.out.println(result);
		//結果確認
		assertThat(result, is(nullValue()));
	}

	/**
	 * checkDuplicateItemName
	 * 項番5
	 * itemInfoEntity != nullの場合
	 * 異常系
	 */
	@Test
	void checkDuplicateItemName_2() {
		//Mock戻り値を設定

		ItemInfoEntity existName = new ItemInfoEntity();
		//Mockの設定
		when(itemInfoMapper.findByItemName(itemName)).thenReturn(existName);
		//対象メソッドを実行
		String result = itemService.checkDuplicateItemName(itemName);
		System.out.println(result);
		//結果確認
		assertThat(result, is("商品名称が重複しています"));
	}

	/**
	 * checkDuplicateItemName
	 * 項番6
	 * itemInfoMapper.findByItemNameで予期せぬエラー
	 * 異常系
	 */
	@Test
	void checkDuplicateItemName_3() {
		// Mockの設定
		when(itemInfoMapper.findByItemName(itemName)).thenThrow(new RuntimeException());

		// 対象メソッド実行
		String result = itemService.checkDuplicateItemName(itemName);
		System.out.println(result);

		// 結果確認
		assertThat(result, is("java.lang.RuntimeException" + "システムエラー：DB接続に失敗しました"));
	}
	
	/**
	 * createItem
	 * 項番7
	 * updateCountInfoとupdateCountStockの両方が1の場合
	 * （商品情報の登録、在庫情報の登録、商品リストの取得が成功）
	 * 正常系
	 */
	@Test
	void createItem_1() {

		//createItemメソッドの引数（定数）
		String loginUser = createUser;

		// Mock戻り値の設定（商品情報と在庫情報）
		ItemInfoForm requestForm = createItemInfoForm();
		ItemInfoEntity itemInfoEntity = formToEntity(requestForm);
		ItemInfoEntity newItemInfoEntity = findByItemName(requestForm.getItemName());
		int itemCountInfo = 1;
		int itemCountStock = 1;

		// Mock戻り値の設定（getItemListAll()内）
		List<ItemInfoEntity> entList = createItemInfoEntityList();
		List<ItemInfoForm> formList = entityListToFormList(entList);

		// Mockの設定・商品情報の登録と在庫情報の登録は成功
		when(itemConverter.insertFormToEntity(requestForm, loginUser)).thenReturn(itemInfoEntity);
		when(itemInfoMapper.insert(itemInfoEntity)).thenReturn(itemCountInfo);
		when(itemInfoMapper.findByItemName(requestForm.getItemName())).thenReturn(newItemInfoEntity);
		when(itemInfoMapper.getFindAll()).thenReturn(entList);
		when(itemConverter.entityListToFormList(entList)).thenReturn(formList);
		when(itemInfoMapper.stockInsert(any(ItemStockEntity.class))).thenReturn(itemCountStock);

		// 対象メソッド実行
		ItemListForm result = itemService.createItem(requestForm, loginUser);

		//メソッド呼び出しの検証
		verify(itemService, times(1)).getItemListAll();

		// 結果確認
		assertThat(result.getErrMsg(), is(nullValue()));
		assertThat(result.getSysMsg(), is("商品情報を登録しました"));
	}

	/**
	 * createItem
	 * 項番8
	 * itemConverter.insertFormToEntityで予期せぬエラー
	 * 異常系
	 */
	@Test
	void createItem_2() {
		
		//createItemメソッドの引数（定数）
		String loginUser = createUser;
		
		// Mock戻り値の設定（商品情報と在庫情報）
		ItemInfoForm requestForm = createItemInfoForm();
		
		//Mockの設定
		when(itemConverter.insertFormToEntity(requestForm, loginUser)).thenThrow(new RuntimeException());

		//対象メソッドを実行
		ItemListForm result = itemService.createItem(requestForm, loginUser);
		
		//結果確認
		assertThat(result.getErrMsg(), is("java.lang.RuntimeException" + "システムエラー：DB接続に失敗しました"));

	}

	/**
	 * createItem
	 * 項番9
	 * itemInfoMapper.insertで予期せぬエラー
	 * 異常系
	 */
	@Test
	void createItem_3() {

		//createItemメソッドの引数（定数）
		String loginUser = createUser;

		// Mock戻り値の設定
		ItemInfoForm requestForm = createItemInfoForm();
		ItemInfoEntity itemInfoEntity = formToEntity(requestForm);

		//Mockの設定
		when(itemConverter.insertFormToEntity(requestForm, loginUser)).thenReturn(itemInfoEntity);
		when(itemInfoMapper.insert(itemInfoEntity)).thenThrow(new RuntimeException());
		
		//対象メソッドを実行
		ItemListForm result = itemService.createItem(requestForm, loginUser);
		
		//結果確認
		assertThat(result.getErrMsg(), is("java.lang.RuntimeException" + "システムエラー：DB接続に失敗しました"));
	}

	/**
	 * createItem
	 * 項番10
	 * itemInfoMapper.findByItemName()で予期せぬエラー
	 * 異常系
	 */
	@Test
	void createItem_4() {
		
		//createItemメソッドの引数（定数）
		String loginUser = createUser;
		
		// Mock戻り値の設定
		ItemInfoForm requestForm = createItemInfoForm();
		ItemInfoEntity itemInfoEntity = formToEntity(requestForm);
		int updateCountInfo = 1;
		
		//Mockを設定
		when(itemConverter.insertFormToEntity(requestForm, loginUser)).thenReturn(itemInfoEntity);
		when(itemInfoMapper.insert(itemInfoEntity)).thenReturn(updateCountInfo);
		when(itemInfoMapper.findByItemName(itemName)).thenThrow(new RuntimeException());
		
		//対象メソッドを実行
		ItemListForm result = itemService.createItem(requestForm, loginUser);

		//結果確認
		assertThat(result.getErrMsg(), is("java.lang.RuntimeException" + "システムエラー：DB接続に失敗しました"));
	}

	/**
	 * createItem
	 * 項番11
	 * itemInfoMapper.stockInsert()で予期せぬエラー
	 * 異常系
	 */
	@Test
	void createItem_5() {
		
		//createItemメソッドの引数（定数）
		String loginUser = createUser;

		// Mock戻り値の設定（商品情報と在庫情報）
		ItemInfoForm requestForm = createItemInfoForm();
		ItemInfoEntity itemInfoEntity = formToEntity(requestForm);
		ItemInfoEntity newItemInfoEntity = findByItemName(requestForm.getItemName());
		int itemCountInfo = 1;

		// Mock戻り値の設定（getItemListAll()内）
		List<ItemInfoEntity> entList = createItemInfoEntityList();
		List<ItemInfoForm> formList = entityListToFormList(entList);

		// Mockの設定・商品情報の登録と在庫情報の登録は成功
		when(itemConverter.insertFormToEntity(requestForm, loginUser)).thenReturn(itemInfoEntity);
		when(itemInfoMapper.insert(itemInfoEntity)).thenReturn(itemCountInfo);
		when(itemInfoMapper.findByItemName(requestForm.getItemName())).thenReturn(newItemInfoEntity);
		when(itemInfoMapper.getFindAll()).thenReturn(entList);
		when(itemConverter.entityListToFormList(entList)).thenReturn(formList);
		when(itemInfoMapper.stockInsert(any(ItemStockEntity.class))).thenThrow(new RuntimeException());

		// 対象メソッド実行
		ItemListForm result = itemService.createItem(requestForm, loginUser);

		//結果確認
		assertThat(result.getErrMsg(), is("java.lang.RuntimeException" + "システムエラー：DB接続に失敗しました"));
	}

	/**
	 * createItem
	 * 項番12
	 * updateCountInfoとupdateCountStockのどちらか一方が1ではない、かつ、itemListForm.getItemListForm()のerrMsgがnullでない場合
	 * 異常系
	 */
	@Test
	void createItem_6() {

		//createItemメソッドの引数（定数）
		String loginUser = createUser;

		// Mock戻り値の設定（商品情報と在庫情報）
		ItemInfoForm requestForm = createItemInfoForm();
		ItemInfoEntity itemInfoEntity = formToEntity(requestForm);
		ItemInfoEntity newItemInfoEntity = findByItemName(requestForm.getItemName());
		int updateCountInfo = 1;
		int updateCountStock = 0;

		// Mock戻り値の設定（getItemListAll()内）
		List<ItemInfoEntity> entList =  new ArrayList<>();
		List<ItemInfoForm> formList = entityListToFormList(entList);

		// Mockの設定・商品情報の登録と在庫情報の登録は成功
		when(itemConverter.insertFormToEntity(requestForm, loginUser)).thenReturn(itemInfoEntity);
		when(itemInfoMapper.insert(itemInfoEntity)).thenReturn(updateCountInfo);
		when(itemInfoMapper.findByItemName(requestForm.getItemName())).thenReturn(newItemInfoEntity);
		when(itemInfoMapper.getFindAll()).thenReturn(entList);
		when(itemConverter.entityListToFormList(entList)).thenReturn(formList);
		when(itemInfoMapper.stockInsert(any(ItemStockEntity.class))).thenReturn(updateCountStock);

		// 対象メソッド実行
		ItemListForm result = itemService.createItem(requestForm, loginUser);
		
		//メソッド呼び出しの検証
		verify(itemService, times(1)).getItemListAll();

		//結果確認
		assertThat(result.getErrMsg(), is("商品情報の更新に失敗しました" + System.lineSeparator() + "システムエラー：商品情報が見つかりません"));
	}

	/**
	 * createItem
	 * 項番13
	 * updateCountInfo == 1 && updateCountStock == 1（商品情報&在庫情報の登録が成功）だが、商品情報の取得に失敗（getItemListAllでErrMsgが格納されている）
	 * 異常系
	 */
	@Test
	void createItem_7() {

		//createItemメソッドの引数（定数）
		String loginUser = createUser;

		// Mock戻り値の設定（商品情報と在庫情報）
		ItemInfoForm requestForm = createItemInfoForm();
		ItemInfoEntity itemInfoEntity = formToEntity(requestForm);
		ItemInfoEntity newItemInfoEntity = findByItemName(requestForm.getItemName());
		int updateCountInfo = 1;
		int updateCountStock = 1;

		// Mock戻り値の設定（getItemListAll()内）
		List<ItemInfoEntity> entList =  new ArrayList<>();
		List<ItemInfoForm> formList = entityListToFormList(entList);

		// Mockの設定・商品情報の登録と在庫情報の登録は成功
		when(itemConverter.insertFormToEntity(requestForm, loginUser)).thenReturn(itemInfoEntity);
		when(itemInfoMapper.insert(itemInfoEntity)).thenReturn(updateCountInfo);
		when(itemInfoMapper.findByItemName(requestForm.getItemName())).thenReturn(newItemInfoEntity);
		when(itemInfoMapper.getFindAll()).thenReturn(entList);
		when(itemConverter.entityListToFormList(entList)).thenReturn(formList);
		when(itemInfoMapper.stockInsert(any(ItemStockEntity.class))).thenReturn(updateCountStock);

		// 対象メソッド実行
		ItemListForm result = itemService.createItem(requestForm, loginUser);
		
		//メソッド呼び出しの検証
		verify(itemService, times(1)).getItemListAll();
		
		System.out.println(result.getErrMsg());

		//結果確認
		assertThat(result.getErrMsg(), is("システムエラー：商品情報が見つかりません"));
	}

	/**
	 * createItemメソッドで使用するItemInfoFormのテストデータ作成用メソッド
	 * @return itemInfoForm
	 */
	public ItemInfoForm createItemInfoForm() {
		ItemInfoForm form = new ItemInfoForm();

		form.setItemId(itemId);
		form.setItemName(itemName);
		form.setItemKubun(itemKubun);
		form.setMaker(maker);
		form.setJancd(jancd);
		form.setPurchaseUnitPrice(String.valueOf(purchaseUnitPrice));
		form.setSalesUnitPrice(String.valueOf(salesUnitPrice));
		form.setStorageLocation(storageLocation);
		form.setReceiptDate(convertDateToStrDate(receiptDate));
		form.setCreateDate(convertDateToStrDate(createDate));
		form.setCreateUser(createUser);
		form.setUpdateDate(convertDateToStrDate(updateDate));
		form.setUpdateUser(updateUser);

		return form;
	}

	/**
	 * createItemメソッドで使用するItemInfoFormをItemInfoEntityに変換するコンバータ
	 * @param form ItemInfoForm型のデータ
	 * @return itemInfoEntity型のデータを返却
	 */
	public ItemInfoEntity formToEntity(ItemInfoForm form) {
		ItemInfoEntity itemInfoEntity = new ItemInfoEntity();
		itemInfoEntity.setItemId(form.getItemId());
		itemInfoEntity.setItemName(itemName);
		itemInfoEntity.setItemKubun(itemKubun);
		itemInfoEntity.setMaker(maker);
		itemInfoEntity.setJancd(jancd);
		itemInfoEntity.setPurchaseUnitPrice(Integer.parseInt(form.getPurchaseUnitPrice()));
		itemInfoEntity.setSalesUnitPrice(Integer.parseInt(form.getSalesUnitPrice()));
		itemInfoEntity.setStorageLocation(storageLocation);
		itemInfoEntity.setReceiptDate(receiptDate);
		itemInfoEntity.setCreateUser(createUser);
		itemInfoEntity.setCreateDate(createDate);
		itemInfoEntity.setUpdateUser(updateUser);
		itemInfoEntity.setUpdateDate(updateDate);
		itemInfoEntity.setLogicalDeleteFlg(logicalDeleteFlg);
		itemInfoEntity.setVersion(version);
		return itemInfoEntity;
	}

	/**
	 * createItemで使用するfindByItemNameの返り値作成メソッド
	 * @param itemName
	 * @return newItemInfoEntity
	 */
	public ItemInfoEntity findByItemName(String itemName) {
		ItemInfoEntity newItemInfoEntity = new ItemInfoEntity();
		newItemInfoEntity.setCreateDate(createDate);
		newItemInfoEntity.setCreateUser(createUser);
		newItemInfoEntity.setItemId(itemId);
		newItemInfoEntity.setItemKubun(itemKubun);
		newItemInfoEntity.setItemName(itemName);
		newItemInfoEntity.setJancd(jancd);
		newItemInfoEntity.setLogicalDeleteFlg(logicalDeleteFlg);
		newItemInfoEntity.setMaker(maker);
		newItemInfoEntity.setPurchaseUnitPrice(purchaseUnitPrice);
		newItemInfoEntity.setReceiptDate(receiptDate);
		newItemInfoEntity.setSalesUnitPrice(salesUnitPrice);
		newItemInfoEntity.setStorageLocation(storageLocation);
		newItemInfoEntity.setUpdateDate(updateDate);
		newItemInfoEntity.setUpdateUser(updateUser);
		newItemInfoEntity.setVersion(version);
		return newItemInfoEntity;
	}


}