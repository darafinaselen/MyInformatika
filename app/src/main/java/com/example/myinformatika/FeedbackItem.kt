package com.example.myinformatika

data class FeedbackItem (
    val sender: String,
    val year: String,
    val category: String,
    val feedbackSnippet: String,
    val showDelete: Boolean = true
)