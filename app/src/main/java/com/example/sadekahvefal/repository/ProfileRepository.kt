package com.example.sadekahvefal.repository

import com.example.sadekahvefal.base.BaseRepository
import com.example.sadekahvefal.network.APIClientImpl
import javax.inject.Inject

class ProfileRepository @Inject constructor (
    private val apiClientImpl: APIClientImpl
) : BaseRepository() {

}