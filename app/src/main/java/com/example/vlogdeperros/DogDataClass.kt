package com.example.vlogdeperros

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class DogDataClass(@Exclude var id:String = "",
                        var title:String = "",
                        var photoUrl:String = "",
                        var likeList: Map<String,Boolean> = mutableMapOf())
