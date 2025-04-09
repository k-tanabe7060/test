
//APIなしバージョン
//csbBtnを取得・clickイベントでfunction発火
document.getElementById("csvBtn").addEventListener("click", function() {
	const table = document.getElementById("itemListForCsv").querySelector("table");	//id=itemListForCsv要素内のtableを取得
	let csvContent = "\uFEFF";	//文字化け防止でcsvContentの先頭にBOMを付与

	const rows = table.rows;//trの一覧を取得
	let excludeIndex = -1; //在庫の列を取得しない処理で使う用

	for (let i = 0; i < rows.length; i++) {
		let rowData = []; //一行分のデータを入れる用の配列
		const cells = rows[i].cells; //その行のセル（tdとth）を取得

		for (let j = 0; j < cells.length; j++) {
			//0行目（ヘッダ行）で「在庫」という文字があったら、その列はスキップ
			if (i === 0 && cells[j].innerText.trim() === "在庫") {
				excludeIndex = j;
				continue;
			}

			// 「在庫」の列に当たる場合はスキップ
			if (j === excludeIndex) {
				continue;
			}
			rowData.push(cells[j].innerText.replace(/\n/g, "").trim()); //セル内の文字を取得→改行を削除→余計なスペースを消す
		}
		csvContent += rowData.join(",") + "\n"; //「,」でつないで改行を入れる
	}

	const blob = new Blob([csvContent], { type: "text/csv" }); //csvをblobに変換・MIMEタイプを指定
	const url = URL.createObjectURL(blob); //ダウンロード用URLを生成
	this.href = url; //hrefにさっきのURLを指定
	this.download = "itemList.csv"; //ファイル名
    setTimeout(() => URL.revokeObjectURL(url), 1000); //URLを削除（若干処理遅らせてる）
});
