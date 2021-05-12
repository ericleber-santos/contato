package br.com.ericleber.contatos.ui.contato

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.ericleber.contatos.R
import br.com.ericleber.contatos.data.db.AppDatabase
import br.com.ericleber.contatos.data.db.dao.ContatoDAO
import br.com.ericleber.contatos.extension.hideKeyboard
import br.com.ericleber.contatos.repository.DatabaseDataSource
import br.com.ericleber.contatos.repository.ContatoRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.contato_fragment.*

class ContatoFragment : Fragment(R.layout.contato_fragment) {

    //instanciando viewmodel utilizando factory, assim é possivel fazer injeção de dependencia passando parametro pro viewmodel
    //assim não precisa ter um late init var, não vai ser instanciado no oncreate view
    private val viewModel: ContatoViewModel by viewModels{
        //implementando o factory
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val contatoDAO: ContatoDAO = AppDatabase.getInstance(requireContext()).contatoDAO
                val repository: ContatoRepository = DatabaseDataSource(contatoDAO)
                return ContatoViewModel(repository) as T
            }
        }
    }

    private val args: ContatoFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //diferenciando quando é uma inserção ou atualização
        args.contato?.let { contato ->
            button_contato.text = getString(R.string.contato_button_update)
            input_name.setText(contato.nome)
            input_celular.setText(contato.celular)
            input_email.setText(contato.email)

            button_delete.visibility = View.VISIBLE
        }

        observeEvents()
        setListeners()
    }

    private fun observeEvents() {
        viewModel.contatoStateEventData.observe(viewLifecycleOwner){ contatoState ->
            when(contatoState){
                is ContatoViewModel.ContatoState.Inserted,
                is ContatoViewModel.ContatoState.Updated,
                is ContatoViewModel.ContatoState.Deleted -> {
                    clearFields()
                    hideKeyBoard()
                    requireView().requestFocus()

                    findNavController().popBackStack() //retorna pra tela anterior
                }
            }
        }

        viewModel.messageEventData.observe(viewLifecycleOwner){ stringResId ->
            Snackbar.make(requireView(), stringResId, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun clearFields() {
        input_name.text?.clear()
        input_celular.text?.clear()
        input_email.text?.clear()
    }

    private fun hideKeyBoard() {
       val parentActivity = requireActivity()
        if(parentActivity is AppCompatActivity){
            parentActivity.hideKeyboard() //utilizando uma extension criada pra poder ser reutilizada por outras views
        }
    }

    private fun setListeners() {
        button_contato.setOnClickListener() {
            val name = input_name.text.toString()
            val celular = input_celular.text.toString()
            val email = input_email.text.toString()

            viewModel.addOrUpdateContato(name, celular, email,args.contato?.id ?: 0)
        }

        button_delete.setOnClickListener{
            viewModel.removeContato(args.contato?.id ?: 0)
        }
    }

}