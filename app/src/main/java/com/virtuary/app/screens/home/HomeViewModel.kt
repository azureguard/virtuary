package com.virtuary.app.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algolia.search.client.ClientSearch
import com.algolia.search.helper.deserialize
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.google.firebase.firestore.Query
import com.virtuary.app.firebase.FirestoreRepository
import com.virtuary.app.firebase.Item
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    companion object {
        const val API_KEY = "api_key"
        const val APP_ID = "application_id"
        const val INDEX_NAME = "index_name"
    }

    private val repository: FirestoreRepository = FirestoreRepository()
    var query: Query = repository.queryAllItems()

    private val _searchResult = MutableLiveData<List<Item>>()
    val searchResult: LiveData<List<Item>> = _searchResult


    fun searchItem(queryString: String) {
        viewModelScope.launch {
            val algoliaConfig = FirestoreRepository().getAlgoliaConfig()
            val appID = ApplicationID(algoliaConfig[APP_ID] as String)
            val apiKey = APIKey(algoliaConfig[API_KEY] as String)
            val client = ClientSearch(appID, apiKey)

            val indexName = IndexName(algoliaConfig[INDEX_NAME] as String)
            val index = client.initIndex(indexName)
            val query = com.algolia.search.model.search.Query(query = queryString)
            val result = index.search(query)

            _searchResult.value = result.hits.deserialize(Item.serializer())
        }
    }

}
