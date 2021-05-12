package br.com.ericleber.contatos.ui.contatolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.ericleber.contatos.data.db.entity.ContatoEntity
import br.com.ericleber.contatos.repository.ContatoRepository
import kotlinx.coroutines.launch

class ContatoListViewModel(
        private val repository: ContatoRepository //repositório recebido por injeção de construtor
) : ViewModel() {
        private val _allContactsEvent = MutableLiveData<List<ContatoEntity>>()
        val allContactsEvent : LiveData<List<ContatoEntity>>
                get() = _allContactsEvent

        fun getContatos() = viewModelScope.launch {
                _allContactsEvent.postValue(repository.getAllContatos())
        }
}