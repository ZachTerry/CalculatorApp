package com.example.dpscalculator

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random
import android.view.Menu;

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {


            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show()
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.options_menu, menu);
        return true;

    }


    //runs when submit button is clicked
    fun submitInfo(view: android.view.View) {

        val pkmnLvl = findViewById<EditText>(R.id.pkmnLvlText).text.toString().toDouble();
        val pkmnAtk = findViewById<EditText>(R.id.pkmnAtkText).text.toString().toDouble();
        val enemyDef = findViewById<EditText>(R.id.enemyDefText).text.toString().toDouble();
        val moveAtk = findViewById<EditText>(R.id.moveAtkText).text.toString().toDouble();

        val pkmnType1 = findViewById<Spinner>(R.id.pkmnType1Spinner).selectedItem.toString();
        val pkmnType2 = findViewById<Spinner>(R.id.pkmnType2Spinner).selectedItem.toString();
        val enemyType1 = findViewById<Spinner>(R.id.enemyType1Spinner).selectedItem.toString();
        val enemyType2 = findViewById<Spinner>(R.id.enemyType2Spinner).selectedItem.toString();
        val moveType = findViewById<Spinner>(R.id.moveTypeSpinner).selectedItem.toString();

        val damageText: TextView = findViewById<TextView>(R.id.damageText);

        //call get effectiveness function
        val effectiveness = getEffectiveness(moveType, enemyType1, enemyType2);
        Toast.makeText(getApplicationContext(),"Effectiveness: " + effectiveness,Toast.LENGTH_SHORT).show();

        //random value between .85 and 1
        val random: Double = Random.nextDouble(.85, 1.0)

        // if move used is the same type as the pokemon value is 1.5 instead of 1
        var Stab = 1.0;
        if (pkmnType1 == moveType || pkmnType2 == moveType) {
            Stab = 1.5;
        }

        // formula based off pokemon damage formula: https://bulbapedia.bulbagarden.net/wiki/Damage
        // with changes, not every factor is taken into the formula for this app
        val damage = ((((((2 * pkmnLvl)/5)+2) * moveAtk * (pkmnAtk/enemyDef))/50) + 2) * Stab * effectiveness * random;

        damageText.text = damage.toInt().toString();

    }

    // run when the Clear button is pressed, clears all values and fields
    fun clearInfo(view: android.view.View) {

        Toast.makeText(getApplicationContext(),"Fields cleared",Toast.LENGTH_SHORT).show();

        //clears text fields
        findViewById<EditText>(R.id.pkmnLvlText).text.clear();
        findViewById<EditText>(R.id.pkmnAtkText).text.clear();
        findViewById<EditText>(R.id.enemyDefText).text.clear();
        findViewById<EditText>(R.id.enemyLvlText).text.clear();
        findViewById<EditText>(R.id.moveAtkText).text.clear();

        //sets spinner to default
        findViewById<Spinner>(R.id.pkmnType1Spinner).setSelection(0);
        findViewById<Spinner>(R.id.pkmnType2Spinner).setSelection(0);
        findViewById<Spinner>(R.id.enemyType1Spinner).setSelection(0);
        findViewById<Spinner>(R.id.enemyType2Spinner).setSelection(0);
        findViewById<Spinner>(R.id.moveTypeSpinner).setSelection(0);

        // clears text fields
        findViewById<TextView>(R.id.damageText).text = "0";

    }

    // this function determines the effectiveness of a move against an opponent based on types.
    fun getEffectiveness(moveType: String, enemyType1: String, enemyType2: String): Double {
        // value depending on effectiveness of move. 1x for normal, 2x super, 4x double super, .5x half, .25x quarter
        var effectiveness = 1.0;

        // plan to implement better algorithm, and all types for type effectiveness
        // temporary if statements for determining type effectiveness. Only types currently in are normal, water, grass and fire.
        // normal type is assumed to be 1.0 always currently
        //fire type 2x against grass but 1/2 against fire and water, combined when two types
        if (moveType == "Fire") {
            if (enemyType1 == "Grass") {
                if (enemyType2 == "Normal" || enemyType2 == "None") {
                    effectiveness = 2.0;
                }
            } else if (enemyType1 == "Fire") {
                if (enemyType2 == "Water") {
                    effectiveness = 0.25;
                } else if (enemyType2 == "Normal" || enemyType2 == "None") {
                    effectiveness = 0.5;
                }
            } else if (enemyType1 == "Water") {
                if (enemyType2 == "Fire") {
                    effectiveness = 0.25;
                } else if (enemyType2 == "Normal" || enemyType2 == "None") {
                    effectiveness = 0.5;
                }
            }

        //Water type 2x against fire but 1/2x against grass and water
        } else if (moveType == "Water") {
            if (enemyType1 == "Fire") {
                if (enemyType2 == "Normal" || enemyType2 == "None") {
                    effectiveness = 2.0;
                }
            } else if (enemyType1 == "Grass") {
                if (enemyType2 == "Water") {
                    effectiveness = 0.25;
                } else if (enemyType2 == "Normal" || enemyType2 == "None") {
                    effectiveness = 0.5;
                }
            } else if (enemyType1 == "Water") {
                if (enemyType2 == "Grass") {
                    effectiveness = 0.25;
                } else if (enemyType2 == "Normal" || enemyType2 == "None") {
                    effectiveness = 0.5;
                }
            }

        //Grass type 2x against water but 1/2x against grass and fire
        } else if (moveType == "Grass") {
            if (enemyType1 == "Water") {
                if (enemyType2 == "Normal" || enemyType2 == "None") {
                    effectiveness = 2.0;
                }
            } else if (enemyType1 == "Grass") {
                if (enemyType2 == "Fire") {
                    effectiveness = 0.25;
                } else if (enemyType2 == "Normal" || enemyType2 == "None") {
                    effectiveness = 0.5;
                }
            } else if (enemyType1 == "Fire") {
                if (enemyType2 == "Grass") {
                    effectiveness = 0.25;
                } else if (enemyType2 == "Normal" || enemyType2 == "None") {
                    effectiveness = 0.5;
                }
            }
        }
        return effectiveness;

    }

}