package br.com.ericleber.contatos.ui.contatolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.ericleber.contatos.R
import br.com.ericleber.contatos.data.db.entity.ContatoEntity
import kotlinx.android.synthetic.main.contato_item.view.*

class ContatoListAdapter(
    private val contatos: List<ContatoEntity>
) : RecyclerView.Adapter<ContatoListAdapter.ContatoListViewHolder>() {

    var onItemClick: ((entity: ContatoEntity) -> Unit)? =  null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContatoListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contato_item, parent, false)

        return ContatoListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContatoListViewHolder, position: Int) {
      holder.bindView(contatos[position])
    }

    override fun getItemCount() = contatos.size

    inner class ContatoListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val textViewContatoNome: TextView = itemView.text_contato_nome
        private val textViewContatoCelular: TextView = itemView.text_contato_celular
        private val textViewContatoEmail: TextView = itemView.text_contato_email

        fun bindView(contato: ContatoEntity){
            textViewContatoNome.text = contato.nome
            textViewContatoCelular.text = contato.celular
            textViewContatoEmail.text = contato.email

            itemView.setOnClickListener{
                onItemClick?.invoke(contato)
            }
        }
    }
}