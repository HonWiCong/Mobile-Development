package com.example.assignment7.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.assignment7.R
import com.example.assignment7.adapter.RecipeItemRecycleViewAdapter
import com.example.assignment7.data_class.Ingredients
import com.example.assignment7.data_class.Link
import com.example.assignment7.data_class.Recipe
import com.google.gson.Gson
import java.lang.Exception

class LunchFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lunch, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.lunch_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val lunchLinkList = ArrayList<String?>()
        val lunchRecipeList = ArrayList<Recipe>()

        val url = "https://api.edamam.com/api/recipes/v2?type=public&app_id=1ebd0db1&app_key=4fae7ddfb922cf36e09e1d6e10ff1802&cuisineType=Japanese&mealType=Lunch&field=cuisineType"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null, { response ->
            try {
                var i = 0
                while (i < 8) {
                    val link = Gson().fromJson(response.toString(), Link::class.java).hits[i].Links?.self?.href
                    lunchLinkList.add(link)
                    if (lunchLinkList.isEmpty()) {
                        Log.d("link: ", "The List is empty")
                    }

                    i++
                }

                // Move the loop here, inside the API response callback
                for (link in lunchLinkList) {
                    Log.d("My Link: ", link!!)
                    val json = JsonObjectRequest(
                        Request.Method.GET, link, null, { response ->
                        try {
                            val recipe = response.getString("recipe")
                            val label = Gson().fromJson(recipe, Recipe::class.java).label.toString()
                            val source = Gson().fromJson(recipe, Recipe::class.java).source.toString()
                            val diet = Gson().fromJson(recipe, Recipe::class.java).dietLabels.toMutableList()
                            val ingredients = Gson().fromJson(recipe, Recipe::class.java).ingredients.toMutableList()
                            val thumbnail = Gson().fromJson(recipe, Recipe::class.java).images
                            val image = Gson().fromJson(recipe, Recipe::class.java).image.toString()

                            lunchRecipeList.add(
                                Recipe(
                                label,
                                image,
                                thumbnail,
                                source,
                                diet as ArrayList<String>,
                                ingredients as ArrayList<Ingredients>
                            )
                            )

                            recyclerView.adapter = RecipeItemRecycleViewAdapter(lunchRecipeList, requireContext())

                        } catch (error: Exception) {
                            Log.d("Error 1:",response.toString())
                        }
                    },
                        { error ->
                            Log.d("Error 2:", error.toString())
                        })

                    Volley.newRequestQueue(context).add(json)
                }
            } catch (error: Exception) {
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