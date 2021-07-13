package com.example.dietdiary

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import java.io.File
import java.lang.IllegalStateException

private const val ARG_IMAGE = "image"

class PictureDialogFragment :DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_picture, null)

            builder.setView(view)

            val dietPhoto = view.findViewById(R.id.diet_picture) as ImageView

            val photofile = arguments?.getSerializable(ARG_IMAGE) as File
            val bitmap = getScaledBitmap(photofile.path, requireActivity())

            dietPhoto.setImageBitmap(bitmap)

            builder.setNegativeButton(
                "Dismiss",
                DialogInterface.OnClickListener { _, _ -> dialog?.cancel() })

            builder.create()

        } ?: throw  IllegalStateException("Activit cannot be null")

    }

    companion object {
        fun newInstance(photoFile : File): PictureDialogFragment {
            val args = Bundle().apply { putSerializable(ARG_IMAGE, photoFile) }
            return PictureDialogFragment().apply { arguments = args }
        }
    }
}