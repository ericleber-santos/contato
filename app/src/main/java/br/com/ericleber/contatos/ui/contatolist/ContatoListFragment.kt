package br.com.ericleber.contatos.ui.contatolist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import br.com.ericleber.contatos.R
import br.com.ericleber.contatos.data.db.AppDatabase
import br.com.ericleber.contatos.data.db.dao.ContatoDAO
import br.com.ericleber.contatos.extension.navigateWithAnimations
import br.com.ericleber.contatos.repository.DatabaseDataSource
import br.com.ericleber.contatos.repository.ContatoRepository
import kotlinx.android.synthetic.main.contato_list_fragment.*

class ContatoListFragment : Fragment(R.layout.contato_list_fragment) {

    private val viewModel: ContatoListViewModel by viewModels{
        //implementando o factory ...aqui se estivesse usando o a biblioteca Koin ou dagger não precisaria ficar repetindo codigo
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val contatoDAO: ContatoDAO = AppDatabase.getInstance(requireContext()).contatoDAO
                val repository: ContatoRepository = DatabaseDataSource(contatoDAO)
                return ContatoListViewModel(repository) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelEvents()
        configureViewListeners()
    }

    private fun observeViewModelEvents() {
        viewModel.allContactsEvent.observe(viewLifecycleOwner){ allContatos ->
            val contatoListAdapter = ContatoListAdapter(allContatos).apply {
                onItemClick = { contato ->
                    val directions = ContatoListFragmentDirections
                            .actionContatoListFragmentToContatoFragment(contato)
                    findNavController().navigateWithAnimations(directions)
                }
            }

            with(recycler_contatos){
                setHasFixedSize(true)
                adapter = contatoListAdapter //vincula ao recycleview
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getContatos()
        //qdo estiver na tela de cadastro e voltar para a anterior(esta), será chamado
        // onresume e a lista sera atualizada
    }

    fun configureViewListeners(){
        fabAddContato.setOnClickListener {
            findNavController().navigateWithAnimations(
                R.id.action_contatoListFragment_to_contatoFragment) //usando a extension criada pra animaçoes da navegação
        }
    }
}