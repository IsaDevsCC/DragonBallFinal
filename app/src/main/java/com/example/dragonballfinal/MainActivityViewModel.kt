package com.example.dragonballfinal

import android.util.Log
import androidx.lifecycle.ViewModel
import okhttp3.Credentials
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

const val BASE_URL = "https://dragonball.keepcoding.education/api/"

var token = ""

class MainActivityViewModel : ViewModel() {

    fun isUserValid(user : String) = user.contains('@') && user.contains('.')

    fun isPassValid(password : String) = password.length <= 4

    /**
     *  FUNCIÓN DE EXTENSIÓN : SE PUEDEN CREAR PARA PODER SER INVOCADAS POR EL TIPO DE OBJETO CON QUE SE DEFINE (STRING, ARRAYS ETC) COMO UNA FUNCIÓN PROPIA.
     */
    fun String.contarVocales() : Int {
        var counter = 0
        forEach { if(it == 'a') counter++ }

        return counter
    }

    /**
     * REQUEST INTERNET
     */
    suspend fun loguear(user: String, password: String) : LoginState {
        // GESTOR DE LAS LLAMADAS A INTERNET (OBJETO HTTP)
        val client = OkHttpClient()
        val url = "${BASE_URL}auth/login"
        var credentials = Credentials.basic(user, password)

        // POST
        val fromBody = FormBody.Builder().build()
        // CONSTRUIMOS LA LLAMADA
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", credentials)
            .post(fromBody)
            .build()

        // REALIZAMOS LA LLAMADA
        val call = client.newCall(request)
        val response = call.execute()

        if (response.isSuccessful){
            response.body?.let {
                token = it.string()
                Log.w("LOGGIN2", "CORRECTO LOGIN $token")
                return LoginState.OnSucess
            } ?: return LoginState.OnError("No se ha recibido token")
        }
        else
            return LoginState.OnError(response.message)
    }

    sealed class LoginState {
        data class OnError(val message : String) : LoginState()
        object OnSucess : LoginState()
    }
}

