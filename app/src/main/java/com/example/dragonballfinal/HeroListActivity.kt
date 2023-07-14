package com.example.dragonballfinal

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dragonballfinal.databinding.ActivityHeroListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *  LA ACTIVITY IMPLEMENTA LA INTERFACE DEL ADAPTER PARA ESTABLECER LA COMUNICACIÓN ENTRE AMBOS,
 *  ENVIANDO SOLO LOS ELEMNTOS QUE LE INTERESAN AL ADAPTER Y HACIENDO QUE SEA MÁS REUTILIZABLE
 */
class HeroListActivity : AppCompatActivity(), HeroAdapter.HeroAdapterInterface {

    private lateinit var binding : ActivityHeroListBinding

    // INVOCACIÓ A SU VIEWMODEL
    private val viewModel : HeroListActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHeroListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // TODO Poner el adapter

        val adapter = HeroAdapter(this)
        // INVOCAMOS EL ADAPTER PARA RELLENAR EL RECYCLER
        binding.rvHeros.adapter = adapter
        // TODO Definir la orientación (vista)
        binding.rvHeros.layoutManager = LinearLayoutManager(this)

        // ASIGNAMOS LA LISTA DE HEROES DEL VIEWMODEL AL ADAPTER
        //adapter.getCharacters(viewModel.downloadHeros())
        lifecycleScope.launch(Dispatchers.IO) {
            // INVOCAMOS LA RESPUESTA DE LA REQUEST Y ALMACENAMOS EN VARIABLE PARA EL WHEN
            val state = viewModel.loadHeroes()
            when (state) {
            is HeroListActivityViewModel.HeroListState.OnSucess ->
                withContext(Dispatchers.Main){
                    adapter.getCharacters(state.heroList)
                }
            is HeroListActivityViewModel.HeroListState.OnError ->
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@HeroListActivity, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    override fun abrirDetalle(name: String) {
        binding.tvTitle.text = "Acabas de presionarme $name"
    }
}