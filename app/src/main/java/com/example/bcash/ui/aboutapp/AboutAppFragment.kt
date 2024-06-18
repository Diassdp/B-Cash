package com.example.bcash.ui.aboutapp

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.bcash.R

class AboutAppFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Handle fragment arguments if necessary
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_about_app, container, false)

        // Find the TextView and set it to be clickable
        val tvRepo: TextView = view.findViewById(R.id.tv_repo)
        Linkify.addLinks(tvRepo, Linkify.WEB_URLS)
        tvRepo.movementMethod = LinkMovementMethod.getInstance()

        return view
    }

    companion object {
        // Define any constants or methods for the fragment if necessary
    }
}
