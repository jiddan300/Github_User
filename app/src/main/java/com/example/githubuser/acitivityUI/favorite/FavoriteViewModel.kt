package com.example.githubuser.acitivityUI.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.database.Favorite
import com.example.githubuser.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : ViewModel()  {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun getAllFavorite(): LiveData<List<Favorite>> = mFavoriteRepository.getAllFavorite()

}