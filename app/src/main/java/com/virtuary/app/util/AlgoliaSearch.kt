package com.virtuary.app.util

import com.algolia.search.saas.Client
import com.virtuary.app.firebase.FirestoreRepository


class AlgoliaSearch {
    companion object {
        const val API_KEY = "api_key"
        const val APP_ID = "application_id"
        const val INDEX_NAME = "application_id"
    }

    suspend fun config() {
        val algoliaConfig = FirestoreRepository().getAlgoliaConfig()
        var client = Client(algoliaConfig[APP_ID] as String, algoliaConfig[API_KEY] as String)
        var index = client.getIndex(algoliaConfig[INDEX_NAME] as String)
    }
}
