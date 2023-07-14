package com.example.dragonballfinal

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dragonballfinal.databinding.ItemHeroBinding


/**
 * - Si se desea crear una comunicación entre el adapter y la actividty, se debe de pasar como parámetro
 * en el constructor del adapter la activity con la que se debe de comunicar
 */
class HeroAdapter(val callback : HeroAdapterInterface) : RecyclerView.Adapter<HeroAdapter.HeroViewHolder>() {

    /**
     *  AL SER LA INTERFACE RECIBIDA COMO PARÁMETRO EN EL ADAPTER, YA EXISTE COMUNICACIÓN ENTRE AMBOS, AHORA, SE DEBE DE
     *  RECOGER EL ADAPTER DESDE ACÁ Y DEFINIR LAS FUNCIONES QUE REIBIRÁ EL ADAPTER
     *  PARA GESTIONAR LA COMUNICACIÓN
     */
    interface HeroAdapterInterface {
        fun abrirDetalle(name : String)
    }

    // REDEFINIMOS LA VARIABLE LIST AL TIPO QUE SERÁ ENVIADA DESDE EL VIEWMODEL
    private var list = mutableListOf<HeroListActivityViewModel.Hero>()

    /**
     *  EL PARÁMETRO DE LA ACTIVITY TAMBIÉN SE DEBE DE PASAR AL VIEWHOLDER, DEFINIENDO EN SU CONSTRUCTOR
     */
    class HeroViewHolder(val binding : ItemHeroBinding, val callback : HeroAdapterInterface) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SuspiciousIndentation")

        /**
         * reconvertimos la función bind para que reciba específicamente el modelo que se desea printar en la celda
         */
        fun bind(hero : HeroListActivityViewModel.Hero, position: Int) {
            with(binding){
                tvName.text = hero.nombre

                // INSERCIÓN DEL URI IMAGE EN LA IMAGEVIEW MEDIANTE LA CONVERSIÓN DEL GLIDE
                Glide.with(root)
                    .load(hero.image)
                    .into(ivPhoto)

                val bgColorId = if(position %2 == 0) R.color.custom1 else R.color.custom2
                    // REUSO DEL CONTEXT MEDIANTE LA INVOCACIÓN POR '.' DEL ROOT,O DE CUALQUIER ELEMENTO DE LA ACTIVITY
                    binding.root.setBackgroundColor(ContextCompat.getColor(binding.root.context, bgColorId))

                root.setOnClickListener{
                    Toast.makeText(root.context, "Push $position", Toast.LENGTH_LONG).show()
                    // ES LA VARIABLE QUE SE COMUNICA CON EL ACTIVITY
                    callback.abrirDetalle(hero.nombre)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    /**
     *  - INVOCAMOS EL BINDING DEL ITEM A MOSTRAR
     *
     *  - Inflate refiere a la conversión del xml al código que se invoca desde acá
     *
     *  - CON EL PARÁMETRO 'VIEWTYPE' SE PUEDE ESPECIFICAR EL INFLATE DE MÚLTIPLES LAYOUTS
     *
     *  - AL VIEWHOLDER, TAMBIÉN SE LE DEBE DE PASAR EL ACTIVITY QUE RECIBE EL ADAPTER PARA QUE MANEJE LA COMUNICACIÓN
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        return HeroViewHolder(ItemHeroBinding.inflate(
                                        LayoutInflater.from(parent.context),
                                        parent, false), callback)
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getCharacters(listHero : List<HeroListActivityViewModel.Hero>) {
        list = listHero.toMutableList()
        // CONDICIONA AL ADAPTER PARA QUE VERIFIQUE CAMBIOS EN LOS DATOS SI LOS HAY
        notifyDataSetChanged()
    }
}