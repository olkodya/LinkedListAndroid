package com.example.linkedlistandroid.model

import java.io.Serializable


class Node(
    private var next: Node?,
    private var data: UserType,
) : Serializable{

    fun getData(): UserType = data

    fun setData(data: UserType) {
        this.data = data
    }

    fun getNext(): Node? = next

    fun setNext(next: Node?) {
        this.next = next
    }
}