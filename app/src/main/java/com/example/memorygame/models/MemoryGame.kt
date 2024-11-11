package com.example.memorygame.models

import com.example.memorygame.utils.Defalut_icons

class MemoryGame (private val boradsize: Boradsize){

    val cards:List<MemoryCard>
    var numPairsFound =0
    private var cardFlips=0
private var indexOfSingleSelectedCard:Int?=null
    init {
        val choosenImage= Defalut_icons.shuffled().take(boradsize.getNumparis())
        val randomizedImage= (choosenImage+choosenImage).shuffled()
        cards=randomizedImage.map { MemoryCard(it) }
    }
    fun flipCard(position: Int):Boolean {
        cardFlips++
         val card :MemoryCard=cards[position]

        //three cases:
        //case one:0 cards are flipped->flip over selected card
        //case two:one card is alredy flipped->flipover the sekextef csard and check if the csrfds are soem
        //case three : 2cards ate flipepd->restoores cards anf flip the seleced care(we csn ude the sme for the foidt cader)
        var foundMatch=false
        if(indexOfSingleSelectedCard==null){
            restoreCards()
            indexOfSingleSelectedCard=position
        }
        else{
            foundMatch =checkForMatch(indexOfSingleSelectedCard!!,position)
            indexOfSingleSelectedCard=null
        }
        card.isfaceup= !card.isfaceup
        return foundMatch
    }

    private fun checkForMatch(indexOfSingleSelectedCard: Int, position: Int): Boolean {
        return if(cards[indexOfSingleSelectedCard].identifer!=cards[position].identifer){
            false
        } else {
            cards[indexOfSingleSelectedCard].isMatched=true
            cards[position].isMatched=true
            numPairsFound++
            true
        }

    }


    private fun restoreCards() {
        for (card in cards){
            if (!card.isMatched)
            card.isfaceup=false

        }
    }

    fun haveWonGame(): Boolean {
       return numPairsFound == boradsize.getNumparis()
    }

    fun isCardFaceUp(position: Int): Boolean {
          return cards[position].isfaceup
    }

    fun getNumMoves(): Int {

     return cardFlips/2
    }

}