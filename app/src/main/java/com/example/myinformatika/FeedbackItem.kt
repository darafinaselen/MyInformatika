package com.example.myinformatika

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class FeedbackItem(
    @DocumentId
    val id: String = "",
    val date: String = "",
    val category: String = "",
    val feedbackContent: String = "",
    val userID: String = "",
    val timestamp: Timestamp? = null,
    val isDone: Boolean = false
)