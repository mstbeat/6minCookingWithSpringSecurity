function returnConfirm(btn) {
	if (btn == 0) {
		ret = confirm("商品情報を登録しますか？");
		if (ret == false) {
			return false;
		}
	}
	if (btn == 1) {
		ret = confirm("商品情報を更新しますか？");
		if (ret == false) {
			return false;
		}
	}
	if (btn == 2) {
		ret = confirm("商品情報を削除しますか？");
		if (ret == false) {
			return false;
		}
	}
	if (btn == 3) {
		href = "product-list";
		ret = confirm("入力内容は保存されません。前画面に戻りますか？");
		if (ret == true) {
			location.href = href;
		}
	}
}