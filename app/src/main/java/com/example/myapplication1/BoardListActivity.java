package com.example.myapplication1;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication1.adapters.Adapter;
import com.example.myapplication1.model.Data;
import com.example.myapplication1.model.FirebaseId;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardListActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    public static List<String> documentIdList = new ArrayList<>();

    private Context mContext;
    private ArrayList<Data> mArrayList;
    private Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private EditText edit_name, edit_number;
    private Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_setting);
        mContext = getApplicationContext ();
        edit_name = findViewById (R.id.edit_name);
        edit_number = findViewById (R.id.edit_number);
        btn_save = findViewById (R.id.btn_save);
        mRecyclerView = findViewById (R.id.recycler);

        //리사이클러뷰 객체 생성 어댑터 연결 약간의 리팩토링
        initRecyclerView ();

        /*  initRecyclerView에 넣음
        레이아웃메니저는 리사이클러뷰의 항목 배치를 어떻게 할지 정하고, 스크롤 동작도 정의한다.
        수평/수직 리스트 LinearLayoutManager
        그리드 리스트 GridLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager (mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager (layoutManager);

        mArrayList = new ArrayList<>();
        mAdapter = new Adapter (mContext, mArrayList);
        mRecyclerView.setAdapter (mAdapter);
         */

        //버튼 클릭이벤트
        //이름과 전화번호를 입력한 후 버튼을 클릭하면 어레이리스트에 데이터를 담고 리사이클러뷰에 띄운다.
        btn_save.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                if(edit_name.getText ().length ()==0&&edit_number.getText ().length ()==0){
                    Toast.makeText (mContext,"이름과 전화번호를 입력해주세요", Toast.LENGTH_SHORT).show ();
                }else{
                    String name = edit_name.getText ().toString ();
                    String number = edit_number.getText ().toString ();
                    edit_name.setText ("");
                    edit_number.setText ("");
                    Data data = new Data (name, number);

                    mArrayList.add (data);
                    mAdapter.notifyItemInserted (mArrayList.size ()-1);

                    //데이터를 테이블에 삽입합니다.
                    insertNumber (name, number);
                }
            }
        });

        //리사이클러뷰 아이템 클릭 이벤트
        mAdapter.setOnItemClickListener (new Adapter.OnItemClickListener () {

            //아이템 클릭시 토스트메시지
            @Override
            public void onItemClick(View v, int position) {
                String name = mArrayList.get (position).getName ();
                String number = mArrayList.get (position).getNumber ();
                Toast.makeText (mContext, "이름 : "+name+"\n전화번호 : "+number, Toast.LENGTH_SHORT).show ();

                //startActivity 를 사용해 DetailActivity 를 호출한다.
                Intent intent = new Intent (mContext, BoardListDetailActivity.class);
                intent.putExtra ("name", name);
                intent.putExtra ("number", number);
                startActivity (intent);
            }

            //수정
            @Override
            public void onEditClick(View v, int position) {
                String name = mArrayList.get (position).getName ();
                String number = mArrayList.get (position).getNumber ();

                editItem (name, number, position);
            }

            //삭제
            @Override
            public void onDeleteClick(View v, int position) {

                //핵심 !! 삭제버튼 누를때 파이어스토에서도 지우고, 어댑터에서도 지워준다.
                //읽기, 쓰기, 수정, 삭제 모두 마찬가지.
                
                //파이어베이스에서 지우기
                String name = mArrayList.get (position).getName ();
                String number = mArrayList.get (position).getNumber ();
                deleteNumber (name, number, position);

                //어댑터에서도 지워준다.
                mArrayList.remove (position);
                mAdapter.notifyItemRemoved (position);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    //리사이클러뷰
    private void initRecyclerView() {
        //레이아웃메니저는 리사이클러뷰의 항목 배치를 어떻게 할지 정하고, 스크롤 동작도 정의한다.
        //수평/수직 리스트 LinearLayoutManager
        //그리드 리스트 GridLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager (mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager (layoutManager);

        mArrayList = new ArrayList<> ();
        mAdapter = new Adapter (mContext, mArrayList);
        mRecyclerView.setAdapter (mAdapter);

        loadData ();
    }


    //데이터 불러오기
    private void loadData() {

        mStore.collection("post").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        for (DocumentSnapshot snap : task.getResult()) {
                            Map<String, Object> shot = snap.getData();
                            String title = String.valueOf(shot.get("title"));
                            String contents = String.valueOf(shot.get("contents"));
                            Data data = new Data(title, contents);
                            mArrayList.add(data);

                            String documentIds = snap.getId();
                            Log.d("아이디 가져오기 ==================" , documentIds);
                            documentIdList.add(documentIds);   //documentId 모두 저장
                        }
                        mAdapter.notifyDataSetChanged ();
                    }
                }
            }
        });

    }

    //데이터 삽입
    private void insertNumber(String name, String number){
        if (mAuth.getCurrentUser() != null) {
            Map<String, Object> data = new HashMap<>();
            data.put(FirebaseId.title , name);  //edit_name.getText().toString()
            data.put(FirebaseId.contents , number); //edit_number.getText().toString()
            //data.put(FirebaseId.timestamp , FieldValue.serverTimestamp());
            //data.put(FirebaseId.name, name );   //보드 리스트에 글쓴사람 이름도 나오도록

            mStore.collection(FirebaseId.post).document().set(data, SetOptions.merge()); //게시글 ID 안겹치게

        }

    }

    private void deleteNumber(String name, String number, int position) {
        String thisDocumentId = BoardListActivity.documentIdList.get(position);

        //String postId = mStore.collection(FirebaseId.post).document().getId();
        mStore.collection("post").document(thisDocumentId).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "removeValue");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                e.printStackTrace();
            }
        });

    }


    private void editItem(String name, String number, int position) {
        
        //수정창 새창으로 띄움
        AlertDialog.Builder builder = new AlertDialog.Builder (this);
        View view = LayoutInflater.from (this).inflate (R.layout.dialog, null, false);
        builder.setView (view);

        final AlertDialog dialog = builder.create ();

        final Button btn_edit = view.findViewById (R.id.btn_edit);
        final Button btn_cancel = view.findViewById (R.id.btn_cancel);
        final EditText edit_name = view.findViewById (R.id.edit_editName);
        final EditText edit_number = view.findViewById (R.id.edit_editNumber);

        edit_name.setText (name);
        edit_number.setText (number);

        // 수정 버튼 클릭
        //어레이리스트 값을 변경한다.
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editName = edit_name.getText().toString();
                String editNumber = edit_number.getText().toString();
                mArrayList.get(position).setName(editName);
                mArrayList.get(position).setNumber(editNumber);

                //파이어베이스에서 수정한다.
                updateNumber (name,number,editName,editNumber,position);

                //어댑터에서 수정한다.
                mAdapter.notifyItemChanged(position);
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show ();

    }
    //newName 은 수정된 값, oldName 수정전 값
    private void updateNumber(String oldName, String oldNumber, String newName, String newNumber, int position){

        String thisDocumentId = BoardListActivity.documentIdList.get(position);
        Map<String, Object> data = new HashMap<>();
        data.put(FirebaseId.title , newName);  //edit_name.getText().toString()
        data.put(FirebaseId.contents , newNumber); //edit_number.getText().toString()

        mStore.collection("post").document(thisDocumentId).update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "update Success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                e.printStackTrace();
            }
        });



    }
    @Override
    public void onBackPressed() {
        super.onBackPressed ();
    }
}