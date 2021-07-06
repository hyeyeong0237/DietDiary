package com.example.dietdiary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.DateFormat
import java.util.*

class DietListFragment : Fragment() {

    private lateinit var dietRecyclerView: RecyclerView
    private var adapter : DietAdapter ? = null

    private val dietListViewModel : DietListViewModel by lazy {
        ViewModelProvider(this).get(DietListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diet_list, container, false)
        dietRecyclerView = view.findViewById(R.id.diet_recycler_view) as RecyclerView
        dietRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()

        return view
    }

    private fun updateUI(){
        val diets = dietListViewModel.dietList
        adapter = DietAdapter(diets)
        dietRecyclerView.adapter = adapter
    }

    private inner class DietHolder(view : View) : RecyclerView.ViewHolder(view), View.OnClickListener{

        private lateinit var diet : Diet

        private val weightTextView : TextView = itemView.findViewById(R.id.diet_weight)
        private val dateTextView : TextView = itemView.findViewById(R.id.diet_date)
        private val waterTextView : TextView = itemView.findViewById(R.id.water_cups)
        private val MoodImage : ImageView = itemView.findViewById(R.id.diet_mood)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(diet : Diet){
            this.diet = diet
            val weight = diet.weight.toString()
            weightTextView.setText("$weight kg")
            val cups = diet.water.toString()
            waterTextView.setText("$cups CUPS")
            dateTextView.text = DateFormat.getInstance().format(diet.date)

            when(diet.Mood){
                1 -> MoodImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.smile))
                2 -> MoodImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.water))
                3 -> MoodImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.camera))
                4 -> MoodImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.water_minus))
                5 -> MoodImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.water_plus))
            }

        }

        override fun onClick(v: View?) {

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