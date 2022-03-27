package com.example.myapplication1.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication1.R;
import com.example.myapplication1.model.Data;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private String TAG = "Adapter";
    private Context mContext;
    private ArrayList<Data> mArrayList; //데이터를 담을 어레이리스트

    public Adapter(Context context, ArrayList<Data> arrayList) {
        this.mArrayList = arrayList;
        this.mContext =context;
    }

    //리스트의 각 항목을 이루는 디자인(xml)을 적용.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate (R.layout.item_view, parent, false);
        ViewHolder vh = new ViewHolder (view);
        return vh;
    }

    //리스트의 각 항목에 들어갈 데이터를 지정.
    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        Data data = mArrayList.get (position);

        holder.tv_name.setText (data.getName ());
        holder.tv_number.setText (data.getNumber ());
    }

    //화면에 보여줄 데이터의 갯수를 반환.
    @Override
    public int getItemCount() {
        Log.d (TAG, "getItemCount: "+mArrayList.size ());
        return mArrayList.size ();
    }

    //아이템 클릭 리스너 인터페이스
    public interface OnItemClickListener{
        void onItemClick(View v, int position); //뷰와 포지션값

        //수정
        void onEditClick(View v, int position);

        //삭제
        void onDeleteClick(View v, int position);
    }

    //리스너 객체 참조 변수
    private OnItemClickListener mListener = null;
    //리스너 객체 참조를 어댑터에 전달 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    //뷰홀더 객체에 저장되어 화면에 표시되고, 필요에 따라 생성 또는 재활용 된다.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_number;
        Button btn_edit, btn_cancel;

        public ViewHolder(@NonNull View itemView) {
            super (itemView);
            this.tv_name = itemView.findViewById (R.id.tv_name);
            this.tv_number = itemView.findViewById (R.id.tv_number);
            this.btn_edit = itemView.findViewById (R.id.btn_edit);
            this.btn_cancel = itemView.findViewById (R.id.btn_cancel);

            itemView.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition ();
                    if (position!=RecyclerView.NO_POSITION){
                        if (mListener!=null){
                            mListener.onItemClick (view,position);
                        }
                    }
                }
            });

            btn_edit.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition ();
                    if (position!=RecyclerView.NO_POSITION){
                        if (mListener!=null){
                            mListener.onEditClick (view,position);
                        }
                    }
                }
            });

            btn_cancel.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition ();
                    if (position!=RecyclerView.NO_POSITION){
                        if (mListener!=null){
                            mListener.onDeleteClick(view,position);
                        }
                    }
                }
            });
        }


    }
}
