package com.dicoding.dicodingevent.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.databinding.FragmentFinishedBinding
import com.dicoding.dicodingevent.databinding.FragmentSearchResultBinding
import com.google.android.material.snackbar.Snackbar

class SearchResultFragment : Fragment(){

    private var position: Int = 0
    private var query: String = ""

    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            query = it.getString(ARG_QUERY) ?: ""
        }

        val adapter = EventAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("EVENT_ID", event.id)
            startActivity(intent)
        }

        binding.apply {
            rvEvents.layoutManager = LinearLayoutManager(requireContext())
            rvEvents.adapter = adapter
        }
        val viewModel = ViewModelProvider(this)[EventViewModel::class.java]

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.listSearch.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
            if (events.isEmpty()) {
                binding.tvResult.visibility = View.VISIBLE
            } else{
                binding.tvResult.visibility = View.GONE
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { message ->
                Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
            }
        }

        val activeStatus = if (position == 0) 1 else 0
        viewModel.searchEvents(query, activeStatus)
    }

    companion object {
        const val ARG_POSITION = "section_number"
        const val ARG_QUERY = "search_query"

        fun newInstance(position: Int, query: String) =
            SearchResultFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_POSITION, position)
                    putString(ARG_QUERY, query)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}