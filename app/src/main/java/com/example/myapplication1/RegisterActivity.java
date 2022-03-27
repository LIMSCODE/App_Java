package com.example.myapplication1;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication1.model.FirebaseId;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    EditText userEmailText, userPasswordText, userPasswordCheckText, userNameText, userPhoneText;

    private FirebaseAuth firebaseAuth;

    //private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    //private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //액션 바 등록하기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");

        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기버튼
        actionBar.setDisplayShowHomeEnabled(true); //홈 아이콘

        //파이어베이스 접근 설정
        // user = firebaseAuth.getCurrentUser();
        firebaseAuth =  FirebaseAuth.getInstance();
        //firebaseDatabase = FirebaseDatabase.getInstance().getReference();


        //버튼 아이디 가져오기
        userEmailText = findViewById(R.id.userEmail);
        userPasswordText = findViewById(R.id.userPassword);
        userPasswordCheckText = findViewById(R.id.userPasswordCheck);
        userNameText = findViewById(R.id.userName);
        userPhoneText = findViewById(R.id.userPhone);

        //파이어베이스 user 로 접글
        Button registerBtn = (Button) findViewById(R.id.register);

        //가입버튼 클릭리스너   -->  firebase에 데이터를 저장한다.
        registerBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                //가입 정보 가져오기
                final String Email = userEmailText.getText().toString().trim();
                String pwd = userPasswordText.getText().toString().trim();
                String pwdcheck = userPasswordCheckText.getText().toString().trim();
                String name = userNameText.getText().toString().trim();
                String phone = userPhoneText.getText().toString().trim();

                if (pwd.equals(pwdcheck)) {
                    Log.d(TAG, "등록 버튼 " + Email + " , " + pwd);
                    final ProgressDialog mDialog = new ProgressDialog(RegisterActivity.this);
                    mDialog.setMessage("가입중입니다...");
                    mDialog.show();

                    //파이어베이스에 신규계정 등록하기
                    firebaseAuth.createUserWithEmailAndPassword(Email, pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {

                            //가입 성공시
                            if (task.isSuccessful()) {
                                mDialog.dismiss();

                                //파이어베이스에서 변수 가져오기
                                //FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                //String email = firebaseUser.getEmail();
                                //String uid = firebaseUser.getUid();

                                //생성자
                                //User user = new User(email, pwd, pwdcheck, name, phone);

                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if (user != null) {
                                    Map<String, Object> userMap = new HashMap<>();
                                    userMap.put(FirebaseId.documentId, user.getUid());
                                    userMap.put(FirebaseId.name, name);
                                    userMap.put(FirebaseId.email, Email);
                                    userMap.put(FirebaseId.password, pwd);

                                    mStore.collection(FirebaseId.user)
                                            .document(user.getUid()).set(userMap, SetOptions.merge());
                                    finish();

                                } else  {
                                    Toast.makeText(RegisterActivity.this, "회원가입에러", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                        /*  리얼타임 데이터베이스 이용한 회원가입
                                    String email = user.getEmail();
                                    String uid = user.getUid();

                                    //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
                                    HashMap<Object, String> hashMap = new HashMap<>();
                                    hashMap.put("uid", uid);
                                    hashMap.put("email", email);
                                    hashMap.put("pwd", pwd);
                                    hashMap.put("name", name);
                                    hashMap.put("phone", phone);

                                    //FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference databaseReference = firebaseDatabase.getReference("Users");   //??
                                    databaseReference.child(uid).setValue(hashMap);
                                    //databaseReference.child("message").child("안녕").setValue("2"); //default db > message > 안녕 > 2

                                    //가입이 이루어져을시 가입 화면을 빠져나감.
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                                } else {
                                    mDialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "이미 존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                                    return;  //해당 메소드 진행을 멈추고 빠져나감.

                                }
                            }
                        });
                     */
                    
                
                //비밀번호 오류시    
                }else{
                    Toast.makeText(RegisterActivity.this, "비밀번호가 틀렸습니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    public boolean onSupportNavigateUp(){
        onBackPressed(); // 뒤로가기 버튼이 눌렸을시
        return super.onSupportNavigateUp(); // 뒤로가기 버튼
    }
}
