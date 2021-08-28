package com.rs.game.player.dialogues.impl;

import com.rs.game.player.dialogues.Dialogue;

public class OnSpotDueling extends Dialogue {
    @Override
    public void start() {
    	sendOptionsDialogue("Where would you like to fight?", "Let's fight on our Spot.", "Let's fight at Duel Arena.");
    }

    @Override
    public void run(int interfaceId, int componentId)  {
        switch (stage) {
            case -1:
                player.setOnSpotDueling(false);
                switch (componentId) {
                    case OPTION_1:
                        player.setOnSpotDuelingRequest(true);
                        player.getInterfaceManager().sendInterface(640);
                        end();
                        break;
                    case OPTION_2:
                        player.setOnSpotDuelingRequest(false);
                        player.getInterfaceManager().sendInterface(640);
                        end();
                        break;
                }
                break;
        }
    }

    @Override
    public void finish() {

    }
}
