package com.bossdev.automazione

class Utils {
    companion object
    {
        fun mapRange(number: Int, original: IntRange, target: IntRange): Int {
            val originalLength = original.last - original.first
            val targetLength = target.last - target.first

            // Calculate the ratio of the number's position within the original range
            val ratio = (number - original.first).toFloat() / originalLength

            // Scale and shift the ratio to fit the target range
            return (ratio * targetLength + target.first).toInt()
        }
    }
}