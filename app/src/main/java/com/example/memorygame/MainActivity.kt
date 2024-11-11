package com.example.memorygame

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.memorygame.databinding.ActivityMainBinding
import com.example.memorygame.models.Boradsize
import com.example.memorygame.models.MemoryGame
import com.example.memorygame.utils.EXTRA_BOARD_SIZE
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private var boardsize = Boradsize.EASY
    lateinit var memoryGame: MemoryGame
    lateinit var memoryGameAdapter:MemoryGameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        setupboard()



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       menuInflater.inflate(R.menu.menumain,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.refresh_id ->{
                //set up game agian
                if(memoryGame.getNumMoves()>0 && !memoryGame.haveWonGame()){
                   showAlertDialog("Quit your current game?",null,View.OnClickListener { setupboard() })
                }else
                setupboard()
            }
            R.id.choose_boardsize->{
                chooseBoardsizeDialoge()
            }
            R.id.Create_game->{
                createGameDialoge()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createGameDialoge() {
        val layoutForBoardsize = LayoutInflater.from(this).inflate(R.layout.boardsize_select, null )

        val radioGroup= layoutForBoardsize.findViewById<RadioGroup>(R.id.radio_group)
        showAlertDialog("Create Game",layoutForBoardsize,View.OnClickListener {
            val desiredBoardsize :Boradsize= when(radioGroup.checkedRadioButtonId){
                R.id.easy_button-> Boradsize.EASY
                R.id.medium_button ->
                    Boradsize.MEDIUM

                else -> {  Boradsize.HARD}
            }

            val intent = Intent(this,createActivity::class.java)
            intent.putExtra(EXTRA_BOARD_SIZE,desiredBoardsize)
            startActivityForResult(intent,23)

        })
    }

    private fun chooseBoardsizeDialoge() {
        val layoutForBoardsize = LayoutInflater.from(this).inflate(R.layout.boardsize_select, null )
        val radioGroup= layoutForBoardsize.findViewById<RadioGroup>(R.id.radio_group)
        when(boardsize){
            Boradsize.EASY->radioGroup.check(R.id.easy_button)
            Boradsize.MEDIUM->radioGroup.check(R.id.medium_button)
            Boradsize.HARD ->radioGroup.check(R.id.hard_button)
        }
       showAlertDialog("Choose Boardsize",layoutForBoardsize,View.OnClickListener {
           boardsize= when(radioGroup.checkedRadioButtonId){
               R.id.easy_button-> Boradsize.EASY
               R.id.medium_button ->
                   Boradsize.MEDIUM

               else -> {  Boradsize.HARD}
           }
           setupboard()


       })
    }

    private fun showAlertDialog(title:String,view:View?,positiveClickLisenter:View.OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("cancel",null)
            .setPositiveButton("Ok"){_,_ ->
                positiveClickLisenter.onClick(null)
            }.show()
    }

    private fun setupboard() {
        when(boardsize){
            Boradsize.EASY ->{
                binding.textMoves.text="Easy: 4 x 2"
                binding.textPairs.text="Pairs: 0 / 4"
            }
            Boradsize.MEDIUM ->{
                binding.textMoves.text="Medium: 6 x 3"
                binding.textPairs.text="Pairs: 0 / 9"
            }
            Boradsize.HARD->{
                binding.textMoves.text="Hard: 6 x 4"
                binding.textPairs.text="Pairs: 0 / 12 "
            }
        }
        memoryGame=MemoryGame(boardsize)

        binding.recyclerViewId.setHasFixedSize(true)


        memoryGameAdapter= MemoryGameAdapter(this,boardsize,memoryGame.cards,object :MemoryGameAdapter.CardClickListener{
            override fun onCardClicked(position: Int) {
                updateGameWithFlip(position)
            }
        })
        binding.recyclerViewId.adapter=memoryGameAdapter
        binding.recyclerViewId.layoutManager= GridLayoutManager(this,boardsize.getwidth())

    }
    @SuppressLint("SetTextI18n")
    private fun updateGameWithFlip(position: Int) {
        //error checing
        if(memoryGame.haveWonGame()){
          Snackbar.make(binding.clRoot,"You already won",Snackbar.LENGTH_LONG).show()
            return
        }
        if (memoryGame.isCardFaceUp(position)){
            Snackbar.make(binding.clRoot,"Invalid Move",Snackbar.LENGTH_LONG).show()

            return

        }
        //Acutal flip of card
     if( memoryGame.flipCard(position)){
         binding.textPairs.text="Pairs:${memoryGame.numPairsFound}/${boardsize.getNumparis()}"
         if(memoryGame.haveWonGame()){
             Snackbar.make(binding.clRoot,"you have won the game",Snackbar.LENGTH_LONG).show()


         }
     }
        binding.textMoves.text="Moves:${memoryGame.getNumMoves()}"
        memoryGameAdapter.notifyDataSetChanged()
    }
}