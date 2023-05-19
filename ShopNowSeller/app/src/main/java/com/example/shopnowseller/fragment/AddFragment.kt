package com.example.shopnowseller.fragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.shopnowseller.R
import com.example.shopnowseller.data_class.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class AddFragment : Fragment() {
    private var imageUri : Uri? = null
    private lateinit var image : ImageView
    private var storageRef = FirebaseStorage.getInstance()
    private var imageDownloadURL = ""
    private lateinit var galleryImage: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        super.onViewCreated(view, savedInstanceState)

        val name = view.findViewById<EditText>(R.id.add_product_name_input)
        val price = view.findViewById<EditText>(R.id.add_price_input)
        val description = view.findViewById<EditText>(R.id.add_description_input)
        val category = view.findViewById<Spinner>(R.id.add_category_spinner)
        image = view.findViewById(R.id.add_image)
        val uploadImageButton = view.findViewById<Button>(R.id.add_upload_image_button)
        val activeRadio = view.findViewById<RadioButton>(R.id.add_active_radio)
        val inactiveRadio = view.findViewById<RadioButton>(R.id.add_inactive_radio)
        val submitButton = view.findViewById<Button>(R.id.add_submit_button)

        val database = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser

        val categoryOptions = resources.getStringArray(R.array.category)
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryOptions)
        category.adapter = spinnerAdapter

        var status = true
        activeRadio.setOnClickListener {
            if (activeRadio.isChecked) status = true
        }

        inactiveRadio.setOnClickListener {
            if (activeRadio.isChecked) status = false
        }

        var categorySelected = ""
        category.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when (position) {
                    0 -> { categorySelected = "Technology" }
                    1 -> { categorySelected = "Daily" }
                    2 -> { categorySelected = "Food" }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                categorySelected = "Daily"
            }
        }


        galleryImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { result ->
                image.setImageURI(result)
                imageUri = result!!
            }
        )

        image.setOnClickListener {
            galleryImage.launch("image/*")
        }

        uploadImageButton.setOnClickListener {
            storageRef.getReference("images").child(System.currentTimeMillis().toString())
                .putFile(imageUri!!)
                .addOnCompleteListener {
                    it.result.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                        imageDownloadURL = it.toString()
                    }
                }

        }

        submitButton.setOnClickListener {
            val product = Product(
                name = name.text.toString(),
                price = price.text.toString().toDouble(),
                image = imageDownloadURL,
                description = description.text.toString(),
                seller = currentUser!!.uid,
                category = categorySelected,
                status = status
            )

            database
                .collection("products")
                .add(product)
                .addOnSuccessListener { Toast.makeText(context, "Product added successfully",  Toast.LENGTH_SHORT).show() }
                .addOnFailureListener { Toast.makeText(context, "Failed to add product",  Toast.LENGTH_SHORT).show() }


        }


        return view
    }

}