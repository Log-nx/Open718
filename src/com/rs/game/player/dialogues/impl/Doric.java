package com.rs.game.player.dialogues.impl;

import com.rs.game.item.Item;
import com.rs.game.player.dialogues.Dialogue;
import com.rs.game.player.dialogues.impl.quests.DoricsQuest;

public class Doric extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (int) parameters[0];
        if (!player.startedDoricsQuest) {
            sendNPCDialogue(npcId, 9827, "Oy der, mate! Care to help a little fella who needs some clay and a couple o' ores? There's a good reward, there is. So, how 'bout it, mate?");
        } else if (player.inProgressDoricsQuest) {
            sendNPCDialogue(npcId, 9827, "So, you got my items, mate?");
            stage = 4;
        } else if (player.completedDoricsQuest) {
            sendNPCDialogue(npcId, 9850, "Y'know, you could always use my anvils.");
            stage = -2;
        }
    }

    @Override
    public void run(int interfaceId, int componentId)  {
        switch (stage) {
            case -1:
                sendOptionsDialogue(SEND_DEFAULT_OPTIONS_TITLE, "Sure.", "Uh, I'll have to pass for now.");
                stage = 0;
                break;
            case 0:
                switch (componentId) {
                    case OPTION_1:
                        sendPlayerDialogue(9830, "Sure.");
                        DoricsQuest.handleProgressQuest(player);
                        stage = 1;
                        break;
                    case OPTION_2:
                        sendPlayerDialogue(9830, "Uh, I'll have to pass for now.");
                        stage = 3;
                        break;
                }
                break;
            case 1:
                sendNPCDialogue(npcId, 9850, "Oh, that's great, right 'der, that's great. Now, I will be needing some clay, some copper, and a couple o' iron ores.");
                stage = 2;
                break;
            case 2:
                sendPlayerDialogue(9830, "Alright, I'm on my way.");
                stage = -2;
                break;
            case 3:
                sendNPCDialogue(npcId, 9789, "You'll have to pass? Oh I see, it's 'cause I'm short, ain't it? You pesky humans need to learn some respect for us dwarfs. Get out of my house!");
                stage = -2;
                break;
            case 4:
                if (player.getInventory().containsItems(new int[]{437, 441, 435}, new int[]{4, 2, 6})) {
                    sendPlayerDialogue(9850, "Here's all the items!");
                    stage = 5;
                } else {
                    sendPlayerDialogue(9830, "I don't have all the items yet.");
                    stage = 7;
                }
                break;
            case 5:
                player.getInventory().removeItems(new Item(437, 4), new Item(441, 2), new Item(435, 6));
                sendNPCDialogue(npcId, 9850, "Thanks mate! I owe it to ya!");
                stage = 6;
                break;
            case 6:
                end();
                DoricsQuest.handleQuestComplete(player);
                DoricsQuest.handleQuestCompleteInterface(player);
                break;
            case 7:
                sendNPCDialogue(npcId, 9830, "Well then go get them, you baffoon!");
                stage = -2;
                break;
            default:
                end();
                break;
        }
    }

    @Override
    public void finish() {

    }
}