package com.example.nkustplatformassistant.data.remote.interests


import kotlinx.coroutines.flow.Flow


/**
 * Interface to the Login data layer.
 */
interface InterestsLogin {

    /**
     * login to NKUST educational administration system(webap).
     */
    suspend fun loginWebap(uid:String, pwd:String, etxtCode:String?): Result<Boolean>

    /**
     * login to NKUST educational administration system (mobile version).
     */
    suspend fun loginMobile(uid:String, pwd:String, etxtCode:String?): Result<List<String>>

    /**
     * Get list of publications.
     */
    suspend fun getPublications(): Result<List<String>>

    /**
     * Toggle between selected and unselected
     */
    suspend fun toggleTopicSelection(topic: String)

    /**
     * Toggle between selected and unselected
     */
    suspend fun togglePersonSelected(person: String)

    /**
     * Toggle between selected and unselected
     */
    suspend fun togglePublicationSelected(publication: String)

    /**
     * Currently selected topics
     */
    fun observeTopicsSelected(): Flow<Set<String>>

    /**
     * Currently selected people
     */
    fun observePeopleSelected(): Flow<Set<String>>

    /**
     * Currently selected publications
     */
    fun observePublicationSelected(): Flow<Set<String>>
}


