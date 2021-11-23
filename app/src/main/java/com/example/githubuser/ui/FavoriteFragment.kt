package com.example.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.data.remote.GithubUsersConfig
import com.example.githubuser.databinding.FragmentFavoriteBinding
import com.example.githubuser.helper.FavoriteViewModelFactory
import com.example.githubuser.model.User
import com.example.githubuser.utils.Result
import com.example.githubuser.viewmodels.FavoriteViewModel
import com.google.android.material.snackbar.Snackbar

class FavoriteFragment : Fragment() {

    private val favoriteViewModel: FavoriteViewModel by navGraphViewModels(R.id.mobile_navigation) {
        FavoriteViewModelFactory(GithubUsersConfig.provideUserRepository(requireContext()))
    }
    private var _binding: FragmentFavoriteBinding? = null
    private lateinit var favoriteAdapter: UserAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        subscribeUI()
    }

    private fun init() {
        favoriteAdapter = UserAdapter(arrayListOf())
        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = favoriteAdapter
        }
    }

    private fun subscribeUI() {
        favoriteViewModel.favoriteUsers.observe(viewLifecycleOwner, { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    result.data?.let { list ->
                        list.observe(viewLifecycleOwner, {
                            showRecyclerList(it)
                        })
                    }
                    showLoading(false)
                }

                Result.Status.LOADING -> {
                    result.message?.let {
                        showError(it)
                    }
                    showLoading(false)
                }

                Result.Status.ERROR -> {
                    showLoading(true)
                }

            }
        })
    }

    private fun showRecyclerList(list: List<User>) {
        favoriteAdapter.updateData(ArrayList(list))
        favoriteAdapter.setOnItemClickCallback(object :
            UserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User?) {
                showSelecterUser(user)
            }
        })
        binding.tvTotalResult.text =
            resources.getQuantityString(R.plurals.result_favorites, list.size, list.size)
        if (list.isNotEmpty()) {
            binding.tvUserNotFound.visibility = View.GONE
            binding.ivUserNotFound.visibility = View.GONE
            binding.tvTotalResult.visibility = View.VISIBLE
        } else {
            binding.tvUserNotFound.visibility = View.VISIBLE
            binding.ivUserNotFound.visibility = View.VISIBLE
            binding.tvTotalResult.visibility = View.GONE
        }

    }

    private fun showError(isError: String) {
        Snackbar.make(binding.favoriteParent, isError, Snackbar.LENGTH_INDEFINITE)
            .setAction("DISMISS") {
            }.show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE

        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showSelecterUser(user: User?) {
        val intent = Intent(activity, DetailUserActivity::class.java)
        intent.putExtra(DetailUserActivity.EXTRA_USER, user?.login)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}