package com.mishok.emojifinder.data.db.remote.service

class FirebaseCollection : FirebaseInit() {

    fun <K, V>setCollectionData(collection: String, data: HashMap<K, V>){
        if(mAuth.uid != null){
            mFireStore.collection(collection).document(mAuth.uid!!).set(data)
        }
    }
}