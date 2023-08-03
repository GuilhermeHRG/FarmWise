package com.example.farmwise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.farmwise.Model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Objects;


public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private Button botaoCadastrar;
    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);


        mAuth = FirebaseAuth.getInstance();


        inicializar();

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarCampos();
            }
        });
    }


    private void validarCampos() {
        String nome = campoNome.getText().toString().trim();
        String email = campoEmail.getText().toString().trim();
        String senha = campoSenha.getText().toString().trim();

        if (nome.isEmpty()) {
            Toast.makeText(this, "Campo Nome não pode ser vazio!", Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Campo Email não pode ser vazio!", Toast.LENGTH_SHORT).show();
        } else if (senha.isEmpty()) {
            Toast.makeText(this, "Campo Senha não pode ser vazio!", Toast.LENGTH_SHORT).show();
        } else {
            cadastrarUsuario(nome, email, senha);
        }
    }

    private void cadastrarUsuario(String nome, String email, String senha) {
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                        Usuario usuario = new Usuario(nome, email);
                        Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        abrirHome();
                    } else {
                        String excecao = "";

                        try{
                            throw task.getException();
                        }catch (FirebaseAuthEmailException e){
                            excecao = "Digite um E-mail válido!";
                        }catch (FirebaseAuthWeakPasswordException e){
                            excecao = "Digite uma senha mais forte!";
                        }catch (FirebaseAuthUserCollisionException e){
                            excecao = "Usuário já tem cadastro!";
                        }catch(Exception e){
                            excecao = "Erro ao cadastrar usuário!"+ e.getMessage();
                            e.printStackTrace();
                        }
                        Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void abrirHome() {
        Toast.makeText(this, "Bem-Vindo! "+campoNome.getText(), Toast.LENGTH_LONG).show();
        Intent i = new Intent(CadastroActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void inicializar() {
        campoNome = findViewById(R.id.editTextNome);
        campoEmail = findViewById(R.id.editTextemail);
        campoSenha = findViewById(R.id.editTextSenha);
        botaoCadastrar = findViewById(R.id.buttonLogin);
    }
}
