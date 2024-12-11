package com.example.prova2pdm_amalvada.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prova2pdm_amalvada.db.CafeDAO
import com.example.prova2pdm_amalvada.models.Cafe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CafeViewModel: ViewModel() {

    private val _cafes = MutableLiveData<List<Cafe>>(emptyList())
    private var cafeDAO = CafeDAO()
    private val _selectedCafe = MutableLiveData<Cafe>()

    val cafes: LiveData<List<Cafe>> get() = _cafes
    val selectedCafe: LiveData<Cafe> get() = _selectedCafe

    init {
        get_all()
    }

    fun set_selected_cafe(cafe: Cafe) {
        _selectedCafe.value = cafe
    }

    fun getCafeById(cafeId: String?): Cafe? {
        return _cafes.value?.find { it.idCafe == cafeId }
    }

    // Função para mostrar quantos itens estão presentes no banco
    fun getTotalCafes(): Int {
        return _cafes.value?.size ?: 0
    }

    // Função para mostrar o item mais caro
    fun getMostExpensiveCafe(): Cafe? {
        return _cafes.value?.maxByOrNull { it.precoCafe }
    }

    // Função para ordenar por ordem de sabor
    fun getCafesSortedBySabor(): List<Cafe> {
        return _cafes.value?.sortedBy { it.sabor } ?: emptyList()
    }

    // Função para calcular o valor médio dos preços
    fun getAveragePrice(): Double {
        val cafes = _cafes.value ?: return 0.0
        return if (cafes.isNotEmpty()) {
            cafes.map { it.precoCafe }.average()
        } else {
            0.0
        }
    }

    // Função para mostrar o café com mais aroma
    fun getCafeWithMostAroma(): Cafe? {
        return _cafes.value?.maxByOrNull { it.aroma }
    }

    // Função para mostrar o café com menos acidez
    fun getCafeWithLeastAcidez(): Cafe? {
        return _cafes.value?.minByOrNull { it.acidez }
    }

    fun create(cafe: Cafe) {
        viewModelScope.launch(Dispatchers.IO) {
            cafeDAO.insertCafe(cafe)
            get_all()
        }
    }

    fun update(cafe: Cafe) {
        viewModelScope.launch(Dispatchers.IO) {
            cafeDAO.updateCafe(cafe)
            get_all()
        }
    }

    fun get_all() {
        viewModelScope.launch(Dispatchers.IO) {
            _cafes.postValue(cafeDAO.getAllCafes())
        }
    }

    fun delete(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            cafeDAO.deleteCafe(id)
            get_all()
        }
    }

}