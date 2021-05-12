package br.com.ericleber.contatos.ui.contato

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.ericleber.contatos.R
import br.com.ericleber.contatos.repository.ContatoRepository
import kotlinx.coroutines.launch

class ContatoViewModel(
    private val repository : ContatoRepository //recebendo a interface (view model não conhece o repositório só o contrato)
) : ViewModel() {

    //notificando pra view que teve alteração
    private val _contatoStateEventData =
        MutableLiveData<ContatoState>() //tornando acesso privado pra evitar comportamentos estranhos
    val contatoStateEventData: LiveData<ContatoState> //permitindo a view acessar o livedata que é imutável
        get() = _contatoStateEventData

    private val _messageEventData = MutableLiveData<Int>()
    val messageEventData: LiveData<Int>
        get() = _messageEventData

    fun addOrUpdateContato(name: String, celular: String, email: String, id: Long = 0) = viewModelScope.launch {
        if (id > 0) {
            updateContato(id, name, celular, email)
        } else {
            insertContato(name, celular, email)
        }
    }

    private fun updateContato(id: Long, name: String, celular: String, email: String) = viewModelScope.launch {
        try {
            repository.updateContato(id, name, celular, email)

            _contatoStateEventData.value = ContatoState.Updated
            _messageEventData.value = R.string.contato_updated_successfully
        } catch (ex: Exception) {
            _messageEventData.value = R.string.contato_error_to_update
            Log.e(TAG, ex.toString())
        }
    }

    private fun insertContato(name: String, celular: String, email: String) = viewModelScope.launch {
        try {
            val id = repository.insertContato(name, celular, email)
            if (id > 0) {
                _contatoStateEventData.value =
                    ContatoState.Inserted //salve salve família, ocorreu um evento de inserção ai, fiquem espertos...
                _messageEventData.value = R.string.contato_insert_successfully
            }
        } catch (ex: Exception) {
            _messageEventData.value = R.string.contato_error_to_insert
            Log.e(TAG, ex.toString())
        }
    }

    fun removeContato(id: Long) = viewModelScope.launch {
        try {
            if (id > 0) {
                repository.deleteContato(id)
                _contatoStateEventData.value = ContatoState.Deleted
                _messageEventData.value = R.string.contato_deleted_successfully
            }
        } catch (ex: Exception) {
            _messageEventData.value = R.string.contato_error_to_delete
            Log.e(TAG, ex.toString())
        }
    }

    sealed class ContatoState {
        object Inserted : ContatoState()
        object Updated : ContatoState()
        object Deleted : ContatoState()
    }

    companion object {
        private val TAG = ContatoViewModel::class.java.simpleName
    }
}