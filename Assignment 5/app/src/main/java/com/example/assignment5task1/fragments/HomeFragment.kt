package com.example.assignment5task1.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment5task1.R
import com.example.assignment5task1.Task
import com.example.assignment5task1.adapter.TaskListRecycleAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val taskListRecyclerView = view.findViewById<RecyclerView>(R.id.task_list)
        taskListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val taskList = ArrayList<Task>()

        val db = FirebaseFirestore.getInstance()
        db.collection("tasks")
            .get()
            .addOnSuccessListener {
                var index = 0
                if (!it.isEmpty) {
                    for (data in it.documents) {
                        val id = it.documents[index].id
                        val task = data.get("task").toString()
                        val description = data.get("description").toString()
                        val date = data.get("date").toString()
                        val priority = data.get("priority").toString()
                        val status = data.get("status").toString()
                        taskList.add(Task(
                            id,
                            task as String?,
                            description as String?,
                            date as String?,
                            priority as String?,
                            status as String?,
                            ""
                        ))
                        index++
                    }
                }
                taskListRecyclerView.adapter = TaskListRecycleAdapter(taskList, requireActivity(), "all")
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

        return view
    }

}