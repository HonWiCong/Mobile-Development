package com.example.assignment2task2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Heath Living"
        val colorDrawable = ColorDrawable(Color.parseColor("#006400"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        init()
    }

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
    private var iceberg_lettuce_image: ImageView? = null
    private var coral_lettuce_image: ImageView? = null
    private var romaine_lettuce_image: ImageView? = null

    private var chicken_checkbox: CheckBox? = null
    private var salmon_checkbox: CheckBox? = null
    private var bacon_checkbox: CheckBox? = null
    private var turkey_checkbox: CheckBox? = null
    private var egg_checkbox: CheckBox? = null
    private var smoked_duck_checkbox: CheckBox? = null
    private var tomato_checkbox: CheckBox? = null
    private var apple_checkbox: CheckBox? = null
    private var potato_checkbox: CheckBox? = null
    private var nuts_checkbox: CheckBox? = null
    private var cheese_checkbox: CheckBox? = null
    private var avocado_checkbox: CheckBox? = null
    private var croutons_checkbox: CheckBox? = null

    private var thousand_island_radio: RadioButton? = null
    private var vinaigrette_radio: RadioButton? = null
    private var caesar_radio: RadioButton? = null

    private var next_button: Button? = null

    private var green_list = ArrayList<Product>()
    private var protein_list = ArrayList<Product>()
    private var sides_list = ArrayList<Product>()
    private var dressing_list = ArrayList<Product>()


    private fun init() {
        iceberg_lettuce_image = findViewById(R.id.iceberg_lettuce_image)
        coral_lettuce_image = findViewById(R.id.coral_lettuce_image)
        romaine_lettuce_image = findViewById(R.id.romaine_lettuce_image)

        chicken_checkbox = findViewById(R.id.chicken_checkbox)
        salmon_checkbox = findViewById(R.id.salmon_checkbox)
        bacon_checkbox = findViewById(R.id.bacon_checkbox)
        turkey_checkbox = findViewById(R.id.turkey_checkbox)
        egg_checkbox = findViewById(R.id.egg_checkbox)
        smoked_duck_checkbox = findViewById(R.id.smoked_duck_checkbox)
        tomato_checkbox = findViewById(R.id.tomato_checkbox)
        apple_checkbox = findViewById(R.id.apple_checkbox)
        potato_checkbox = findViewById(R.id.potato_checkbox)
        nuts_checkbox = findViewById(R.id.nuts_checkbox)
        cheese_checkbox = findViewById(R.id.cheese_checkbox)
        avocado_checkbox = findViewById(R.id.avocado_checkbox)
        croutons_checkbox = findViewById(R.id.croutons_checkbox)

        thousand_island_radio = findViewById(R.id.thousand_island_radio)
        vinaigrette_radio = findViewById(R.id.vinaigrette_radio)
        caesar_radio = findViewById(R.id.caesar_radio)

        next_button = findViewById(R.id.next_button)

        iceberg_lettuce_image?.setOnClickListener{ greens("Iceberg") }
        coral_lettuce_image?.setOnClickListener{ greens("Coral") }
        romaine_lettuce_image?.setOnClickListener{ greens("Romaine") }

        chicken_checkbox?.setOnClickListener{ protein(chicken_checkbox!!,"Chicken") }
        salmon_checkbox?.setOnClickListener{ protein(salmon_checkbox!!,"Salmon") }
        bacon_checkbox?.setOnClickListener{ protein(bacon_checkbox!!,"Bacon") }
        turkey_checkbox?.setOnClickListener{ protein(turkey_checkbox!!,"Turkey") }
        egg_checkbox?.setOnClickListener{ protein(egg_checkbox!!,"Egg") }
        smoked_duck_checkbox?.setOnClickListener{ protein(smoked_duck_checkbox!!,"Smoked Duck") }

        tomato_checkbox?.setOnClickListener{ sides(tomato_checkbox!!,"Tomato") }
        apple_checkbox?.setOnClickListener{ sides(apple_checkbox!!,"Apple") }
        potato_checkbox?.setOnClickListener{ sides(potato_checkbox!!,"Potato") }
        nuts_checkbox?.setOnClickListener{ sides(nuts_checkbox!!,"Nuts") }
        cheese_checkbox?.setOnClickListener{ sides(cheese_checkbox!!,"Cheese") }
        avocado_checkbox?.setOnClickListener{ sides(avocado_checkbox!!,"Avocado") }
        croutons_checkbox?.setOnClickListener{ sides(croutons_checkbox!!,"Croutons") }

        thousand_island_radio?.setOnClickListener{ dressing(thousand_island_radio!!,"Thousand Island") }
        vinaigrette_radio?.setOnClickListener{ dressing(vinaigrette_radio!!,"Vinaigrette") }
        caesar_radio?.setOnClickListener{ dressing(caesar_radio!!,"Caesar") }

        next_button?.setOnClickListener { next() }
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(p0: View?) {

    }

    private fun clear() {
        green_list.clear()
        protein_list.clear()
        sides_list.clear()
        dressing_list.clear()

        chicken_checkbox?.isChecked = false
        salmon_checkbox?.isChecked = false
        bacon_checkbox?.isChecked = false
        turkey_checkbox?.isChecked = false
        egg_checkbox?.isChecked = false
        smoked_duck_checkbox?.isChecked = false

        tomato_checkbox?.isChecked = false
        apple_checkbox?.isChecked = false
        potato_checkbox?.isChecked = false
        nuts_checkbox?.isChecked = false
        cheese_checkbox?.isChecked = false
        avocado_checkbox?.isChecked = false
        croutons_checkbox?.isChecked = false

        thousand_island_radio?.isChecked = false
        vinaigrette_radio?.isChecked = false
        caesar_radio?.isChecked = false
    }

    @SuppressLint("SetTextI18n")
    private fun greens(text: String) {
        green_list.clear()
        for (product in product_list) {
            if (product.name.contains(text)) {
                green_list.add(product)
                break
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun protein(checkBox: CheckBox, text: String) {

        when (checkBox.isChecked) {
            true -> {
                for (product in product_list) {
                    if (product.name.contains(text)) {
                        protein_list.add(product)
                        break
                    }
                }
            }
            else -> {
                for (product in product_list) {
                    if (product.name.contains(text)) {
                        protein_list.remove(product)
                        break
                    }
                }

            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun sides(checkBox: CheckBox, text: String) {
        when (checkBox.isChecked) {
            true -> {
                for (product in product_list) {
                    if (product.name.contains(text)) {
                        sides_list.add(product)
                        break
                    }
                }
            }
            else -> {
                for (product in product_list) {
                    if (product.name.contains(text)) {
                        sides_list.remove(product)
                        break
                    }
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun dressing(radioButton: RadioButton, text: String) {
        dressing_list.clear()

        for (product in product_list) {
            if (product.name.contains(text)) {
                dressing_list.add(product)
                break
            }
        }
    }

    private fun next() {
        val intent = Intent(this, order::class.java)
        intent.putExtra("green_list", green_list)
        intent.putExtra("protein_list", protein_list)
        intent.putExtra("sides_list", sides_list)
        intent.putExtra("dressing_list", dressing_list)
        startActivityForResult(intent, 0)
//        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            val shouldClearValues = data?.getBooleanExtra("clear", false) ?: false
            if (shouldClearValues) {
                clear()
            }
        }
    }

}