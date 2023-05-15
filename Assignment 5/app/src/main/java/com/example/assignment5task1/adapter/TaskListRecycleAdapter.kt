package com.example.assignment5task1.adapter

import android.content.ContentValues.TAG
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment5task1.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import com.example.assignment5task1.Task
import com.example.assignment5task1.ViewActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

class TaskListRecycleAdapter(private val TaskList: ArrayList<Task>, private val context: Context, private val status: String) : RecyclerView.Adapter<TaskListRecycleAdapter.TaskViewHolder>() {
    class TaskViewHolder(TaskItemView: View) : RecyclerView.ViewHolder(TaskItemView) {
        val priorityColor = TaskItemView.findViewById<CardView>(R.id.priority_color)
        val todoText = TaskItemView.findViewById<TextView>(R.id.todo_text)
        val deadlineText = TaskItemView.findViewById<TextView>(R.id.deadline_text)
        val pendingRadio = TaskItemView.findViewById<RadioButton>(R.id.pending_radio)
        val inProgressRadio = TaskItemView.findViewById<RadioButton>(R.id.in_progress_radio)
        val completeRadio = TaskItemView.findViewById<RadioButton>(R.id.complete_radio)
        val item = TaskItemView.findViewById<CardView>(R.id.item_layout)

        val todoTextSmall = TaskItemView.findViewById<TextView>(R.id.small_task_text)
        val dateTextSmall = TaskItemView.findViewById<TextView>(R.id.small_date_text)
        val statusTextSmall = TaskItemView.findViewById<TextView>(R.id.small_status_text)
        val statusIcon = TaskItemView.findViewById<ImageView>(R.id.small_icon)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskListRecycleAdapter.TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (status) {
            "in progress", "complete" -> {
                val view = inflater.inflate(R.layout.task_item_small, parent, false)
                TaskViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.todo_item, parent, false)
                TaskViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return TaskList.size
    }

    override fun onBindViewHolder(holder: TaskListRecycleAdapter.TaskViewHolder, position: Int) {
        val task = TaskList[position]
        val db = FirebaseFirestore.getInstance()

        when (status) {
            "all" -> {
                holder.priorityColor.setBackgroundColor(getPriorityColor(task.priority))
                holder.todoText.text = task.task
                holder.deadlineText.text = task.date

                when (task.status) {
                    "pending" -> { holder.pendingRadio.isChecked = true }
                    "in progress" -> { holder.inProgressRadio.isChecked = true }
                    "complete" -> { holder.completeRadio.isChecked = true }
                }

                holder.pendingRadio.setOnClickListener {
                    updateTaskStatus(db, task.id.toString(), "pending")
                }

                holder.inProgressRadio.setOnClickListener {
                    updateTaskStatus(db, task.id.toString(), "in progress")
                }

                holder.completeRadio.setOnClickListener {
                    updateTaskStatus(db, task.id.toString(), "complete")
                }

                holder.itemView.setOnLongClickListener {
                    showItemOptionsDialog(task)
                    true
                }

                holder.item.setOnClickListener {
                    openViewActivity(task)
                }

            }
            "in progress" -> {
                if (task.status == "in progress") {
                    holder.todoTextSmall.text = task.task
                    holder.statusTextSmall.text = "In progress..."
                    holder.dateTextSmall.text = ""
                    holder.statusIcon.setImageResource(R.drawable.baseline_sync_24)
                } else {
                    holder.itemView.visibility = View.GONE
                    val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
                    params.height = 0
                    params.width = 0
                    holder.itemView.layoutParams = params
                }
            }
            "complete" -> {
                if (task.status == "complete") {
                    holder.todoTextSmall.text = task.task
                    holder.statusTextSmall.text = "Date Completed: "
                    holder.dateTextSmall.text = task.completedDate
                    holder.statusIcon.setImageResource(R.drawable.check)
                } else {
                    holder.itemView.visibility = View.GONE
                    val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
                    params.height = 0
                    params.width = 0
                    holder.itemView.layoutParams = params
                }
            }
        }
    }

    private fun getPriorityColor(priority: String?): Int {
        return when (priority) {
            "low" -> Color.parseColor("#46892a")
            "medium" -> Color.parseColor("#e98930")
            "high" -> Color.parseColor("#bf2a05")
            else -> Color.TRANSPARENT
        }
    }

    private fun updateTaskStatus(db: FirebaseFirestore, taskId: String, newStatus: String) {
        if (newStatus == "complete") {
            val calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH) + 1
            val year = calendar.get(Calendar.YEAR)

            db.collection("tasks")
                .document(taskId)
                .update(
                    "status", newStatus,
                    "completedDate", "$day-$month-$year"
                )
        } else {
            db.collection("tasks")
                .document(taskId)
                .update("status", newStatus)
        }

    }

    private fun openViewActivity(task: Task) {
        val intent = Intent(context, ViewActivity::class.java)
        intent.putExtra("id", task.id)
        intent.putExtra("task", task.task)
        intent.putExtra("description", task.description)
        intent.putExtra("date", task.date)
        intent.putExtra("priority", task.priority)
        context.startActivity(intent)
    }

    private fun showItemOptionsDialog(task: Task) {
        val dialog = AlertDialog.Builder(context)
            .setTitle("Task Options")
            .setMessage("Choose an option for this task")
            .setPositiveButton("Delete") { _, _ ->
                deleteTask(task)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun deleteTask(task: Task) {
        val db = FirebaseFirestore.getInstance()
        db.collection("tasks")
            .document(task.id.toString())
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Task deleted successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error deleting task: $exception")
                Toast.makeText(context, "Failed to delete task", Toast.LENGTH_SHORT).show()
            }
    }
}