package com.example.assignment2task2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RadioButton

private var product_list: Array<Product> = arrayOf(
    Product("Iceberg Lettuce", 3.5),
    Product("Coral Lettuce", 4.0),
    Product("Romaine Lettuce", 4.0),
    Product("Chicken", 3.7),
    Product("Salmon", 4.5),
    Product("Bacon", 3.5),
    Product("Turkey", 3.7),
    Product("Smoked Duck", 5.0),
    Product("Egg", 2.0),
    Product("Tomato", 1.2),
    Product("Apple", 2.5),
    Product("Potato", 1.8),
    Product("Nuts", 2.0),
    Product("Cheese", 2.0),
    Product("Avocado", 3.0),
    Product("Croutons", 1.5),
    Product("Thousand Island", 0.0),
    Product("Vinaigrette", 0.8),
    Product("Caesar", 1.0)
)

class menu : Fragment() {
    var greenList = ArrayList<Product>()
    var proteinList = ArrayList<Product>()
    var sidesList = ArrayList<Product>()
    var dressingList = ArrayList<Product>()
    var selectedProductList = ArrayList<Product>()
    private lateinit var communicator: Communicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        // Greens image
        val icebergLettuceImage = view?.findViewById<ImageView>(R.id.iceberg_lettuce_image)
        val coralLettuceImage = view?.findViewById<ImageView>(R.id.coral_lettuce_image)
        val romaineLettuceImage = view?.findViewById<ImageView>(R.id.romaine_lettuce_image)

        // Proteins checkbox
        val chickenCheckbox = view?.findViewById<CheckBox>(R.id.chicken_checkbox)
        val salmonCheckbox = view?.findViewById<CheckBox>(R.id.salmon_checkbox)
        val baconCheckbox = view?.findViewById<CheckBox>(R.id.bacon_checkbox)
        val turkeyCheckbox = view?.findViewById<CheckBox>(R.id.turkey_checkbox)
        val eggCheckbox = view?.findViewById<CheckBox>(R.id.egg_checkbox)
        val smokedDuckCheckbox = view?.findViewById<CheckBox>(R.id.smoked_duck_checkbox)

        // Sides checkbox
        val tomatoCheckbox = view?.findViewById<CheckBox>(R.id.tomato_checkbox)
        val appleCheckbox = view?.findViewById<CheckBox>(R.id.apple_checkbox)
        val potatoCheckbox = view?.findViewById<CheckBox>(R.id.potato_checkbox)
        val nutsCheckbox = view?.findViewById<CheckBox>(R.id.nuts_checkbox)
        val cheeseCheckbox = view?.findViewById<CheckBox>(R.id.cheese_checkbox)
        val avocadoCheckbox = view?.findViewById<CheckBox>(R.id.avocado_checkbox)
        val croutonsCheckbox = view?.findViewById<CheckBox>(R.id.croutons_checkbox)

        // Dressings radio button
        val thousandIslandRadio = view?.findViewById<RadioButton>(R.id.thousand_island_radio)
        val vinaigretteRadio = view?.findViewById<RadioButton>(R.id.vinaigrette_radio)
        val caesarRadio = view?.findViewById<RadioButton>(R.id.caesar_radio)

        // Next button
        val nextButton = view?.findViewById<Button>(R.id.next_button)


        // Attach on click listener
        icebergLettuceImage?.setOnClickListener { greens("Iceberg") }
        coralLettuceImage?.setOnClickListener { greens("Coral") }
        romaineLettuceImage?.setOnClickListener { greens("Romaine") }

        chickenCheckbox?.setOnClickListener{ protein(chickenCheckbox!!,"Chicken") }
        salmonCheckbox?.setOnClickListener{ protein(salmonCheckbox!!,"Salmon") }
        baconCheckbox?.setOnClickListener{ protein(baconCheckbox!!,"Bacon") }
        turkeyCheckbox?.setOnClickListener{ protein(turkeyCheckbox!!,"Turkey") }
        eggCheckbox?.setOnClickListener{ protein(eggCheckbox!!,"Egg") }
        smokedDuckCheckbox?.setOnClickListener{ protein(smokedDuckCheckbox!!,"Smoked Duck") }

        tomatoCheckbox?.setOnClickListener{ sides(tomatoCheckbox!!,"Tomato") }
        appleCheckbox?.setOnClickListener{ sides(appleCheckbox!!,"Apple") }
        potatoCheckbox?.setOnClickListener{ sides(potatoCheckbox!!,"Potato") }
        nutsCheckbox?.setOnClickListener{ sides(nutsCheckbox!!,"Nuts") }
        cheeseCheckbox?.setOnClickListener{ sides(cheeseCheckbox!!,"Cheese") }
        avocadoCheckbox?.setOnClickListener{ sides(avocadoCheckbox!!,"Avocado") }
        croutonsCheckbox?.setOnClickListener{ sides(croutonsCheckbox!!,"Croutons") }

        thousandIslandRadio?.setOnClickListener{ dressing("Thousand Island") }
        vinaigretteRadio?.setOnClickListener{ dressing("Vinaigrette") }
        caesarRadio?.setOnClickListener{ dressing("Caesar") }
        
        nextButton?.setOnClickListener { next() }
        communicator = activity as Communicator

        return view
    }

    private fun next() {
        selectedProductList.addAll(greenList)
        selectedProductList.addAll(proteinList)
        selectedProductList.addAll(sidesList)
        selectedProductList.addAll(dressingList)

        communicator.selectedProductPassing(selectedProductList)
    }

    fun greens(text: String) {
        greenList.clear()
        for (product in product_list) {
            if (product.name?.contains(text) == true) {
                greenList.add(product)
                break
            }
        }
    }

    private fun protein(checkBox: CheckBox, text: String) {
        when (checkBox.isChecked) {
            true -> {
                for (product in product_list) {
                    if (product.name?.contains(text) == true) {
                        proteinList.add(product)
                        break
                    }
                }
            }
            else -> {
                for (product in product_list) {
                    if (product.name?.contains(text) == true) {
                        proteinList.remove(product)
                        break
                    }
                }

            }
        }
    }

    private fun sides(checkBox: CheckBox, text: String) {
        when (checkBox.isChecked) {
            true -> {
                for (product in product_list) {
                    if (product.name?.contains(text) == true) {
                        sidesList.add(product)
                        break
                    }
                }
            }
            else -> {
                for (product in product_list) {
                    if (product.name?.contains(text) == true) {
                        sidesList.remove(product)
                        break
                    }
                }
            }
        }
    }

    private fun dressing(text: String) {
        dressingList.clear()
        for (product in product_list) {
            if (product.name?.contains(text) == true) {
                dressingList.add(product)
                break
            }
        }
    }


}