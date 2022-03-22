package com.example.sadekahvefal.model.response

import com.example.sadekahvefal.model.FalJobs

data class JobResponse(
    val isSuccessful: Boolean,
    val job_list: List<FalJobs>,
    val message: String
)