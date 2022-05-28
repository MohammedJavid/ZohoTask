package com.javid.zohotask.ui.phase2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.javid.zohotask.R
import com.javid.zohotask.data.model.modelclass.paging.Result
import com.javid.zohotask.databinding.FragmentRandomLocationWeatherBinding
import com.javid.zohotask.ui.adapters.NetworkLoadStateAdapter
import com.javid.zohotask.ui.adapters.ResultListAdapter
import com.javid.zohotask.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RandomLocationWeatherFragment : Fragment() {

    private lateinit var binding: FragmentRandomLocationWeatherBinding
    private val phase2ViewModel: Phase2ViewModel by viewModels()
    private lateinit var adapter: ResultListAdapter
    companion object {
        var city: String? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRandomLocationWeatherBinding.inflate(inflater, container, false)
        binding.clMcViewProgressBar.visibility = View.VISIBLE
        binding.clMcViewRandomError.visibility = View.GONE
        binding.clWeatherData.visibility = View.GONE
        setPagingAdapter()
        observeWeatherData()
        return binding.root
    }



    private fun setPagingAdapter() {
        adapter = ResultListAdapter(
            context = requireActivity(),
            clickListener = {result: Result -> getWeatherData(result) },
            addToDatabase = null,
            getFirstWeatherData = {result: Result -> getWeatherData(result)}
        )

        binding.rclrViewUsers.adapter = adapter.withLoadStateHeaderAndFooter(
            header = NetworkLoadStateAdapter {adapter.retry()},
            footer = NetworkLoadStateAdapter {adapter.retry()}
        )

        adapter.addLoadStateListener { loadState ->
            binding.rclrViewUsers.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.clProgressBar.isVisible = loadState.source.refresh is LoadState.Loading

            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error

            if(errorState is LoadState.Error) {
                binding.tvErrorText.text = errorState.error.message
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
            phase2ViewModel.pagingResult?.distinctUntilChanged()?.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun getWeatherData(result: Result) {
        result.location?.city?.let { it ->
            city = it
            phase2ViewModel.getWeatherData(
                url = getString(R.string.weather_url),
                city = it
            )
        }
    }

    private fun observeWeatherData() {
        phase2ViewModel.weatherData.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        binding.clMcViewProgressBar.visibility = View.VISIBLE
                        binding.clMcViewRandomError.visibility = View.GONE
                        binding.clWeatherData.visibility = View.GONE
                    }
                    Status.SUCCESS -> {
                        if (it.data != null) {
                            Log.d("setObservers: ", it.data.toString())
                            val weatherData = it.data
                            binding.tvRandomLocationText.text = city
                            binding.tvTemperatureText.text = weatherData.current?.tempC.toString().plus("\u2103")
                            binding.tvDateText.text = weatherData.current?.lastUpdated
                            binding.tvConditionText.text = weatherData.current?.condition?.text
                            binding.tvHumidityText.text = weatherData.current?.humidity.toString().plus("%")
                            binding.tvWindText.text = weatherData.current?.windKph.toString().plus("Kmph")
                            binding.tvAirQualityText.text = when(weatherData.current?.airQuality?.usEpaIndex) {
                                1 -> { "Good" }
                                2 -> { "Moderate" }
                                3,4,5 -> { "Unhealthy" }
                                else -> { "Hazardous" }
                            }

                            weatherData.current?.condition?.icon?.let { it1 ->
                                Glide.with(requireActivity())
                                    .load("https:".plus(it1))
                                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                    .error(com.google.android.material.R.drawable.mtrl_ic_error)
                                    .into(binding.ivCondition)
                            }
                        }

                        binding.clMcViewProgressBar.visibility = View.GONE
                        binding.clMcViewRandomError.visibility = View.GONE
                        binding.clWeatherData.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        binding.clMcViewProgressBar.visibility = View.GONE
                        binding.clMcViewRandomError.visibility = View.VISIBLE
                        binding.tvMcViewRandomErrorText.text = it.message ?: "Location Not found"
                        binding.clWeatherData.visibility = View.GONE
                    }
                }
            }
        }
    }


}