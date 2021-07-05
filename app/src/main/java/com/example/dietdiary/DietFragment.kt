package com.example.dietdiary

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import java.util.*

private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0

class DietFragment : Fragment(), DatePickerFragment.Callbacks {

    private lateinit var diet : Diet
    private lateinit var weight : EditText
    private lateinit var meal : EditText
    private lateinit var exercise : EditText
    private lateinit var dateButton : Button
    private lateinit var water : TextView
    private lateinit var waterPlusButton : ImageButton
    private lateinit var waterMinusButton : ImageButton
    private lateinit var MoodGroup : RadioGroup



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        diet = Diet()
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
        MoodGroup = view.findViewById(R.id.MoodGroup) as RadioGroup

        updateUI()

        return view
    }

    override fun onStart() {
        super.onStart()


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
                diet.weight = weightText.toDouble()
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
                water.setText("$watercup Cups")
            }
        }
        waterMinusButton.apply {
            setOnClickListener {
                val watercup = --diet.water
                water.setText("$watercup Cups")
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

    }

    override fun onDateSelected(date: Date) {
        diet.date = date
        updateUI()
    }

    private fun updateUI(){
        val calendar = Calendar.getInstance()
        calendar.time = diet.date
        val Year = calendar.get(Calendar.YEAR)
        calendar.add(Calendar.MONTH, 1)
        val Month = calendar.get(Calendar.MONTH)
        val Day = calendar.get(Calendar.DAY_OF_MONTH)
        dateButton.setText("$Year. $Month. $Day")
    }

}