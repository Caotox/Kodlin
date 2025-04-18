package com.example.monappli

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(40, 40, 40, 40)

            val btnMinuteur = Button(context).apply {
                text = "Minuteur"
                setOnClickListener {
                    findNavController().navigate("minuteur")
                }
            }
            addView(btnMinuteur)
            val btnCalculette = Button(context).apply {
                text = "Calculette"
                setOnClickListener {
                    findNavController().navigate("calculette")
                }
            }
            addView(btnCalculette)

            val btnChrono = Button(context).apply {
                text = "Chronomètre"
                setOnClickListener {
                    findNavController().navigate("chrono")
                }
            }
            addView(btnChrono)
        }
        return layout
    }
}
