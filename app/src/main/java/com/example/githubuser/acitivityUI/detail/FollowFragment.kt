package com.example.githubuser.acitivityUI.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.acitivityUI.ListUserAdapter
import com.example.githubuser.api.ApiConfig
import com.example.githubuser.api.ItemsItem
import com.example.githubuser.databinding.FragmentFollowBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private var _binding: FragmentFollowBinding? = null
private val binding get() = _binding!!


private var position: Int? = null
private var username: String? = null

class FollowFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val TAG = "FollowFragment"
        const val ARG_USERNAME = "username"
        const val ARG_POSITION = "position"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollow.addItemDecoration(itemDecoration)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        if (position == 1){
            showLoading(true)
            val client = username?.let { ApiConfig.getApiService().getFollowers(it) }
            getDetailFollow(client)
        }else{
            val client = username?.let { ApiConfig.getApiService().getFollowing(it) }
            getDetailFollow(client)
        }


    }

    private fun getDetailFollow(client: Call<List<ItemsItem>>?) {
        showLoading(true)

        client?.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful) {
                    val listUser = response.body()
                    if (listUser != null) {
                        val adapter = ListUserAdapter(listUser)
                        binding.rvFollow.adapter = adapter
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
                showLoading(false)

            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.followProgressBar.visibility = View.VISIBLE
        } else {
            binding.followProgressBar.visibility = View.GONE
        }

    }
}
