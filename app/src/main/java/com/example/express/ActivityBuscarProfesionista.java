package com.example.express;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.express.models.Usuario;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActivityBuscarProfesionista extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private RecyclerView mBlogList;
    ImageView regresar;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_profesionista);

        inicializarFirebase();

        regresar = (ImageView) findViewById(R.id.btn_regresar);

        mBlogList = (RecyclerView) findViewById(R.id.myRecyclerView);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));

        final SearchView searchView = (SearchView) findViewById(R.id.search_profesionist);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!s.equals("")) {
                    FirebaseRecyclerAdapter<Usuario, InicioActivity.UsuarioViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Usuario, InicioActivity.UsuarioViewHolder>
                            (Usuario.class, R.layout.blog_row, InicioActivity.UsuarioViewHolder.class, databaseReference.child("Usuario").orderByChild("profesion").startAt(s).endAt(s + "\uf8ff")) {
                        @Override
                        protected void populateViewHolder(InicioActivity.UsuarioViewHolder usuarioViewHolder, Usuario usuario, int i) {
                            usuarioViewHolder.setNombre(usuario.getNombre());
                            usuarioViewHolder.setProfesion(usuario.getProfesion());
                            usuarioViewHolder.setDescripcion(usuario.getDescripcion());
                            usuarioViewHolder.setImagen(getApplicationContext(), usuario.getImagen());

                            final String usuarioID = usuario.getUid();
                            usuarioViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ActivityBuscarProfesionista.this, MostrarUsuarioActivity.class);
                                    intent.putExtra("UsuarioID", usuarioID);
                                    intent.putExtra("isContacto", "nulo");
                                    startActivity(intent);
                                }
                            });
                        }
                    };
                    mBlogList.setAdapter(firebaseRecyclerAdapter);
                }else{
                    mBlogList.setAdapter(null);
                }
                return false;
            }
        });

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityBuscarProfesionista.this,InicioActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}