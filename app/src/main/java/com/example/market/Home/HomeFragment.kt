package com.example.market.Home

import android.app.AlertDialog
import android.os.Bundle
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
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(context)

        // 필터 버튼 클릭 시 동작 정의
        val filterButton = view.findViewById<Button>(R.id.filterButton)
        filterButton.setOnClickListener {
            // 필터링 옵션 Dialog 표시
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Filter")
                .setSingleChoiceItems(arrayOf("⚪️ 모든 상품", "🟢 판매 중", "🔴 판매 완료"), -1) { dialog, which ->
                    when (which) {
                        // 각 버튼에 따라 해당하는 상품 로드
                        0 -> loadProducts(null) // 모든 상품 로드
                        1 -> loadProducts(true) // 판매 중인 상품 로드
                        2 -> loadProducts(false) // 판매 완료된 상품 로드
                    }
                    dialog.dismiss()
                }
                .create()
            dialog.show()
        }

        // 로그아웃 버튼 클릭 시
        val logoutButton = view.findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            // Firebase에서 로그아웃
            auth.signOut()
            // 로그인 화면으로 이동
            navController?.navigate(R.id.action_homeFragment_to_loginFragment)
        }

        // Firestore 및 Storage 인스턴스 초기화
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance("gs://kkkk-82d4d.appspot.com")

        // 글쓰기 화면으로 이동하는 FAB 버튼 클릭 시
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            navController?.navigate(R.id.postFragment)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // NavController 초기화
        navController = findNavController()
        // 모든 상품 로드
        loadProducts(null)
    }

    // 상품을 로드하는 메소드
    private fun loadProducts(isAvailable: Boolean?) {
        val productList = mutableListOf<Product>() // 상품 리스트 생성
        adapter = ProductAdapter(requireContext(), productList, navController, false) // 어댑터 생성
        recyclerView.adapter = adapter // 리사이클러뷰에 어댑터 설정

        auth = FirebaseAuth.getInstance()

        // 상품의 판매 여부에 따라 Firestore 쿼리 생성
        val products = if (isAvailable == null) {
            db.collection("posts") // 판매 여부에 관계없이 모든 상품 로드
        } else {
            db.collection("posts").whereEqualTo("sell", isAvailable) // 판매 여부에 맞는 상품 로드
        }

        // Firestore에서 데이터 가져오기
        products.get().addOnSuccessListener { result ->
            for (document in result) {
                val product = document.toObject(Product::class.java) // 문서를 Product 객체로 변환
                productList.add(product) // 상품 리스트에 추가
            }
            adapter.notifyDataSetChanged() // 어댑터에 데이터 변경 알림
        }
    }
}
