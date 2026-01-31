package com.dicoding.dicodingevent.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar


class HomeFragment : Fragment(){

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // adapter caraousel
        val upcomingAdapter = CarouselAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("EVENT_ID", event.id)
            startActivity(intent)
        }
        binding.rvUpcomingHome.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = upcomingAdapter
        }

        // adapter finished list
        val finishedAdapter = EventAdapter{ event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("EVENT_ID", event.id)
            startActivity(intent)
        }
        binding.rvFinishedHome.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = finishedAdapter
            isNestedScrollingEnabled = false
        }

        //viewmodel
        val viewModel = ViewModelProvider(this)[EventViewModel::class.java]

        //observe
        viewModel.listUpcoming.observe(viewLifecycleOwner){events ->
            upcomingAdapter.submitList(events.take(5))
        }

        viewModel.listFinished.observe(viewLifecycleOwner) { events ->
            finishedAdapter.submitList(events.take(5))
        }

        viewModel.isLoading.observe(
            viewLifecycleOwner
        ){ isLoading ->
            binding.progressBar.visibility= if(isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { message ->
                Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
            }
        }

        if (viewModel.listUpcoming.value == null) viewModel.getEvents(1)
        if (viewModel.listFinished.value == null) viewModel.getEvents(0)

        //searchbar
        binding.searchView.setupWithSearchBar(binding.searchBar)
        binding.searchView.editText.setOnEditorActionListener { _, _, _ ->
            val query = binding.searchView.text.toString()
            if (query.isNotEmpty()) {
                binding.searchView.hide()


                val intent = Intent(requireContext(), SearchResultActivity::class.java)
                intent.putExtra(SearchResultActivity.EXTRA_QUERY, query)
                startActivity(intent)
            }
            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}