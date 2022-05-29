package com.javid.zohotask.ui.phase1.detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.javid.zohotask.R
import com.javid.zohotask.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()
    private val navArgs: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        getDataByEmail(navArgs.email)
        return binding.root
    }

    private fun getDataByEmail(email: String) {
        lifecycleScope.launch {
            detailViewModel.getDataByEmail(email).observe(viewLifecycleOwner) {
                Log.d("getDataByEmail: ", it.toString())
                binding.tvFullNameText.text = it.name?.title
                    .plus(". ")
                    .plus(it.name?.first)
                    .plus(" ")
                    .plus(it.name?.last)
                binding.tvEmailText.text = it.email
                binding.tvPhoneText.text = it.phone ?: "-"
                binding.tvDobText.text = it.dob?.date?.let { it1 -> getFormattedDate(it1) }
                binding.tvAgeText.text = it.dob?.age.toString()
                binding.tvAddressText.text = it.location?.street?.number.toString()
                    .plus(", ")
                    .plus(it.location?.street?.name)
                    .plus(", ")
                    .plus(it.location?.city)
                    .plus(", ")
                    .plus(it.location?.postcode)
                    .plus(", ")
                    .plus(it.location?.country)

                it?.picture?.let { picture ->
                    Glide.with(requireActivity())
                        .load(picture.large)
                        .error(com.google.android.material.R.drawable.mtrl_ic_error)
                        .listener(
                            object : RequestListener<Drawable> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    binding.progressBarImg.visibility = View.GONE
                                    return false
                                }

                                override fun onResourceReady(
                                    resource: Drawable?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    dataSource: DataSource?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    binding.progressBarImg.visibility = View.GONE
                                    return false
                                }

                            }
                        ).into(binding.ivProfileImg)
                }
            }
        }
    }

    private fun getFormattedDate(timeStamp: String): String {
        return try {
            val apiFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val  uiFormat = SimpleDateFormat("dd MMM yyyy")
            uiFormat.format(
                Date(
                    apiFormat.parse(timeStamp)!!.time
                )
            )
        } catch (e: Exception) {
            "-"
        }
    }

}