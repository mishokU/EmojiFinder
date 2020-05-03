package com.example.emojifinder.data.db.remote.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

open class FirebaseInit  {
    val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    val mUser : FirebaseUser? = mAuth.currentUser
    val mFireStore = FirebaseFirestore.getInstance()
}