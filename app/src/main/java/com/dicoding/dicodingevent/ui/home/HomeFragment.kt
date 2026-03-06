package com.dicoding.dicodingevent.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.databinding.FragmentHomeBinding
import com.dicoding.dicodingevent.ui.ViewModelFactory
import com.dicoding.dicodingevent.ui.detail.DetailActivity
import com.dicoding.dicodingevent.ui.adapter.CarouselAdapter
import com.dicoding.dicodingevent.ui.adapter.EventAdapter
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        val viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        // Carousel upcoming
        val carouselAdapter = CarouselAdapter { event ->
            startActivity(Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_ID, event.id)
            })
        }
        binding.rvUpcomingHome.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = carouselAdapter
        }

        // Finished list
        val finishedAdapter = EventAdapter { event ->
            startActivity(Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_ID, event.id)
            })
        }
        binding.rvFinishedHome.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = finishedAdapter
            isNestedScrollingEnabled = false
        }

        viewModel.listUpcoming.observe(viewLifecycleOwner) { carouselAdapter.submitList(it) }
        viewModel.listFinished.observe(viewLifecycleOwner) { finishedAdapter.submitList(it) }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }

        // Search bar
        binding.searchView.setupWithSearchBar(binding.searchBar)
        binding.searchView.editText.setOnEditorActionListener { _, _, _ ->
            val query = binding.searchView.text.toString()
            if (query.isNotEmpty()) {
                binding.searchView.hide()
                startActivity(Intent(requireContext(),
                    com.dicoding.dicodingevent.ui.search.SearchResultActivity::class.java).apply {
                    putExtra(com.dicoding.dicodingevent.ui.search.SearchResultActivity.EXTRA_QUERY, query)
                })
            }
            false
        }

        // See all buttons
        val bottomNav = requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.nav_view)
        binding.tvSeeAllUpcoming.setOnClickListener {
            bottomNav.selectedItemId = R.id.navigation_upcoming
        }
        binding.tvSeeAllFinished.setOnClickListener {
            bottomNav.selectedItemId = R.id.navigation_finished
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
