package com.dicoding.dicodingevent.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getIntExtra("EVENT_ID", 0).toString()
        val viewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        viewModel.getDetailEvent(eventId)

        viewModel.eventDetail.observe(this){ event ->

            binding.apply {
                tvDetailName.text = event.name
                tvDetailOwner.text = event.ownerName
                tvDetailTime.text = event.beginTime
            }

            val quota = (event.quota ?: 0) - (event.registrants ?: 0)
            binding.tvDetailQuota.text ="Sisa Kuota: $quota"

            binding.tvDetailDescription.text = HtmlCompat.fromHtml(
                event.description.toString(),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            Glide.with(this).load(event.mediaCover).into(binding.ivDetailImage)

            binding.btnRegister.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(event.link)
                startActivity(intent)
            }
        }

        viewModel.isLoading.observe(this){ isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

    }
}