package com.farukaygun.kotlincountries.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.farukaygun.kotlincountries.R
import com.farukaygun.kotlincountries.databinding.FragmentCountryDetailBinding
import com.farukaygun.kotlincountries.databinding.FragmentFeedBinding
import com.farukaygun.kotlincountries.viewmodel.CountryDetailViewModel


class CountryDetailFragment : Fragment() {
    private lateinit var binding: FragmentCountryDetailBinding
    private lateinit var viewModel: CountryDetailViewModel
    private var countryId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCountryDetailBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(CountryDetailViewModel::class.java)
        viewModel.getDataFromRoom()

        arguments?.let {
            countryId = CountryDetailFragmentArgs.fromBundle(it).countryId
        }

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.countryLiveData.observe(viewLifecycleOwner) { country ->
            country?.let {
                binding.textViewCountryName.text = country.countryName
                binding.textViewCountryCapital.text = country.countryCapital
                binding.textViewCountryCurrency.text = country.countryCurrency
                binding.textViewCountryLanguage.text = country.countryLanguage
                binding.textViewCountryRegion.text = country.countryRegion
            }
        }
    }
}