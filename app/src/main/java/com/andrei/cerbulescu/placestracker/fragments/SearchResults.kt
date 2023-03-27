package com.andrei.cerbulescu.placestracker.fragments

import android.app.appsearch.SearchResult
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.andrei.cerbulescu.placestracker.R
import com.andrei.cerbulescu.placestracker.data.Place
import com.andrei.cerbulescu.placestracker.databinding.FragmentSearchResultsBinding

class SearchResults : Fragment() {
    val args: SearchResultsArgs by navArgs()
    private lateinit var binding: FragmentSearchResultsBinding
    private lateinit var places: Array<Place>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchResultsBinding.inflate(layoutInflater)

        places = args.places

        return binding.root
    }

    @Composable
    fun ListItem(place: Place){
        var image = BitmapFactory.decodeByteArray(place.image, 0, place.image.size).asImageBitmap()
        Image(bitmap = image, contentDescription = "test")
    }

    @Composable
    fun RecyclerView(places: List<Place>){
        LazyColumn(content = Modifier.padding(vertical = 4.dp)){

        }
    }
}