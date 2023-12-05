package com.example.market.Chat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.market.ActivityNavigation
import com.example.market.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FragmentSend : Fragment() {

    // Firebase 인증(Authentication) 인스턴스
    private lateinit var auth: FirebaseAuth
    // Firebase Firestore 인스턴스
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 이 프래그먼트의 레이아웃을 확장(inflate)합니다.
        val view = inflater.inflate(R.layout.sen_fragment, container, false)

        // Firebase 인스턴스 초기화
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // 각 뷰들을 ID를 통해 찾습니다.
        val emailInput = view.findViewById<EditText>(R.id.recipientInput)
       
        val sendingButton = view.findViewById<Button>(R.id.sendButton)

        val messageInput = view.findViewById<EditText>(R.id.messageInput)

        // Arguments로부터 판매자 이메일을 가져와 설정합니다.
        val sellerEmail = arguments?.getString("sellerEmail")
        emailInput.setText(sellerEmail)

        // 현재 사용자의 이메일을 가져옵니다.
        val userEmail = auth.currentUser?.email
        if (userEmail == null) {
            return view
        }

        // 전송 버튼에 클릭 리스너를 추가합니다.
        sendingButton.setOnClickListener {
            val recipient = emailInput.text.toString()
            val message = messageInput.text.toString()

            // Firestore에서 사용자 정보를 가져옵니다.
            db.collection("users").document(userEmail)
                .get()
                .addOnSuccessListener { document ->
                    val purchase: String? = document.getString("name")

                    purchase?.let {
                        val newMessage = hashMapOf(
                            "purchase" to it,
                            "message" to message,
                            "seller" to recipient
                        )

                        if (it.isEmpty() || message.isEmpty() || recipient.isEmpty()) {
                            Toast.makeText(context, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                            return@addOnSuccessListener
                        }

                        // 채팅 컬렉션에 새로운 메시지를 추가합니다.
                        db.collection("chat")
                            .add(newMessage)
                            .addOnSuccessListener {
                                // BottomNavigationActivity로 이동하는 인텐트를 생성하고 실행합니다.
                                val intent = Intent(context, ActivityNavigation::class.java)
                                startActivity(intent)
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    context,
                                    "메시지 전송 실패: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } ?: run {
                        Toast.makeText(context, "판매자 정보 불러오기 실패", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "사용자 정보 불러오기 실패", Toast.LENGTH_SHORT).show()
                }
        }

        return view
    }
}
