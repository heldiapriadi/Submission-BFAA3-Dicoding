package com.example.githubuser.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.ListFollowAdapter
import com.example.githubuser.data.remote.GithubUsersConfig
import com.example.githubuser.databinding.FragmentFollowBinding
import com.example.githubuser.helper.FollowListViewModelFactory
import com.example.githubuser.model.User
import com.example.githubuser.utils.Result
import com.example.githubuser.utils.TypeFollow
import com.example.githubuser.viewmodels.FollowListViewModel
import com.google.android.material.snackbar.Snackbar

private const val ARG_PARAM1 = "username"
private const val ARG_PARAM2 = "type"

class FollowFragment : Fragment() {
    private lateinit var binding: FragmentFollowBinding

    private lateinit var username: String
    private lateinit var typeFollow: String
    private lateinit var followList: FollowListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(ARG_PARAM1)!!
            typeFollow = it.getString(ARG_PARAM2)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        subscribeUi()
    }

    private fun init() {
        binding.rvFollower.layoutManager = LinearLayoutManager(activity)

        when (typeFollow) {
            resources.getString(R.string.followers) -> {
                followList = ViewModelProvider(
                    this,
                    FollowListViewModelFactory(
                        GithubUsersConfig.provideUserRepository(
                            requireContext()
                        ), username, TypeFollow.FOLLOWER
                    )
                ).get(FollowListViewModel::class.java)
                binding.notFound.text = resources.getString(R.string.follower_notfound)
            }
            resources.getString(R.string.following) -> {
                followList = ViewModelProvider(
                    this,
                    FollowListViewModelFactory(
                        GithubUsersConfig.provideUserRepository(
                            requireContext()
                        ), username, TypeFollow.FOLLOWING
                    )
                ).get(FollowListViewModel::class.java)
                binding.notFound.text = resources.getString(R.string.following_notfound)
            }
        }
    }

    private fun subscribeUi() {
        followList.listUser.observe(viewLifecycleOwner, { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    showRecyclerList(result.data)
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


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showError(isError: String) {
        Snackbar.make(binding.followParent, isError, Snackbar.LENGTH_INDEFINITE)
            .setAction("DISMISS") {
            }.show()
    }


    private fun showRecyclerList(list: List<User?>?) {
        if (list != null) {
            if (list.isNotEmpty()) {
                val listFollower = ListFollowAdapter(list)
                binding.rvFollower.adapter = listFollower

                listFollower.setOnItemClickCallback(object :
                    ListFollowAdapter.OnItemClickCallback {
                    override fun onItemClicked(user: User?) {
                        showSelecterUser(user)
                    }
                })
                binding.notFound.visibility = View.GONE
            } else {
                binding.notFound.visibility = View.VISIBLE
            }
        }

    }

    private fun showSelecterUser(userFollow: User?) {
        val intent = activity?.intent
        intent?.putExtra(DetailUserActivity.EXTRA_USER, userFollow?.login)
        startActivity(intent)
    }


    companion object {
        @JvmStatic
        fun newInstance(username: String, type: String) =
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, username)
                    putString(ARG_PARAM2, type)
                }
            }
    }
}