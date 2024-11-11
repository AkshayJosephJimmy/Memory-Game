package com.example.memorygame.models

enum class Boradsize(val numCards: Int) {
    EASY(8),
    MEDIUM(18),
    HARD(24);
//this function ids used to get the width ie the number of memory cards wide it should be ie the number of coloums

    fun getwidth(): Int {
        return when (this) {
            EASY -> 2
            MEDIUM -> 3
            HARD -> 4
        }
    }
    // this function is used to find the nnumber of cards in heiht ie the number of rows

    fun getheight():Int{
        return numCards/getwidth()
    }
    fun getNumparis():Int{
       return numCards/2
    }
}