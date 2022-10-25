package com.farukaygun.kotlinartbookwithfragments.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.farukaygun.kotlinartbookwithfragments.adapter.ArtListAdapter
import com.farukaygun.kotlinartbookwithfragments.databinding.FragmentArtListBinding
import com.farukaygun.kotlinartbookwithfragments.model.Art
import com.farukaygun.kotlinartbookwithfragments.roomdb.ArtDao
import com.farukaygun.kotlinartbookwithfragments.roomdb.ArtDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class ArtListFragment : Fragment() {
    private lateinit var artAdapter : ArtListAdapter
    private var _binding: FragmentArtListBinding? = null
    private val binding get() = _binding!!
    private val mDisposable = CompositeDisposable()
    private lateinit var artDao : ArtDao
    private lateinit var artDatabase : ArtDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        artDatabase = Room.databaseBuilder(requireContext(), ArtDatabase::class.java, "Art").build()
        artDao = artDatabase.artDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArtListBinding.inflate(layoutInflater,container,false)

        return binding.root
        //return inflater.inflate(R.layout.fragment_art_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getFromSQL()
    }

    private fun getFromSQL() {
        mDisposable.add(artDao.getArtWithNameAndId()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleResponse))
    }

    private fun handleResponse(artList: List<Art>) {
        binding.recyclerViewArtList.layoutManager = LinearLayoutManager(requireContext())
        artAdapter = ArtListAdapter(artList)
        binding.recyclerViewArtList.adapter = artAdapter
    }
}