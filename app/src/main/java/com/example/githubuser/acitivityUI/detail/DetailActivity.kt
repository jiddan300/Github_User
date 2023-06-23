package com.example.githubuser.acitivityUI.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.api.DetailUserResponse
import com.example.githubuser.R
import com.example.githubuser.acitivityUI.SectionsPagerAdapter
import com.example.githubuser.database.Favorite
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.helper.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val user = Favorite()

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        title = "Detail User"

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("key_user").toString()

        getListFollow(username)

        val detailViewModel = obtainViewModel(this@DetailActivity)

        detailViewModel.getDetailUser(username)

        detailViewModel.detailUser.observe(this){
            setDetailUser(it)
        }


        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }


        detailViewModel.getByUsername(username).observe(this) { List ->
            if (List != null) {
                user.id = List.id
                binding.favoriteFab.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_baseline_favorite,theme))
                binding.favoriteFab.setOnClickListener{
                    detailViewModel.delete(user)
                    Toast.makeText(applicationContext,"Removed from Favorite",Toast.LENGTH_SHORT).show()

                }
            }
            else {
                binding.favoriteFab.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_baseline_favorite_border, theme))
                binding.favoriteFab.setOnClickListener{
                    detailViewModel.insert(user)
                    Toast.makeText(applicationContext,"Added to Favorite",Toast.LENGTH_SHORT).show()

                }
            }
        }


    }

    private fun setDetailUser(it: DetailUserResponse?) {
        if(it != null){
            user.let{ user ->
                user.username = it.login
                user.avatarUrl = it.avatarUrl
            }

            Glide.with(this@DetailActivity)
                .load(it.avatarUrl)
                .into(binding.detailAvatar)

            binding.detailUsername.text = it.name
            binding.detailId.text = it.login
            binding.detailFollowers.text = String.format(getString(R.string.follower), it.followers)
            binding.detailFollowing.text = String.format(getString(R.string.following), it.following)
        }

    }

    private fun getListFollow(username: String) {

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun showLoading(isLoading: Boolean) : Boolean {
        if (isLoading) {
            binding.detailProgressBar.visibility = View.VISIBLE
            return true
        } else {
            binding.detailProgressBar.visibility = View.GONE
            return false
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }
}