package com.example.dsm_frontend.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.dsm_frontend.core.Resource
import com.example.dsm_frontend.repository.MinimarketRepository
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class ProductViewModel(private val repo: MinimarketRepository) : ViewModel() {

    fun getProductByWord(word: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading)
        try {
            emit(repo.getProductsByWord(word))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

}

class ProductViewModelFactory(private val repo: MinimarketRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(MinimarketRepository::class.java).newInstance(repo)
    }
}
