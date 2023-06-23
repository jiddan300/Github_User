package com.example.githubuser.acitivityUI.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.acitivityUI.main.MainActivity
import com.example.githubuser.api.ApiConfig
import com.example.githubuser.api.DetailUserResponse
import com.example.githubuser.database.Favorite
import com.example.githubuser.repository.FavoriteRepository
import retrofit2.Call
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel()   {

    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun insert(favorite: Favorite) {
        mFavoriteRepository.insert(favorite)
    }

    fun delete(favorite: Favorite) {
        mFavoriteRepository.delete(favorite)
    }

    fun getByUsername(username:String) = mFavoriteRepository.getByUsername(username)

    companion object {
        private const val TAG = "DetailActivity"
    }


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _detailUser  = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    fun getDetailUser(username: String){
        _isLoading.value = true
        val detailClient = ApiConfig.getApiService().getDetailUser(username)
        detailClient.enqueue(object : retrofit2.Callback<DetailUserResponse> {
            override fun onResponse(call: Call<DetailUserResponse>, response: Response<DetailUserResponse>) {
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    Log.e(MainActivity.TAG, "onFailure: ${response.message()}")
                }
                _isLoading.value = false

            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(DetailViewModel.TAG, "onFailure: ${t.message}")
            }
        })
    }
}