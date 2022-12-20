package com.example.somekekwapp

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.somekekwapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val router = RouterImpl()
    private val navigator = Navigator(this, R.id.fragment_container)
    private var routerJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addBackPressedCallback { router.exit() }

        router.navigateTo(MainScreen())
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        routerJob = router.commandsFlow
            .onEach(navigator::applyCommand)
            .launchIn(lifecycleScope)
    }

    override fun onStop() {
        super.onStop()
        routerJob?.cancel()
    }

}