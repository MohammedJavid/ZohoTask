package com.javid.zohotask.ui.phase1.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.javid.zohotask.R
import com.javid.zohotask.data.model.modelclass.Result
import com.javid.zohotask.databinding.FragmentPhase1Binding
import com.javid.zohotask.ui.adapters.LocalListAdapter
import com.javid.zohotask.ui.adapters.NetworkLoadStateAdapter
import com.javid.zohotask.ui.adapters.ResultListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Phase1Fragment : Fragment() {

    private lateinit var binding: FragmentPhase1Binding
    private val phase1ViewModel: Phase1ViewModel by viewModels()
    private lateinit var adapter: ResultListAdapter
    private lateinit var localAdapter: LocalListAdapter
    private var searchFlag = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhase1Binding.inflate(inflater, container, false)
        setPagingAdapter()
        setLocalData()

        lifecycleScope.launch {
            phase1ViewModel.pagingResult?.distinctUntilChanged()?.collectLatest {
                adapter.submitData(it)
            }
        }

        return binding.root
    }

    private fun setLocalData() {

    }

    override fun onStart() {
        super.onStart()
        binding.ivSearch.setOnClickListener {
            if (!searchFlag) {
                setLocalAdapter()
                binding.etSearchText.visibility = View.VISIBLE
                binding.tvHeader.visibility = View.GONE
                binding.ivSearch.setImageDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.ic_close))
            } else {
                setPagingAdapter()
                binding.etSearchText.visibility = View.GONE
                binding.tvHeader.visibility = View.VISIBLE
                binding.ivSearch.setImageDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.ic_search))
            }
            searchFlag = !searchFlag
        }
    }

    private fun setPagingAdapter() {
        adapter = ResultListAdapter(
            context = requireActivity(),
            clickListener = {result: Result -> navigateToDetail(result) },
            addToDatabase = {result: Result -> addToDatabase(result) }
        )
         binding.rclrViewItems.adapter = adapter.withLoadStateHeaderAndFooter(
             header = NetworkLoadStateAdapter {adapter.retry()},
             footer = NetworkLoadStateAdapter {adapter.retry()}
         )

        adapter.addLoadStateListener { loadState ->
            binding.rclrViewItems.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.clProgressBar.isVisible = loadState.source.refresh is LoadState.Loading

            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error

            if(errorState is LoadState.Error) {
                binding.tvErrorText.text = getString(R.string.error)
                binding.clError.isVisible = true
            }

            if (loadState.source.refresh is LoadState.Loading) {
                binding.clError.isVisible = false
            } else if(loadState.source.refresh is LoadState.Loading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1) {
                binding.tvErrorText.text = getString(R.string.no_data)
                binding.clError.isVisible = true
            } else {
                binding.clError.isVisible = false
            }

        }
    }

    private fun setLocalAdapter() {
        localAdapter = LocalListAdapter(
            context = requireActivity(),
            clickListener = {result: Result -> navigateToDetail(result) }
        )
        binding.rclrViewItems.adapter = localAdapter
        phase1ViewModel.localData.value?.let {
            localAdapter.setDataList(it)
        }
        localAdapter.notifyDataSetChanged()

        if (localAdapter.itemCount < 1) {
            binding.rclrViewItems.visibility = View.GONE
            binding.tvErrorText.text = getString(R.string.no_data)
            binding.clError.isVisible = true
        } else {
            binding.rclrViewItems.visibility = View.VISIBLE
            binding.clError.isVisible = false
        }
    }

    private fun navigateToDetail(result: Result) {
        findNavController().navigate(
            Phase1FragmentDirections.actionPhase1FragmentToDetailFragment(email = result.email))
    }

    private fun addToDatabase(result: Result) {
        phase1ViewModel.addToDatabase(result)
    }

}