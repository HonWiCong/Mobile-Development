package com.example.assignment5task1.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment5task1.R
import com.example.assignment5task1.Task
import com.example.assignment5task1.adapter.TaskListRecycleAdapter
import com.google.firebase.firestore.FirebaseFirestore

class CompletedFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_completed, container, false)
        val taskListRecyclerView = view.findViewById<RecyclerView>(R.id.task_list_small)
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
                        val completedDate = data.get("completedDate").toString()
                        taskList.add(
                            Task(
                                id,
                                task as String?,
                                description as String?,
                                date as String?,
                                priority as String?,
                                status as String?,
                                completedDate as String?
                            )
                        )
                        index++
                    }
                }
                taskListRecyclerView.adapter = TaskListRecycleAdapter(taskList, requireActivity(), "complete")
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }

        return view
    }
}