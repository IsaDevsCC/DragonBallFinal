package com.example.dragonballfinal

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.dragonballfinal.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val viewModel : MainActivityViewModel by viewModels()
    @SuppressLint("SuspiciousIndentation", "CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        // USO DEL SCOPE 'WITH' PARA PRESCINDIR DEL BINDING YA QUE ESTAREMOS EN SU CONTEXTO

        with(binding){
            /**
             *  LEO PREFERENCES
             */
            getPreferences(Context.MODE_PRIVATE).apply {
                etPassword.setText(getString("PASSWORD", ""))
                etUser.setText(getString("LOGIN", ""))
            }
            // LAMBDA 'doAfterTextChanged' es UN MÉTODO PROPIO DE ANDROID
            etUser.doAfterTextChanged {  editable ->
                editable?.let {
                    btLogin.isEnabled =
                        !(it.toString().isEmpty() && etPassword.text.isEmpty())
                }
            }
            // TODO muestra toast ok si el usuaraio tiene una arroba y un punto y la contraseña es mayor a 4 lestras

            btLogin.setOnClickListener {
                //if( etUser.text.contains('@') && etUser.text.contains('.') && etPassword.text.length >= 4) --> SIN VIEWMODEL
                //if(viewModel.isPassValid(etPassword.text.toString()) && viewModel.isUserValid(etUser.text.toString()))
                if(etPassword.text.toString().isNotEmpty() && etUser.text.toString().isNotEmpty())
                    Toast.makeText(this@MainActivity, "Hay un character ", Toast.LENGTH_LONG).show()
                    Log.w("LOGGIN", "CORRECTO LOGIN")
                    loguea(etUser.text.toString(), etPassword.text.toString())

                /**
                 *  ALMACENO PREFERENCES
                 */
                    getPreferences(Context.MODE_PRIVATE).edit().apply() {
                        putString("PASSWORD", etPassword.text.toString())
                        putString("LOGIN", etUser.text.toString())
                    ckOk.isChecked = true
                }
            }
        }
        val respose = "El usuario debe de contener un @, un . y su password debe de ser mayor a 4 letras"
        Toast.makeText(this, "Hay un character $respose", Toast.LENGTH_LONG).show()
    }

    private fun loguea(name : String, password : String){
        // 'DISPATCHERS.IO' INDICA QUE CAMBIE EL HILO PARA QUE NO BLOQUEE LA INTERFAZ GRÁFICA, LO QUE EVITA QUE LA APP
        // EXPLOTE
        lifecycleScope.launch(Dispatchers.IO) {
            val state = viewModel.loguear(name, password)
            when(state) {
                is MainActivityViewModel.LoginState.OnSucess -> abrirHeroListActivity()
                is MainActivityViewModel.LoginState.OnError ->
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@MainActivity, state.message, Toast.LENGTH_LONG).show()
                    }
            }
        }

    }

    private fun abrirHeroListActivity() {

        val intent = Intent(this, HeroListActivity::class.java)
        startActivity(intent)
        finish()
    }
}