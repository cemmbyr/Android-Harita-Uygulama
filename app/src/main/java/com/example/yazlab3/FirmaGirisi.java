package com.example.yazlab3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirmaGirisi extends AppCompatActivity {

    EditText username,password;
    String kullaniciAdi,sifre,kontrol,kontrol2;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firma_girisi);
        username=(EditText)(findViewById(R.id.editText8));
        password=(EditText)findViewById(R.id.editText9);
    }

    public void giris(View view){
        kontrol=username.getText().toString().trim();
        kontrol2=password.getText().toString().trim();
        if (!kontrol.equalsIgnoreCase("")&& !kontrol2.equalsIgnoreCase("")){
            DatabaseReference databaseReference = firebaseDatabase.getReference("Firma").child(kontrol);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    kullaniciAdi = dataSnapshot.child("FirmaAdi").getValue().toString();
                    sifre = dataSnapshot.child("FirmaId").getValue().toString();
                    if(kullaniciAdi.equals(username.getText().toString().trim()) && sifre.equals(password.getText().toString().trim())){
                        Intent intent=new Intent(getApplicationContext(),Reklam.class);
                        intent.putExtra("isimler",kullaniciAdi);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(FirmaGirisi.this, "Girdiğiniz Bilgileri Kontrol Ediniz", Toast.LENGTH_SHORT).show();
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
}
