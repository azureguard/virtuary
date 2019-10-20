package com.virtuary.app.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algolia.search.helper.deserialize
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import com.virtuary.app.firebase.FirestoreRepository
import com.virtuary.app.firebase.Item
import com.virtuary.app.firebase.ItemSerializable
import com.virtuary.app.util.AlgoliaClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HomeViewModel : ViewModel() {

    private val repository: FirestoreRepository = FirestoreRepository()
    var query: Query = repository.queryAllItems()

    private val _searchResult = MutableLiveData<List<Item>>()
    val searchResult: LiveData<List<Item>> = _searchResult

    fun searchItem(queryString: String) {
        viewModelScope.launch {
            val index = AlgoliaClient.getIndex("item")
            if (queryString.isEmpty()) {
                _searchResult.value = query.get().await().toObjects()
                return@launch
            }
            val query = com.algolia.search.model.search.Query(query = queryString)
            val result = index.search(query)

            val resultList = result.hits.deserialize(ItemSerializable.serializer())
            _searchResult.value = resultList.map {
                Item(
                    documentId = it.objectID.toString(),
                    name = it.name,
                    originalLocation = it.originalLocation,
                    currentLocation = it.currentLocation,
                    story = it.story,
                    relations = it.relations,
                    image = it.image
                )
            }
        }
    }
}
