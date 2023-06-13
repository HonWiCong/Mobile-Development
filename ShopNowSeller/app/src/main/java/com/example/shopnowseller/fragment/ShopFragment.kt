package com.example.shopnowseller.fragment

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.shopnowseller.R
import com.example.shopnowseller.activity.MainActivity
import com.example.shopnowseller.data_class.Product
import com.example.shopnowseller.data_class.Shop
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.concurrent.CountDownLatch

class ShopFragment : Fragment() {
    private val database = Firebase.firestore
    private var coverImageUri: Uri? = null
    private var thumbnailImageUri: Uri? = null
    private val storageRef = FirebaseStorage.getInstance()
    private var coverImageDownloadURL = ""
    private var thumbnailImageDownloadURL = ""

    private lateinit var coverImageResultLauncher: ActivityResultLauncher<String>
    private lateinit var thumbnailImageResultLauncher: ActivityResultLauncher<String>

    private lateinit var cover : ImageView
    private lateinit var thumbnail : ImageView

    companion object {
        private const val TAG = "ShopFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        coverImageResultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            coverImageUri = uri
            cover.setImageURI(coverImageUri)
        }

        thumbnailImageResultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            thumbnailImageUri = uri
            thumbnail.setImageURI(thumbnailImageUri)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shop, container, false)

        cover = view.findViewById(R.id.edit_shop_cover)
        thumbnail = view.findViewById(R.id.edit_shop_thumbnail)
        val name = view.findViewById<EditText>(R.id.edit_shop_name)
        val submitButton = view.findViewById<Button>(R.id.edit_shop_submit_button)

        cover.setOnClickListener {
            coverImageResultLauncher.launch("image/*")
        }

        thumbnail.setOnClickListener {
            thumbnailImageResultLauncher.launch("image/*")
        }

        database
            .collection("shops")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    // Auto fill the name EditText
                    val nameText = document.getString("name")
                    name.setText(nameText)

                    // Load the images using Glide
                    coverImageDownloadURL = document.getString("cover").toString()
                    thumbnailImageDownloadURL = document.getString("thumbnail").toString()

                    Glide.with(this)
                        .load(coverImageDownloadURL)
                        .into(cover)

                    Glide.with(this)
                        .load(thumbnailImageDownloadURL)
                        .into(thumbnail)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

        submitButton.setOnClickListener {
            if (name.text.isNullOrEmpty()) {
                Toast.makeText(context, "All fields are required!", Toast.LENGTH_SHORT).show()
            } else {
                val progressDialog = ProgressDialog(context)
                progressDialog.setTitle("Saving")
                progressDialog.show()

                // These are flags for whether the images are retrieved from Firestore
                var isCoverImageRetrieved = coverImageDownloadURL.isNotEmpty()
                var isThumbnailImageRetrieved = thumbnailImageDownloadURL.isNotEmpty()

                if (coverImageDownloadURL.isEmpty()) {
                    Toast.makeText(context, "Cover image is missing!", Toast.LENGTH_SHORT).show()
                } else if (thumbnailImageUri == null && thumbnailImageDownloadURL.isEmpty()) {
                    Toast.makeText(context, "Thumbnail image is missing!", Toast.LENGTH_SHORT).show()
                } else {
                    val countDownLatch = CountDownLatch(if (isCoverImageRetrieved && isThumbnailImageRetrieved) 0 else if (isCoverImageRetrieved || isThumbnailImageRetrieved) 1 else 2)

                    if (!isCoverImageRetrieved) {
                        storageRef.getReference("images").child(System.currentTimeMillis().toString())
                            .putFile(coverImageUri!!)
                            .addOnCompleteListener {
                                it.result.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                                    coverImageDownloadURL = it.toString()
                                    countDownLatch.countDown()
                                }
                            }
                    }

                    if (!isThumbnailImageRetrieved) {
                        storageRef.getReference("images").child(System.currentTimeMillis().toString())
                            .putFile(thumbnailImageUri!!)
                            .addOnCompleteListener {
                                it.result.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                                    thumbnailImageDownloadURL = it.toString()
                                    countDownLatch.countDown()
                                }
                            }
                    }

                    Thread(Runnable {
                        try {
                            countDownLatch.await()

                            activity?.runOnUiThread {
                                val shop = Shop(
                                    name = name.text.toString(),
                                    thumbnail = thumbnailImageDownloadURL,
                                    cover = coverImageDownloadURL,
                                    seller_id = FirebaseAuth.getInstance().currentUser!!.uid
                                )

                                database
                                    .collection("shops")
                                    .document(FirebaseAuth.getInstance().currentUser!!.uid)
                                    .set(shop)
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Shop edited successfully", Toast.LENGTH_SHORT).show()
                                        progressDialog.dismiss()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, "Fail to save current setting", Toast.LENGTH_SHORT).show()
                                        progressDialog.dismiss()
                                    }
                            }
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                    }).start()
                }
            }
        }


        return view
    }
}
