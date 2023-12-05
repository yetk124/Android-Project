package com.example.market.Home



data class Products (var urlImg: String, var titleName: String, var price: Int, var id: String,
                   var seller: String?,
                   var sell: Boolean,
                   var mailSeller: String?,
                   var contents: String?
) {
// 기본 생성자: 기본값을 설정하여 객체를 초기화하는 보조 생성자

    constructor() : this("", "", 0, "", null, true, null, null)
}
