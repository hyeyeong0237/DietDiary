package com.example.dietdiary

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.io.File
import java.util.*

private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0
private const val REQUEST_PHOTO = 1
private const val ARG_DIET_ID = "diet_Id"
private const val DATE_FORMAT = "yyyy년 M월 d일, E요일"

class DietFragment : Fragment(), DatePickerFragment.Callbacks {

    interface Callbacks{
        fun popCurrentFragment()
    }
    private var callbacks : Callbacks? = null

    private lateinit var diet : Diet
    private lateinit var weight : EditText
    private lateinit var meal : EditText
    private lateinit var exercise : EditText
    private lateinit var dateButton : Button
    private lateinit var water : TextView
    private lateinit var waterPlusButton : ImageButton
    private lateinit var waterMinusButton : ImageButton
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private lateinit var reportButton: Button
    private lateinit var MoodGroup : RadioGroup
    private lateinit var photoButton : ImageButton
    private lateinit var photoView: ImageView
    private lateinit var photoFile : File
    private lateinit var photoUri: Uri
    private val dietDetailViewModel : DietDetailViewModel by lazy {
        ViewModelProvider(this).get(DietDetailViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
        requireActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        diet = Diet()
        val dietID : UUID = arguments?.getSerializable(ARG_DIET_ID) as UUID
        dietDetailViewModel.loadDiet(dietID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diet, container, false)

        weight = view.findViewById(R.id.diet_weight) as EditText
        meal = view.findViewById(R.id.diet_meal) as EditText
        exercise = view.findViewById(R.id.diet_exercise) as EditText
        dateButton = view.findViewById(R.id.date_button) as Button
        water = view.findViewById(R.id.water_cups) as TextView
        waterPlusButton = view.findViewById(R.id.water_plusButton) as ImageButton
        waterMinusButton = view.findViewById(R.id.water_minusButton) as ImageButton
        saveButton = view.findViewById(R.id.save_button) as Button
        deleteButton = view.findViewById(R.id.delete_button) as Button
        reportButton = view.findViewById(R.id.report_button) as Button
        MoodGroup = view.findViewById(R.id.MoodGroup) as RadioGroup
        photoButton = view.findViewById(R.id.diet_imageButton) as ImageButton
        photoView = view.findViewById(R.id.diet_image) as ImageView


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dietDetailViewModel.dietLiveData.observe(
            viewLifecycleOwner,
            Observer { diet ->
                diet?.let {
                    this.diet = diet
                    photoFile = dietDetailViewModel.getPhotoFile(diet)
                    photoUri = FileProvider.getUriForFile(requireContext(), "com.example.dietdiary.fileprovider", photoFile)
                    updateUI()
                }
            }
        )
    }

    override fun onStart() {
        super.onStart()

        photoButton.apply {
            val packageManager: PackageManager = requireActivity().packageManager
            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val resolvedActivity : ResolveInfo? = packageManager.resolveActivity(captureImage, PackageManager.MATCH_DEFAULT_ONLY)
            if(resolvedActivity == null){
                isEnabled = false
            }

            setOnClickListener {
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

                val cameraActivities : List<ResolveInfo> = packageManager.queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY)

                for(camerActivity in cameraActivities){
                    requireActivity().grantUriPermission(camerActivity.activityInfo.packageName,photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                }
                startActivityForResult(captureImage, REQUEST_PHOTO)

            }
        }


        dateButton.setOnClickListener {
            DatePickerFragment.newInstance(diet.date).apply {
                setTargetFragment(this@DietFragment, REQUEST_DATE)
                show(this@DietFragment.getParentFragmentManager(), DIALOG_DATE)
            }
        }

        val weightWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               val weightText = s.toString()
                if(weightText.isEmpty()){
                    diet.weight =0.0
                }else{
                    diet.weight = weightText.toDouble()
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        }

        weight.addTextChangedListener(weightWatcher)

        val mealWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                diet.Meal = s.toString()
            }
            override fun afterTextChanged(s: Editable?) {
            }

        }
        meal.addTextChangedListener(mealWatcher)

        val ExerciseWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                diet.Exercise = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        exercise.addTextChangedListener(ExerciseWatcher)

        waterPlusButton.apply {
            setOnClickListener {
                val watercup = ++diet.water
                water.setText("$watercup CUPS")
            }
        }
        waterMinusButton.apply {
            setOnClickListener {
                val watercup = --diet.water
                water.setText("$watercup CUPS")
            }
        }

        MoodGroup.apply{
            setOnCheckedChangeListener { group, checkedId ->
                when(checkedId){
                    R.id.Mood1 -> diet.Mood = 1
                    R.id.Mood2 -> diet.Mood = 2
                    R.id.Mood3 -> diet.Mood = 3
                    R.id.Mood4 -> diet.Mood = 4
                    R.id.Mood5 -> diet.Mood = 5


                }
            }
        }

        reportButton.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getDietReport())
                putExtra(Intent.EXTRA_SUBJECT, R.string.dietReport_subject)
            }.also { intent ->
                val chooserIntent = Intent.createChooser(intent, "보고서 전송")
                startActivity(chooserIntent)

            }
        }
        saveButton.apply {
            setOnClickListener {
                dietDetailViewModel.saveDiet(diet)
                Toast.makeText(requireContext(),"저장되었습니다", Toast.LENGTH_SHORT ).show()
                callbacks?.popCurrentFragment()
            }
        }

        deleteButton.apply {
            setOnClickListener {
                dietDetailViewModel.deleteDiet(diet)
                Toast.makeText(requireContext(),"삭제되었습니다", Toast.LENGTH_SHORT ).show()
                callbacks?.popCurrentFragment()
           }
        }

    }


    override fun onDateSelected(date: Date) {
        diet.date = date
        updateUI()
    }

    private fun getDietReport(): String{
        val weightString = getString(R.string.weightReport, diet.weight.toString())
        val dateString = DateFormat.format(DATE_FORMAT, diet.date).toString()
        val waterString = getString(R.string.waterReport, diet.water)
        val mealString = getString(R.string.mealReport, diet.Meal)
        val exerciseString = getString(R.string.exerciseReport, diet.Exercise)

        return getString(R.string.dietReport, dateString, mealString, waterString, exerciseString, weightString)
    }

    private fun updateUI(){

        dateButton.setText(DateFormat.format(DATE_FORMAT, diet.date).toString())
        weight.setText(diet.weight.toString())
        water.setText("${diet.water} CUPS")
        meal.setText(diet.Meal)
        exercise.setText(diet.Exercise)
        when(diet.Mood){
            1->MoodGroup.check(R.id.Mood1)
            2->MoodGroup.check(R.id.Mood2)
            3->MoodGroup.check(R.id.Mood3)
            4->MoodGroup.check(R.id.Mood4)
            5->MoodGroup.check(R.id.Mood5)

        }
        updatePhotoView()


    }

    private fun updatePhotoView(){
        if(photoFile.exists()){
            val bitmap = getScaledBitmap(photoFile.path, requireActivity())
            photoView.setImageBitmap(bitmap)
        }else{
            photoView.setImageDrawable(null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when{
            requestCode == REQUEST_PHOTO -> {
                requireActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                updatePhotoView()
            }
        }
    }



    companion object {
        fun newInstance(dietID: UUID): DietFragment {
            val args = Bundle().apply {
                putSerializable(ARG_DIET_ID, dietID)
            }

            return DietFragment().apply {
                arguments = args
            }
        }
    }

}