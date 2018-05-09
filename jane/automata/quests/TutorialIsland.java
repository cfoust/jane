package com.sqweebloid.jane.automata.quests;

import java.awt.Rectangle;
import net.***REMOVED***.api.coords.WorldPoint;
import net.***REMOVED***.api.widgets.Widget;
import net.***REMOVED***.api.ItemID;
import static net.***REMOVED***.api.widgets.WidgetInfo.TO_GROUP;

import com.sqweebloid.jane.automata.Automaton;

/**
 * Does all of Tutorial Island, probably.
 */
public class TutorialIsland extends Automaton {
    @Override
    public void run() {
        machine.setStateChooser(() -> client.getVarps()[281]);

        // Make your character.
        machine.state(0)
            .base()
            .enter(() -> {
                // Look at all widgets with group id 269. 
                // Those that are 45x36 are the appearance buttons
                // Look for the button that says "female"
                Widget creation = input.getWidgetContaining("Welcome to RuneScape");

                if (creation != null && !creation.isHidden()) {
                    mouse(input.getWidgetContaining("Female").getBounds()).left();

                    for (Widget widget : input.getWidgets()) {
                        if (TO_GROUP(widget.getId()) != 269) continue;
                        Rectangle bounds = widget.getBounds();

                        if (bounds.getWidth() != 45 || bounds.getHeight() != 36) continue;

                        for (int i = 0; i < rand(5); i++) {
                            mouse(bounds).left();
                        }
                    }

                    mouse(input.getWidgetContaining("Accept").getBounds()).left();
                }

                // You have to click the guide 3308 to start.
                // He has some dialog and then asks a question.
                npc(3308).talk();
                sleep().most();
                talk();
                sleep().more();

                // Q What's your experience with Runescape?
                choose(rand(2));
                sleep().most();
                talk();
                sleep().most();

                // When you answer, you have to click the "Options" wrench.
                ui().options();
            });

        machine.state(7)
            .enter(() -> {
                npc(3308).talk();
                sleep().most();
                talk();
            });

        machine.state(10)
            .enter(() -> {
                wall(9398).interact("Open");
                sleep().most();
            });

        machine.state(20)
            .enter(() -> {
                go(new WorldPoint(3103, 3096, 0));
                sleep().most();
                npc(3306).talk();
                sleep().most();
                talk();
            });

        machine.state(30)
            .enter(() -> {
                ui().inventory();
            });

        // Title "Viewing the items you were given" and flashing inventory button you have to click
        machine.state(30)
            .enter(() -> {
                // Need to click this even if we already have it open
                ui().inventory();
            });

        // Cut down a tree 9730. "You get some logs."
        machine.state(40)
            .enter(() -> {
                object(9730).interact("Chop down");
                sleep().most();
                sleep().most();
            });

        // Use tinderbox on logs
        machine.state(50)
            .enter(() -> {
                use(ItemID.TINDERBOX).onItem(ItemID.LOGS_2511);
                sleep().most();
            });

        //// Text "You gained some experience" you have to click on stats
        machine.state(60)
            .enter(() -> {
                sleep().some();
                ui().stats();
                sleep().most();
            });

        machine.state(70)
            .enter(() -> {
                npc(3306).talk();
                talk();
                sleep().most();
            });

        machine.state(80)
            .enter(() -> {
                sleep().most();
                // Click on fishing spot NPC 3317
                npc(3317).interact();
                sleep().most();
                sleep().most();
                sleep().most();
            });

        machine.state(90)
            .enter(() -> {
                object(9730).interact("Chop down");
                sleep().most();
                sleep().most();
                use(ItemID.TINDERBOX).onItem(ItemID.LOGS_2511);
                sleep().most();
                use(ItemID.RAW_SHRIMPS_2514).onObject(26185);
            });

        machine.state(110)
            .enter(() -> {
                // Click on fishing spot NPC 3317
                npc(3317).interact();
                sleep().most();
                sleep().most();
                use(ItemID.RAW_SHRIMPS_2514).onObject(26185);
            });

        //// 90
        //// Need to chop down another tree, light fire GO 26185, and use shrimp on it
        //// If there isn't a fire already, need to chop down a tree and make one again.
        //use(ItemID.RAW_SHRIMPS).onObject(26185);
        //// 110
        //// You burn the shrimp
        //// Catch more shrimp and use them on fire again
        //// 120
        ////Now we good
        //Move to gate at 3090, 3092
        //Click on gate W 9470/9708
        //// 130
        //Move to 3079, 3084
        //Click door W 9709
        //// 140
        //Talk to chef 3305
        //// 150
        //Use bucket 1929 on pot with flour 2516
        //// 160
        //Use dough 2307 on range GO 9736 to make loaf 2309
        //// 170
        //Click on flashing music player button 
        //// 180
        //Move to door 3073, 3090
        //Click door W 9710
        //// 183
        //Flashing emotes button
        //// ?
        //Click random emote
        //// 190
        //Click options wrench
        //// 200
        //Set running
        //// 210
        //Move to door 3086, 3127
        //Open door 9716
        //// 220
        //Talk to quest guide 3312
        //// 230
        //Flashing quest button
        //// 240
        //Talk to quest guide again
        //// 250
        //Click ladder 9726
        //// 260
        //Move to 3080, 9506
        //Talk to mining instructor 3311
        //// 270
        //Prospect rock 10080
        //// 280
        //Prospect rock 10079
        //// 290
        //Talk to mining instructor
        //Receive bronze pickaxe 1265
        //// 300
        //Click rock 10080
        //// 310
        //Click rock 10079
        //// 320
        //Use I copper 436 or tin 438 on furnace GO 10082
        //Makes a bronze bar 2349
        //Click dialog text "You retrieve a bar of bronze"
        //The text seems to glitch. Check if it's on the screen and click it.
        //// 330
        //Talk to mining instructor
        //Receive hammer 2347
        //// 340
        //Use bronze bar on anvil GO 2097
        //// 350
        //Look for widget with "Bronze dagger"
        //Smith one dagger 1205
        //// 360
        //Click dialog text "You retrieve a bar of bronze"
        //Move to gate at 3094, 9502
        //Click gate 9718
        //// 370
        //Move to 3105, 9506
        //Talk to combat instructor 3307
        //// 390
        //Flashing equipment tab
        //// 400
        //Click "View equipment stats" button
        //// 405
        //Left click bronze dagger
        //// 410
        //Close window
        //Talk to combat instructor
        //Gives you sword 1277 and shield 1171
        //// 420
        //equip them
        //// 430
        //Flashing combat interface
        //// 440
        //Move to 3111, 9519
        //Click gate 9720
        //// 450
        //Kill rat 3313
        //// 470
        //Click gate 9720
        //Talk to combat instructor
        //Gives bow 841 and arrows 882
        //// 480
        //Wield them
        //Kill rat 3313 
        //// 490
        //// 500
        //Move to 3111, 9525
        //Click ladder GO 9727
        //// 510
        //Move to 3122, 3123
        //Click bank booth 10083
        //Asks "Select an Option"
        //Click "Yes." (0)
        //// 520
        //Bank frame shows up
        //Close window
        //// 525
        //Click poll booth GO 26815
        //Click through dialog
        //Close window
        //// 530
        //Click door 9721 at 3125, 3124
        //Talk to financial advisor 3310
        //// 540
        //Click on door 9722
        //// 550
        //Move to 3127, 3106
        //Click on door 1524 -> 1525
        //Talk to priest 3319
        //// 560
        //Flashing prayer menu
        //// 570
        //Talk to priest again
        //// 580
        //Click friends list
        //// 590
        //Click ignore list
        //// 600
        //Talk to priest again
        //// 610
        //Move to 3122, 3103
        //Click door 9723
        //// 620
        //Move to 3140, 3088
        //Talk to wizard 3309
        //// 630
        //Click magic menu
        //// 640
        //Gives runes wind 556 and mind 558
        //// 650
        //Use wind strike on chicken
        //// 670
        //Talk to wizard
        //"Do you want to go to the mainland?"
        //"Yes." (0)
        //"Select an Option."
        //talk
        //"No, I'm not planning to do that." (1)
        //Talking
        //Teleported to 3233, 3230
        machine.start();
    }
}
