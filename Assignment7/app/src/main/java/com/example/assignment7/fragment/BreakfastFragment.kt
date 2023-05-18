package com.example.assignment7.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.assignment7.R
import com.example.assignment7.data_class.Link
import com.example.assignment7.data_class.Recipe
import com.example.assignment7.adapter.RecipeItemRecycleViewAdapter
import com.example.assignment7.data_class.Ingredients
import com.google.gson.Gson

class BreakfastFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_breakfast, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.breakfast_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val breakFastLinkList = ArrayList<String?>()
        val breakFastRecipeList = ArrayList<Recipe>()

        val url = "https://api.edamam.com/api/recipes/v2?type=public&app_id=1ebd0db1&app_key=4fae7ddfb922cf36e09e1d6e10ff1802&cuisineType=Japanese&mealType=Breakfast&field=cuisineType"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try {
                var i = 0
                while (i < 8) {
                    val link = Gson().fromJson(response.toString(), Link::class.java).hits[i].Links?.self?.href
                    breakFastLinkList.add(link)

                    i++
                }

                for (link in breakFastLinkList) {
                    val json = JsonObjectRequest(Request.Method.GET, link, null, { response ->
                        try {
                            val recipe = response.getString("recipe")
                            val label = Gson().fromJson(recipe, Recipe::class.java).label.toString()
                            val source = Gson().fromJson(recipe, Recipe::class.java).source.toString()
                            val diet = Gson().fromJson(recipe, Recipe::class.java).dietLabels.toMutableList()
                            val ingredients = Gson().fromJson(recipe, Recipe::class.java).ingredients.toMutableList()
                            val thumbnail = Gson().fromJson(recipe, Recipe::class.java).images
                            val image = Gson().fromJson(recipe, Recipe::class.java).image.toString()

                            breakFastRecipeList.add(Recipe(
                                label,
                                image,
                                thumbnail,
                                source,
                                diet as ArrayList<String>,
                                ingredients as ArrayList<Ingredients>
                            ))

                            recyclerView.adapter = RecipeItemRecycleViewAdapter(breakFastRecipeList, requireContext())

                        } catch (error: java.lang.Exception) {
                            Log.d("Error 1:",response.toString())
                        }
                    },
                        { error ->
                            Log.d("Error 2:", error.toString())
                        })

                    Volley.newRequestQueue(context).add(json)
                }
            } catch (error: java.lang.Exception) {
                Log.d("vol",response.toString())
            }
        },
        { error ->
            Log.d("vol", error.toString())
        })

        Volley.newRequestQueue(context).add(jsonObjectRequest)

        return view
    }

}
