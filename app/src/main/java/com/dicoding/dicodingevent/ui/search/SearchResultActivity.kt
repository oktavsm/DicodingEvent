package com.dicoding.dicodingevent.ui.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.databinding.ActivitySearchResultBinding
import com.google.android.material.tabs.TabLayoutMediator

class SearchResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchResultBinding
    private var currentQuery: String = ""

    companion object {
        const val EXTRA_QUERY = "extra_query"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentQuery = intent.getStringExtra(EXTRA_QUERY) ?: ""
        binding.searchBar.setText(currentQuery)
        setupViewPager(currentQuery)
        setupSearchListener()
    }

    private fun setupViewPager(query: String) {
        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2
            override fun createFragment(position: Int) =
                SearchResultFragment.newInstance(position, query)
        }
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = if (position == 0) getString(R.string.upcoming) else getString(R.string.finished)
        }.attach()
    }

    private fun setupSearchListener() {
        binding.searchView.setupWithSearchBar(binding.searchBar)
        binding.searchView.editText.setText(currentQuery)
        binding.searchView.editText.setOnEditorActionListener { _, _, _ ->
            val newQuery = binding.searchView.text.toString()
            if (newQuery.isNotEmpty()) {
                currentQuery = newQuery
                binding.searchBar.setText(newQuery)
                binding.searchView.hide()
                setupViewPager(newQuery)
            } else finish()
            false
        }
    }
}
