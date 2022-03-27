package com.example.myapplication1;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    SoundPool soundf;
    Button btn;
    int tom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 클릭시 웹사이트로 이동
         */
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "버튼을 눌렀습니다.", Toast.LENGTH_LONG).show();

                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.daum.net"));
                startActivity(myIntent);

            }
        });


        /**
            게시판
         */
        Button setting = (Button) findViewById(R.id.settingActivity);
        setting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BoardListActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 사운드 재생
         */
        soundf = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        tom = soundf.load(this, R.raw. ae , 1);
        btn = (Button)findViewById(R.id.sound );
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                soundf.play(tom, 5000, 5000, 0, 1, 1);
            }
        });

        /**
         * 앱 종료하기
         */
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
        AlertDialog.Builder alert1 = new AlertDialog.Builder(MainActivity.this);

        imageButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                alert1.setMessage("앱을 종료하겠습니까?").setCancelable(false).setPositiveButton("네",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });

                AlertDialog alert= alert1.create();
                alert.show();
            }

        });

        Button btn6 = (Button) findViewById(R.id.button6);
        btn6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.act1);
                v.startAnimation(anim);
            }
        });


        Button btn7 = (Button) findViewById(R.id.button7);
        btn7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.act2);
                v.startAnimation(anim);
            }
        });

        /**
         * 특정번호로 전화걸기
         */
        ImageButton imageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        imageButton2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:010-111-1111"));
                startActivity(intent);
            }
        });




    }

}