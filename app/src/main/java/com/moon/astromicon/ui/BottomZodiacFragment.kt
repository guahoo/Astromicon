package com.moon.astromicon.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.moon.astromicon.R

class BottomZodiacFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance() = BottomZodiacFragment()
    }

    private lateinit var viewModel: BottomZodiacViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_zodiac_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BottomZodiacViewModel::class.java)
        setListenerDialogState()
    }

    private fun setListenerDialogState() {
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        if (dialog is BottomSheetDialog) {
            val bdialog = dialog as BottomSheetDialog
            bdialog.behavior.halfExpandedRatio = 0.15f

            bdialog.behavior.isFitToContents = false
            bdialog.behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
    }

}