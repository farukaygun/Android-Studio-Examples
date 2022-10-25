package com.farukaygun.kotlinartbookwithfragments.view

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import androidx.room.Room
import com.farukaygun.kotlinartbookwithfragments.R
import com.farukaygun.kotlinartbookwithfragments.databinding.FragmentArtDetailsBinding
import com.farukaygun.kotlinartbookwithfragments.model.Art
import com.farukaygun.kotlinartbookwithfragments.roomdb.ArtDao
import com.farukaygun.kotlinartbookwithfragments.roomdb.ArtDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.ByteArrayOutputStream



class ArtDetailsFragment : Fragment() {
    var selectedBitmap: Bitmap? = null
    private lateinit var binding : FragmentArtDetailsBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var artDatabase : ArtDatabase
    private lateinit var artDao : ArtDao
    private val mDisposable = CompositeDisposable()
    var artFromMain : Art? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncher()

        artDatabase = Room.databaseBuilder(requireContext(), ArtDatabase::class.java, "Art").build()
        artDao = artDatabase.artDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArtDetailsBinding.inflate(inflater, container, false)

        return binding.root
        //return inflater.inflate(R.layout.fragment_art_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView.setOnClickListener { imageViewClicked(view) }
        binding.buttonSave.setOnClickListener { saveButtonClicked(view) }

        disableMenu()

        arguments?.let {

            val info = ArtDetailsFragmentArgs.fromBundle(it).info
            if (info.equals("new")) {
                //NEW
                binding.editTextArtName.setText("")
                binding.editTextArtistName.setText("")
                binding.editTextYear.setText("")
                binding.buttonSave.visibility = View.VISIBLE

                val selectedImageBackground = BitmapFactory.decodeResource(context?.resources,
                    R.drawable.selectimage
                )
                binding.imageView.setImageBitmap(selectedImageBackground)

            } else {
                //OLD
                binding.buttonSave.visibility = View.GONE

                val selectedId = ArtDetailsFragmentArgs.fromBundle(it).id
                mDisposable.add(artDao.getArtById(selectedId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponseWithOldArt))

            }
        }
    }

    private fun handleResponseWithOldArt(art : Art) {
        artFromMain = art
        binding.editTextArtName.setText(art.name)
        binding.editTextArtistName.setText(art.artist)
        binding.editTextYear.setText(art.year)
        art.image?.let {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            binding.imageView.setImageBitmap(bitmap)
        }
    }

    // disable menu in this fragment
    private fun disableMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.setGroupVisible(0, false)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun saveButtonClicked(view: View) {
        val artName = binding.editTextArtName.text.toString()
        val artistName = binding.editTextArtistName.text.toString()
        val year = binding.editTextYear.text.toString()

        val smallBitmap = makeSmallerBitmap(selectedBitmap!!, 300)

        val outputStream = ByteArrayOutputStream()
        smallBitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream)
        val byteArray = outputStream.toByteArray()

        val art = Art(artName, artistName, year, byteArray)

        mDisposable.add(artDao.insert(art)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleResponse))
    }

    private fun handleResponse() {
        val action = ArtDetailsFragmentDirections.actionArtDetailsFragmentToArtListFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun makeSmallerBitmap(image: Bitmap, maximumSize: Int): Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio : Double = width.toDouble() / height.toDouble()
        if (bitmapRatio > 1) {
            width = maximumSize
            val scaledHeight = width / bitmapRatio
            height = scaledHeight.toInt()
        } else {
            height = maximumSize
            val scaledWidth = height * bitmapRatio
            width = scaledWidth.toInt()
        }

        return Bitmap.createScaledBitmap(image,width,height,true)
    }

    private fun imageViewClicked(view: View) {
        // check does permission granted
        if (ContextCompat.checkSelfPermission(view.context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // if permission doesn't granted request permission.
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            // if permission granted
            val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
        }
    }

    private fun registerLauncher() {
        activityResultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
            // did user access to gallery?
            if (result.resultCode == RESULT_OK) {
                // get data
                val intentFromResult = result.data
                if (intentFromResult != null) { // if user select an image
                    val imageData = intentFromResult.data
                    try {

                        if (Build.VERSION.SDK_INT >= 28) {
                            val source = activity?.let { ImageDecoder.createSource(it.contentResolver, imageData!!) }
                            selectedBitmap = ImageDecoder.decodeBitmap(source!!)
                            binding.imageView.setImageBitmap(selectedBitmap)
                        } else {
                            selectedBitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, imageData)
                            binding.imageView.setImageBitmap(selectedBitmap)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            // if result -> permission granted open the gallery
            if (result) {
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            } else {
                Toast.makeText(context,"Permisson needed!", Toast.LENGTH_LONG).show()
            }
        }
    }
}