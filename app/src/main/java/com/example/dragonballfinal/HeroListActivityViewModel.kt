package com.example.dragonballfinal

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import okhttp3.Credentials
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

class HeroListActivityViewModel : ViewModel() {
    fun downloadHeros() = List(100) { "Vegeta $it"}

    suspend fun loadHeroes() : HeroListState {
        // GESTOR DE LAS LLAMADAS A INTERNET (OBJETO HTTP)
        val client = OkHttpClient()
        val url = "${BASE_URL}heros/all"

        // POST
        val fromBody = FormBody.Builder()
            .add("name", "")
            .build()
        // CONSTRUIMOS LA LLAMADA
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $token")
            .post(fromBody)
            .build()

        // REALIZAMOS LA LLAMADA
        val call = client.newCall(request)
        val response = call.execute()

        if (response.isSuccessful){
            response.body?.let {
                //token = it.toString()
                Log.w("LOGGIN2", "CORRECTO LOGIN $token")
                //Log.w("HEROES", "CORRECTO HEROE ${it.string()}")
                val heroDTO = Gson().fromJson(it.string(), Array<HeroDTO>::class.java)

                //heroDTO.toList()
                return HeroListState.OnSucess(heroDTO.map { Hero(it.name, it.photo)}) // MAPPEANDO DIRECTAMENTE UN OBJETO
            } ?: return HeroListState.OnError("No se ha recibido token")
        }
        else
            return HeroListState.OnError(response.message)
    }

    data class HeroDTO(
        val id: String,
        val description: String,
        val name: String,
        val photo: String,
        val favorite: Boolean
    )

    data class Hero(
        val nombre : String,
        val image : String
    )

    sealed class HeroListState {
        data class OnSucess(val heroList : List<Hero>) : HeroListState()
        data class OnError(val  message : String) : HeroListState()
    }
}