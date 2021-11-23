package com.example.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.RenderMode
import com.example.githubuser.R
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.data.remote.GithubUsersConfig
import com.example.githubuser.databinding.FragmentHomeBinding
import com.example.githubuser.helper.HomeViewModelFactory
import com.example.githubuser.model.User
import com.example.githubuser.utils.Result
import com.example.githubuser.viewmodels.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.mobile_navigation) {
        HomeViewModelFactory(GithubUsersConfig.provideUserRepository(requireContext()))
    }
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchView: SearchView = binding.searchGit
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    homeViewModel.findGithubUser(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        init()
        subscribeUi()
    }

    private fun init() {
        binding.progressBar.setRenderMode(RenderMode.SOFTWARE)
        userAdapter = UserAdapter(arrayListOf())
        binding.rvUsers.apply {
            this.layoutManager = LinearLayoutManager(activity)
            adapter = userAdapter
        }

        binding.btnSettings.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_navigation_home_to_settingActivity)
        }

    }


    private fun subscribeUi() {
        homeViewModel.searchResult.observe(viewLifecycleOwner, { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    result.data?.totalCount?.let { totalCount ->
                        setTotalResult(totalCount)
                    }
                    result.data?.items?.let { list ->
                        showRecyclerList(list)
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setTotalResult(totalCount: Int?) {
        binding.tvResult.text = getString(R.string.result_search, totalCount)
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.rvUsers.visibility = View.GONE
            binding.progressBar.playAnimation()
            binding.progressBar.visibility = View.VISIBLE
            binding.searchNotfound.visibility = View.GONE
            binding.ivSearch.visibility = View.GONE
        } else {
            binding.rvUsers.visibility = View.VISIBLE
            binding.progressBar.cancelAnimation()
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showError(isError: String) {
        Snackbar.make(binding.homeParent, isError, Snackbar.LENGTH_INDEFINITE)
            .setAction("DISMISS") {
            }.show()
    }

    private fun showRecyclerList(list: ArrayList<User?>?) {
        userAdapter.updateData(list)
        userAdapter.setOnItemClickCallback(object :
            UserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User?) {
                showSelecterUser(user)
            }
        })
        if (list?.isNotEmpty() == true) {
            binding.searchNotfound.visibility = View.GONE
            binding.ivSearch.visibility = View.GONE
        } else {
            binding.searchNotfound.visibility = View.VISIBLE
            binding.ivSearch.visibility = View.VISIBLE
        }

    }

    private fun showSelecterUser(user: User?) {
        val intent = Intent(activity, DetailUserActivity::class.java)
        intent.putExtra(DetailUserActivity.EXTRA_USER, user?.login)
        startActivity(intent)

    }

}