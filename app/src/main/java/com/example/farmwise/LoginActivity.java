package com.example.farmwise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.farmwise.Model.Usuario;
import com.example.farmwise.Util.ConfiguraBD;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText campoEmailLogin, campoSenha;
    Button botaoAcessar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inicializarComponentes();
        auth = ConfiguraBD.FirebaseAutenticacao();
    }

    public void validarAcesso(View view) {
        String emailLogin = campoEmailLogin.getText().toString();
        String senhaLog = campoSenha.getText().toString();
        if (!emailLogin.isEmpty()) {
            if (!senhaLog.isEmpty()) {
                Usuario usuario = new Usuario();
                usuario.setEmail(emailLogin);
                usuario.setSenha(senhaLog);

                Logar(usuario);

            } else {
                Toast.makeText(this, "Preencha a senha!", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(LoginActivity.this, "Preencha o campo Usuário!", Toast.LENGTH_SHORT).show();
        }
    }

    private void Logar(Usuario usuario) {
        auth.signInWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    abrirHome();

                } else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        excecao = "Usuário não cadastrado!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "E-mail ou senha incorretos!";
                    } catch (Exception e) {
                        excecao = "Erro ao Logar Usuário" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void abrirHome() {
        Toast.makeText(this, "Bem-Vindo! ", Toast.LENGTH_LONG).show();
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void cadastrarse(View v) {
        Intent i = new Intent(this, CadastroActivity.class);
        startActivity(i);
    }

    private void inicializarComponentes() {
        campoEmailLogin = findViewById(R.id.editTextUsuarioLogin);
        campoSenha = findViewById(R.id.editTextSenhaLogin);
        botaoAcessar = findViewById(R.id.buttonLogin);

        // Adicionar o listener para o botão de acesso
        botaoAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarAcesso(view);
            }
        });
    }
}
