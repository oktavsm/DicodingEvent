package com.dicoding.dicodingevent.ui


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.dicodingevent.databinding.ActivitySearchResultBinding
import com.google.android.material.tabs.TabLayoutMediator


class SearchResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchResultBinding
    private var currentQuery: String = ""
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
        val sectionsPagerAdapter = SectionsPagerAdapter(this, query)
        binding.viewPager.adapter= sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) {tab, position ->
            tab.text = if (position == 0) "Upcoming" else "Finised"
        }.attach()
    }

    private fun setupSearchListener() {
        binding.apply {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setText(currentQuery)

            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val newQuery = searchView.text.toString()
                if (newQuery.isNotEmpty()) {
                    searchBar.setText(newQuery)
                    searchView.hide()
                    setupViewPager(newQuery)
                } else {
                    finish()
                }
                false
            }
        }
    }


    class SectionsPagerAdapter(activity: AppCompatActivity, private val query: String): FragmentStateAdapter(activity){
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): androidx.fragment.app.Fragment {
            return SearchResultFragment.newInstance(position, query)
        }
    }

    companion object{
        const val EXTRA_QUERY = "extra_query"
    }

}