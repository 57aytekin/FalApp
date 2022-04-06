package com.falApp.sadekahvefal.model.response

import com.falApp.sadekahvefal.model.FalJobs

data class JobResponse(
    val isSuccessful: Boolean,
    val job_list: List<FalJobs>,
    val message: String
)