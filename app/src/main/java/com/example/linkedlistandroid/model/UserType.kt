package com.example.linkedlistandroid.model

import java.io.Serializable


interface UserType:Serializable {

    companion object {
        fun create(): UserType? {
            return null
        }

        fun clone(obj: UserType?): UserType? {
            return null
        }
    }
    fun typeName(): String
    fun parseValue(ss: String): UserType
    val typeComparator: Comparator<UserType>
}