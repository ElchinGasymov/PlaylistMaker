package com.example.playlistmaker.media_library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.playlistmaker.databinding.FragmentFavouriteTracksBinding
import com.example.playlistmaker.media_library.ui.view_model.LibraryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteTracksFragment : Fragment() {

    private val binding: FragmentFavouriteTracksBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel by viewModel<LibraryViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    companion object {
        fun newInstance() = FavouriteTracksFragment()
    }

}
