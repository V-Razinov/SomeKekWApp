package com.example.somekekwapp

import android.os.Bundle
import android.view.View
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.somekekwapp.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding by viewBinding(FragmentMainBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBarLayout.setApplyStatusBarInsetsListener { insets ->
            updatePadding(top = insets.top)
        }

        binding.floatingBtn.setApplyNavBarInsetsListener { margins, _, insets ->
            updateMargins(bottom = margins.bottom + insets.bottom)
        }
    }

    companion object {
        const val TAG = "main_fragment"
    }

}

class MainScreen : Screen {
    override val tag: String = MainFragment.TAG
    override val bundle: Bundle? = null
    override fun create(): Fragment = MainFragment()
}