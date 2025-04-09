async function downloadCsv() {
	try {
		const response = await fetch("/api/items/export-json");
		if (!response.ok) {
			throw new Error("データの保存に失敗しました");
		}

		const itemList = await response.json();
		let csvContent = "\uFEFF" + "商品ID,商品名称,区分,メーカー,JANコード,購入単価,販売単価,保管場所,入庫日\n";

		itemList.forEach(item => {
			csvContent += `${item.itemId},${item.itemName},${item.itemKubun},${item.maker},${item.jancd},${item.purchaseUnitPrice},${item.salesUnitPrice},${item.storageLocation},${item.receiptDate}\n`;
		});

		const blob = new Blob([csvContent], { type: "text/csv" });
		const url = window.URL.createObjectURL(blob);
		const a = document.createElement("a");
		a.href = url;
		a.download = "items.csv";
		document.body.appendChild(a);
		a.click();
		document.body.removeChild(a);
		window.URL.revokeObjectURL(url);
	} catch (error) {
		console.error(error);
		alert("CSVの取得に失敗しました");
	}
}
