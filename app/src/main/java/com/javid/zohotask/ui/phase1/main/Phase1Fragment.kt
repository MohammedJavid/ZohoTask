package com.javid.zohotask.ui.phase1.main

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.javid.zohotask.R
import com.javid.zohotask.data.model.modelclass.paging.Result
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
    private var textWatcher: TextWatcher? = null
    private var dataList = ArrayList<Result>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhase1Binding.inflate(inflater, container, false)
        setPagingAdapter()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.ivSearch.setOnClickListener {
            if (!searchFlag) {
                setLocalAdapter()
                setTextWatcher()
                binding.etSearchText.visibility = View.VISIBLE
                binding.tvHeader.visibility = View.GONE
                binding.ivSearch.setImageDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.ic_close))
                binding.etSearchText.addTextChangedListener(textWatcher)
            } else {
                setPagingAdapter()
                binding.etSearchText.visibility = View.GONE
                binding.tvHeader.visibility = View.VISIBLE
                binding.ivSearch.setImageDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.ic_search))
                binding.etSearchText.removeTextChangedListener(textWatcher)
                val imm: InputMethodManager? =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.hideSoftInputFromWindow(binding.etSearchText.windowToken, 0)
                binding.etSearchText.setText("")
            }
            searchFlag = !searchFlag
        }
    }

    private fun setPagingAdapter() {
        adapter = ResultListAdapter(
            context = requireActivity(),
            clickListener = {result: Result -> navigateToDetail(result) },
            addToDatabase = {result: Result -> addToDatabase(result) },
            getFirstWeatherData = null
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

        lifecycleScope.launch {
            phase1ViewModel.pagingResult?.distinctUntilChanged()?.collectLatest {
                adapter.submitData(it)
            }
        }
        binding.ivSearch.isClickable = true
    }

    private fun setLocalAdapter() {
        localAdapter = LocalListAdapter(
            context = requireActivity(),
            clickListener = {result: Result -> navigateToDetail(result) }
        )
        binding.rclrViewItems.adapter = localAdapter
        phase1ViewModel.localData.observe(viewLifecycleOwner) {
            dataList.clear()
            dataList.addAll(it)
            localAdapter.setDataList(it)
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
    }

    private fun navigateToDetail(result: Result) {
        findNavController().navigate(
            Phase1FragmentDirections.actionPhase1FragmentToDetailFragment(email = result.email))
    }

    private fun addToDatabase(result: Result) {
        phase1ViewModel.addToDatabase(result)
    }

    private fun setTextWatcher() {
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun afterTextChanged(text: Editable?) {
                setSearchList(text.toString())
            }
        }
    }

    private fun setSearchList(text: String) {
        val tempDataList = ArrayList<Result>()
        for (item in dataList) {
            item.name?.let {
                if (it.first?.lowercase()!!.contains(text.lowercase())
                    || it.last?.lowercase()!!.contains(text.lowercase())) {
                    tempDataList.add(item)
                }
            }
        }
        localAdapter.setDataList(tempDataList)
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

}