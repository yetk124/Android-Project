package com.example.market.Home

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.market.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class HomeFragment : Fragment() {
    lateinit var navController: NavController
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var adapter: ProductAdapter
    private lateinit var recyclerView : RecyclerView

    // 프래그먼트의 뷰가 생성될 때 호출되는 메소드
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view) // 여기에 수정
        //리사이클러 뷰의 레이아웃 매니저 설정.
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 필터 버튼 클릭 시 동작 정의, Dialog를 이용해 필터링을 구현했음.
        val filterButton = view.findViewById<Button>(R.id.filterButton)
        filterButton.setOnClickListener {
            // 필터링 옵션 Dialog 표시
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Filter")
                .setSingleChoiceItems(arrayOf("⚪️ 모든 상품", "🟢 판매중", "🔴 판매완료"), -1) { dialog, which ->
                    when (which) {
                        //선택한 각 버튼에 따라서 상품을 불러올 대 판매여부를 알 수 있는
                        //isAvailable 매개변수를 넣어줌.
                        0 -> loadProducts(null)
                        1 -> loadProducts(true)
                        2 -> loadProducts(false)
                    }
                    dialog.dismiss()
                }
                .create()
            dialog.show()
        }

        // 로그아웃 버튼 찾아옴
        val logoutButton = view.findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            // 로그아웃 수행
            auth.signOut()
            // 로그인 화면으로 이동
            navController?.navigate(R.id.action_homeFragment_to_loginFragment)
        }

        //호출될때 firestore와 storage가져옴.
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance("gs://kkkk-82d4d.appspot.com")

        //플로팅 액션 버튼을 통해서 글등록 화면으로 이동하는 버튼을 만듦.
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            // 글쓰기 화면으로 이동
            navController?.navigate(R.id.postFragment)
        }

        return view
    }

    //프래그먼트의 뷰가 완성된 후 호출되는 메소드이다.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //내비게이션 컨트롤러를 찾는다.
        navController = findNavController()
        //모든 상품을 로드한다. 메소드 직접구현
        loadProducts(null)
    }

    //상품 로드하는 메소드. 인자로 상품의 판매 여부를 받음.
    private fun loadProducts(isAvailable: Boolean?) {
        val productList = mutableListOf<Product>() // 상품 리스트 생성
        // 어댑터 생성
        adapter = ProductAdapter(requireContext(), productList, navController, false)
        recyclerView.adapter = adapter // 리사이클러뷰에 어댑터 설정

        auth = FirebaseAuth.getInstance()

        // 상품의 사용 가능 여부에 따라 쿼리 생성
        val products = if (isAvailable == null) {
            // 만약 isAvailable이 null 로 들어오면 모든 상품 로드
            db.collection("posts")
        } else { //null 이 아니라면 해당 isAvailable에 맞는 상품만 로드
            db.collection("posts").whereEqualTo("sell", isAvailable)
        }

        //쿼리를 통해서 데이터를 가져옴
        products.get().addOnSuccessListener { result ->
            for (document in result) {
                //문서를 Product 객체로 변환한다.
                val product = document.toObject(Product::class.java)
                //상품 리스트에 추가하고
                productList.add(product)
            }
            //어댑터에 데이터변경을 알림
            adapter.notifyDataSetChanged()
        }
    }
}

