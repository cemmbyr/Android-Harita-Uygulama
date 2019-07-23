package com.example.yazlab3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText username,password;
    String kullaniciAdi,sifre,kontrol,kontrol2;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username=(EditText)(findViewById(R.id.editText));
        password=(EditText)findViewById(R.id.editText2);
    }

    public void giris(View view){
        kontrol=username.getText().toString().trim();
        kontrol2=password.getText().toString().trim();
        if (!kontrol.equalsIgnoreCase("")&& !kontrol2.equalsIgnoreCase("")){
            DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(kontrol);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    kullaniciAdi = dataSnapshot.child("KullaniciAdi").getValue().toString();
                    sifre = dataSnapshot.child("Sifre").getValue().toString();
                    if(kullaniciAdi.equals(username.getText().toString().trim()) && sifre.equals(password.getText().toString().trim())){
                        Intent intent=new Intent(getApplicationContext(),Arayuz.class);
                        intent.putExtra("isimler",kullaniciAdi);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Girdiğiniz Bilgileri Kontrol Ediniz", Toast.LENGTH_SHORT).show();
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else{
            Toast.makeText(this,"Hicbir alan boş birakilamaz",Toast.LENGTH_SHORT).show();
        }

    }

    public void kayit(View view){
        startActivity(new Intent(getApplicationContext(),Kayit.class));
    }

    public void unuttum(View view){
        startActivity(new Intent(getApplicationContext(),SifreYenile.class));
    }


}
