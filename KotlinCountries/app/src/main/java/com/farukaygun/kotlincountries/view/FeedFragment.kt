package com.farukaygun.kotlincountries.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.farukaygun.kotlincountries.adapter.recyclerViewCountriesAdapter
import com.farukaygun.kotlincountries.databinding.FragmentFeedBinding
import com.farukaygun.kotlincountries.model.Country
import com.farukaygun.kotlincountries.viewmodel.FeedViewModel


class FeedFragment : Fragment() {
    private lateinit var binding: FragmentFeedBinding
    private lateinit var viewModel: FeedViewModel
    private val countryAdapter = recyclerViewCountriesAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(FeedViewModel::class.java)
        viewModel.refreshData()

        binding.recyclerViewCountries.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewCountries.adapter = countryAdapter

        binding.swipeRefreshLayout.setOnRefreshListener { refresh() }

        observeLiveData()
    }

    private fun refresh() {
        binding.recyclerViewCountries.visibility = View.GONE
        binding.textViewError.visibility = View.GONE
        binding.progressBarCountryLoading.visibility = View.VISIBLE
        viewModel.refreshData()
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun observeLiveData() {
        viewModel.countries.observe(viewLifecycleOwner, Observer { countries ->
            countries?.let {
                binding.recyclerViewCountries.visibility = View.VISIBLE
                countryAdapter.updateCountryList(countries)
            }
        })

        viewModel.countryError.observe(viewLifecycleOwner) { error ->
            error?.let {
                if (it) {
                    binding.textViewError.visibility = View.VISIBLE
                } else {
                    binding.textViewError.visibility = View.GONE
                }
            }
        }

        viewModel.countryLoading.observe(viewLifecycleOwner) { loading ->
            loading?.let {
                if (it) {
                    binding.progressBarCountryLoading.visibility = View.VISIBLE
                    binding.textViewError.visibility = View.GONE
                    binding.recyclerViewCountries.visibility = View.GONE
                } else {
                    binding.progressBarCountryLoading.visibility = View.GONE
                }
            }
        }
    }
}