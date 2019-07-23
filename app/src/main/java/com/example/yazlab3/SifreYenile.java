package com.example.yazlab3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SifreYenile extends AppCompatActivity {

    EditText username,password;
    Button kaydet;
    String kontrol,kullaniciAdi,sifre;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifre_yenile);

        username=(EditText)(findViewById(R.id.editText6));
        password=(EditText)findViewById(R.id.editText7);
        kaydet=(Button)findViewById(R.id.button6);

        password.setEnabled(false);
        kaydet.setEnabled(false);

    }
    public void onay(View view){
        kontrol=username.getText().toString().trim();
        if (!kontrol.equalsIgnoreCase("")){
            final DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(kontrol);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    kullaniciAdi = dataSnapshot.child("KullaniciAdi").getValue().toString();
                    if(kullaniciAdi.equals(username.getText().toString().trim())){
                        password.setEnabled(true);
                        kaydet.setEnabled(true);
                    }
                    else{
                        Toast.makeText(SifreYenile.this, "Girdiğiniz Bilgileri Kontrol Ediniz", Toast.LENGTH_SHORT).show();
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
    public void kayde(View view){
        sifre=password.getText().toString().trim();
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users").child(kullaniciAdi);
        dR.child("Sifre").setValue(sifre);
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}
