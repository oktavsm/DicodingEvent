package com.dicoding.dicodingevent.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.data.response.Event
import com.dicoding.dicodingevent.databinding.ActivityDetailBinding
import com.dicoding.dicodingevent.ui.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private var currentEvent: Event? = null
    private var isFavorite = false

    companion object {
        const val EXTRA_ID = "EVENT_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        val eventId = intent.getIntExtra(EXTRA_ID, 0).toString()
        viewModel.getDetailEvent(eventId)

        viewModel.eventDetail.observe(this) { event ->
            currentEvent = event
            populateDetail(event)

            // Favorite status
            viewModel.getFavoriteById(event.id?.toString() ?: "").observe(this) { fav ->
                isFavorite = fav != null
                updateFavoriteIcon(isFavorite)
            }
        }

        viewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }

        binding.fabFavorite.setOnClickListener {
            currentEvent?.let { viewModel.toggleFavorite(it, isFavorite) }
        }

        binding.btnBack.setOnClickListener { finish() }
    }

    private fun populateDetail(event: Event) {
        binding.apply {
            tvDetailName.text = event.name
            tvDetailOwner.text = event.ownerName
            tvDetailTime.text = event.beginTime
            tvDetailQuota.text = getString(R.string.quota_remaining, (event.quota ?: 0) - (event.registrants ?: 0))
            tvDetailDescription.text = HtmlCompat.fromHtml(
                event.description ?: "",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            tvDetailCategory.text = event.category

            Glide.with(this@DetailActivity).load(event.mediaCover).into(ivDetailImage)

            btnRegister.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, event.link?.toUri()))
            }
        }
    }

    private fun updateFavoriteIcon(isFav: Boolean) {
        val icon = if (isFav) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
        binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, icon))
    }
}
