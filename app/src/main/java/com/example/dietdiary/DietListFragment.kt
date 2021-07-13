package com.example.dietdiary

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*


private const val DATE_FORMAT = "MM.dd.yy"

class DietListFragment : Fragment() {

    private lateinit var dietRecyclerView: RecyclerView
    private lateinit var fab : ImageButton
    private var adapter : DietAdapter ? = DietAdapter(emptyList())
    private val dietListViewModel : DietListViewModel by lazy {
        ViewModelProvider(this).get(DietListViewModel::class.java)
    }

    interface Callbacks {
        fun onDietSelected(dietId: UUID)
    }

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diet_list, container, false)
        dietRecyclerView = view.findViewById(R.id.diet_recycler_view) as RecyclerView
        fab = view.findViewById(R.id.fab) as ImageButton
        dietRecyclerView.layoutManager = LinearLayoutManager(context)
        dietRecyclerView.adapter = adapter



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dietListViewModel.dietListLiveData.observe(
            viewLifecycleOwner,
            Observer{ diets ->
                diets?.let {
                    updateUI(diets)
                }
            }
        )
    }

    override fun onStart() {
        super.onStart()

        fab.apply {
            setOnClickListener {
                val diet = Diet()
                dietListViewModel.addDiet(diet)
                callbacks?.onDietSelected(diet.id)
            }
        }
    }

    private fun updateUI(diets : List<Diet>){
        adapter = DietAdapter(diets)
        dietRecyclerView.adapter = adapter
    }


    private inner class DietHolder(view : View) : RecyclerView.ViewHolder(view), View.OnClickListener{

        private lateinit var diet : Diet

        private val weightTextView : TextView = itemView.findViewById(R.id.diet_weight)
        private val dateTextView : TextView = itemView.findViewById(R.id.diet_date)
        private val waterTextView : TextView = itemView.findViewById(R.id.water_cups)
        private val mealTextView : TextView = itemView.findViewById(R.id.meal_check)
        private val exerciseTextView : TextView = itemView.findViewById(R.id.exercise_check)
        private val moodImage : ImageView = itemView.findViewById(R.id.diet_mood)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(diet : Diet){
            this.diet = diet
            weightTextView.setText(getString(R.string.Weight, diet.weight.toString()))
            waterTextView.setText(getString(R.string.WaterCups, diet.water))
            dateTextView.text = android.text.format.DateFormat.format(DATE_FORMAT, diet.date).toString()

            when(diet.Mood){
                1 -> moodImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.happy_icon))
                2 -> moodImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.good))
                3 -> moodImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.average))
                4 -> moodImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.poor))
                5 -> moodImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.worst))
            }

            if(diet.Meal.isNotEmpty()){
                mealTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, ResourcesCompat.getDrawable(getResources(),R.drawable.check_24,null), null)
            }
            if(diet.Exercise.isNotEmpty()) {
                exerciseTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, ResourcesCompat.getDrawable(getResources(),R.drawable.check_24,null), null)
            }

        }

        override fun onClick(v: View?) {

            callbacks?.onDietSelected(diet.id)

        }




    }

    private inner class DietAdapter(var diets : List<Diet>) : RecyclerView.Adapter<DietHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DietHolder {
            val view = layoutInflater.inflate(R.layout.list_item_diet, parent, false)
            return DietHolder(view)
        }

        override fun onBindViewHolder(holder: DietHolder, position: Int) {
            val diet = diets[position]
            holder.bind(diet)
        }

        override fun getItemCount() = diets.size

    }

    companion object {
        fun newInstance() : DietListFragment {
            return DietListFragment()
        }
    }
}