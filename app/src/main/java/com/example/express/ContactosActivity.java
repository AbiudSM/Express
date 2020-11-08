package com.example.express;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.express.models.Contactos;
import com.example.express.models.Usuario;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ContactosActivity extends AppCompatActivity {

    BottomNavigationView mBottomNavigationView;
    private RecyclerView mBlogList;

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String uid = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setSelectedItemId((R.id.menu_contacts));

        inicializarFirebase();

        //RecyclerView
        databaseReference.keepSynced(true);
        mBlogList = (RecyclerView) findViewById(R.id.myRecyclerView);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));

        ListarDatos();

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.menu_home){
                    Intent i = new Intent(getApplicationContext(), InicioActivity.class);
                    startActivity(i);
                    overridePendingTransition(0,0);
                }

                if(item.getItemId() == R.id.menu_account){
                    Intent i = new Intent(getApplicationContext(), MiPerfilActivity.class);
                    startActivity(i);
                    overridePendingTransition(0,0);
                }

                return true;
            }
        });
    }

    private void ListarDatos() {
        FirebaseRecyclerAdapter<Usuario, InicioActivity.UsuarioViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Usuario, InicioActivity.UsuarioViewHolder>
                (Usuario.class,R.layout.blog_row, InicioActivity.UsuarioViewHolder.class,databaseReference.child("Usuario").orderByChild("Contactos/"+uid).equalTo(true)) {
            @Override
            protected void populateViewHolder(InicioActivity.UsuarioViewHolder usuarioViewHolder, Usuario usuario, int i) {
                usuarioViewHolder.setNombre(usuario.getNombre());
                usuarioViewHolder.setProfesion(usuario.getProfesion());
                usuarioViewHolder.setDescripcion(usuario.getDescripcion());
                usuarioViewHolder.setImagen(getApplicationContext(),usuario.getImagen());

                final String usuarioID = usuario.getUid();
                usuarioViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ContactosActivity.this,MostrarUsuarioActivity.class);
                        intent.putExtra("UsuarioID",usuarioID);
                        intent.putExtra("isContacto","esContacto");
                        startActivity(intent);
                    }
                });
            }
        };
        mBlogList.setAdapter(firebaseRecyclerAdapter);

    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    // MOSTRAR TOP MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return true;
    }

    // SWITCH DEL TOP MENU
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnSignOut:
                //Detectar al usuario actual
                mAuth = FirebaseAuth.getInstance();

                //SIGN OUT
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}