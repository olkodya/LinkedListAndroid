package com.example.linkedlistandroid.model

import kotlinx.serialization.Serializable
import java.lang.IllegalArgumentException
import java.util.*

class Fraction() : UserType {

    private var numerator = 1
    private var denominator = 2
    private var integerPart = 1

    constructor(integerPart: Int, numerator: Int, denominator: Int) : this() {
        require(isFractionValid(numerator, denominator)) { "Invalid fraction $integerPart $numerator/$denominator" }
        this.numerator = numerator
        this.denominator = denominator
        this.integerPart = integerPart
    }

    companion object {
        fun isFractionValid(numerator: Int, denominator: Int): Boolean {
            return if (numerator >= denominator) false else denominator != 0
        }

        fun create(): UserType {
            val r = Random()
            val randIntPart = r.nextInt(100) + 1
            var randNum = r.nextInt(100) + 1
            var ranDen = r.nextInt(100) + 1
            while (!isFractionValid(randNum, ranDen)) {
                randNum = r.nextInt(100) + 1
                ranDen = r.nextInt(100) + 1
            }
            return Fraction(randIntPart, randNum, ranDen)
        }

        fun clone(obj: UserType): UserType {
            val fraction = Fraction()
            fraction.setDenominator((obj as Fraction).getDenominator())
            fraction.setNumerator(obj.getNumerator())
            fraction.integerPart = obj.integerPart
            return fraction
        }
    }

    override fun typeName(): String {
        return this.javaClass.toString()
    }

    override fun parseValue(ss: String): UserType {
        val parts = ss.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var frParts = emptyList<String>()
        if (parts.size == 2) {
            frParts = parts[1].split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().toList()
        } else throw IllegalArgumentException("Invalid fraction")
        if (frParts.size != 2) throw IllegalArgumentException("Invalid fraction")
        return Fraction(parts[0].toInt(), frParts[0].toInt(), frParts[1].toInt())
    }

    override val typeComparator: Comparator<UserType>
        get() = Comparator { o1, o2 ->
            val fraction1 = (o1 as Fraction).numerator.toDouble() / o1.denominator
            val fraction2 = (o2 as Fraction).numerator.toDouble() / o2.denominator
            if (o1.integerPart != o2.integerPart) {
                o1.integerPart - o2.integerPart
            } else fraction1.compareTo(fraction2)
        }

    fun getNumerator(): Int {
        return numerator
    }

    fun setNumerator(numerator: Int) {
        if (isFractionValid(numerator, denominator)) {
            this.numerator = numerator
        } else this.numerator = 1
    }

    fun setDenominator(denominator: Int) {
        if (isFractionValid(numerator, denominator)) {
            this.denominator = denominator
        } else this.denominator = 2
    }

    fun getDenominator(): Int {
        return denominator
    }

    override fun toString(): String {
        return "{" +
                integerPart + " " +
                numerator + "/" +
                denominator + "}"
    }

}