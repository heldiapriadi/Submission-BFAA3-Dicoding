package com.example.githubuser.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.adapter.SectionsPagerAdapter
import com.example.githubuser.data.remote.GithubUsersConfig
import com.example.githubuser.databinding.ActivityDetailUserBinding
import com.example.githubuser.helper.DetailUserViewModelFactory
import com.example.githubuser.model.User
import com.example.githubuser.utils.Result
import com.example.githubuser.viewmodels.DetailUserViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailUserViewModel: DetailUserViewModel
    private var username: String = ""
    private lateinit var myUser: User
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        username = intent.getStringExtra(EXTRA_USER) as String

        detailUserViewModel =
            ViewModelProvider(
                this,
                DetailUserViewModelFactory(GithubUsersConfig.provideUserRepository(this), username)
            ).get(DetailUserViewModel::class.java)

        init()
        subscribeUi()
    }

    private fun init() {
        val tabList = arrayOf(
            resources.getString(R.string.followers),
            resources.getString(R.string.following)
        )


        val sectionsPagerAdapter = SectionsPagerAdapter(this, username, tabList)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = tabList[position]
        }.attach()

        binding.btnBackDetail.setOnClickListener {
            finish()
        }

        binding.ibBtnFavorite.setOnClickListener {
            onClickFavorite()
        }
    }

    private fun subscribeUi() {
        detailUserViewModel.user.observe(this, { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    result.data.let { user ->
                        if (user != null) {
                            myUser = user
                        }
                        Glide.with(binding.ivAvatar.context)
                            .load(user?.avatarUrl)
                            .into(binding.ivAvatar)

                        if (!user?.name.isNullOrEmpty()) {
                            binding.tvName.text = user?.name
                        } else {
                            binding.tvName.text = resources.getString(R.string.blank)
                        }

                        binding.apply {
                            tvFollower.text = user?.followers.toString()
                            tvFollowing.text = user?.following.toString()
                            tvRepository.text = user?.publicRepos.toString()
                            tvUsername.text = user?.login
                            tvLocation.text = user?.location
                        }
                    }
                    showLoading(false)
                }
                Result.Status.ERROR -> {
                    result.message?.let {
                        showError(it)
                    }
                    showLoading(false)
                }

                Result.Status.LOADING -> {
                    showLoading(true)
                }
            }
        })

        detailUserViewModel.isFavorite.observe(this, {
            isFavorite = it
            showFavorite()
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showError(isError: String) {
        Snackbar.make(binding.detailUserParent, isError, Snackbar.LENGTH_INDEFINITE)
            .setAction("DISMISS") {
            }.show()
    }

    private fun showFavorite() {
        if (isFavorite) {
            binding.ibBtnFavorite.setImageResource(R.drawable.ic_icons8_favorites_filled)
        } else {
            binding.ibBtnFavorite.setImageResource(R.drawable.ic_icons8_favorites)
        }
    }

    private fun onClickFavorite() {
        if (!isFavorite) {
            detailUserViewModel.addFavorite(myUser)
            Snackbar.make(
                binding.detailUserParent,
                resources.getString(R.string.add_favorite),
                Snackbar.LENGTH_INDEFINITE
            ).setDuration(Snackbar.LENGTH_SHORT).setAction("OK") {
            }.show()
        } else {
            detailUserViewModel.removeFavorite(myUser)
            Snackbar.make(
                binding.detailUserParent,
                resources.getString(R.string.remove_favorite),
                Snackbar.LENGTH_INDEFINITE
            ).setDuration(Snackbar.LENGTH_SHORT).setAction("OK") {
            }.show()
        }
    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }

}