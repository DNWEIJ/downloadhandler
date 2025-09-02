package com.dwe.springboot.tutorial.model

data class UploadFileResponse(
    val fileName: String,
    val fileDownloadUrl: String,
    val fileType: String,
    val fileSize: Long = -1L,
)