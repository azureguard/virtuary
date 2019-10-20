package com.virtuary.app.util

import com.algolia.search.client.ClientSearch
import com.algolia.search.client.Index
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.virtuary.app.firebase.FirestoreRepository

class AlgoliaClient {
    companion object {
        private const val API_KEY = "api_key"
        private const val APP_ID = "application_id"
        suspend fun getIndex(index: String): Index {
            val algoliaConfig = FirestoreRepository().getAlgoliaConfig()
            val appID = ApplicationID(algoliaConfig[APP_ID] as String)
            val apiKey = APIKey(algoliaConfig[API_KEY] as String)
            val client = ClientSearch(appID, apiKey)
            val indexName = IndexName(index)
            return client.initIndex(indexName)
        }
    }
}
