package com.example.linkedlistandroid.model
import java.util.*
import kotlin.Comparator

class MyInteger(
    private var value: Int = 0,
): UserType {
    companion object {
        fun create(): UserType {
            val r = Random()
            val randValue = r.nextInt(100) + 1
            return MyInteger(randValue)
        }

        fun clone(obj: UserType): UserType {
            val myInteger = MyInteger((obj as MyInteger).value)
            return myInteger
        }
    }
    override fun typeName(): String {
        return this.javaClass.toString()
    }

    override fun parseValue(ss: String): UserType {
        return MyInteger(ss.toInt())
    }

    override val typeComparator: Comparator<UserType>
        get() = Comparator { o1, o2 -> (o1 as MyInteger).value - (o2 as MyInteger).value }

    fun setValue(value: Int) {
        this.value = value
    }

    fun getValue(): Int {
        return value
    }

    override fun toString(): String {
        return value.toString()
    }
}