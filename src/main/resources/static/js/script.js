function returnConfirm(){
	link = "Yahoo!Japan";
	href = "http://localhost:8080/product-list";
	ret = confirm("入力内容は保存されません。前画面に戻りますか？");
	if (ret == true){
		location.href = href;
  }
}