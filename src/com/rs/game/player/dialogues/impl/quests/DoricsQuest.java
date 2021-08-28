package com.rs.game.player.dialogues.impl.quests;

import com.rs.game.player.Player;
import com.rs.game.player.Skills;

public class DoricsQuest  {

    /**
     * Sends the configuration to the quest tab and sets Doric's Quest into progress.
     *
     * @param player
     */
    public static void handleProgressQuest(final Player player) {
        player.startedDoricsQuest = true;
        player.inProgressDoricsQuest = true;
        player.getPackets().sendConfig(31, 1);
        player.getInterfaceManager().sendInterfaces();
        player.getPackets().sendUnlockIComponentOptionSlots(190, 15, 0, 201, 0, 1, 2, 3);
    }

    /**
     * The interface before the player has started the quest.
     *
     * @param player
     */
    public static void handleQuestStartInterface(final Player player) {
        player.getInterfaceManager().sendInterface(275);
        player.getPackets().sendRunScript(1207, 3);
        player.getPackets().sendIComponentText(275, 1, "Doric's Quest");
        player.getPackets().sendIComponentText(275, 10, "");
        player.getPackets().sendIComponentText(275, 11, "<col=330099>I can start this quest by speaking to</col> <col=660000>Doric</col>, <col=330099>the dwarf, near");
        player.getPackets().sendIComponentText(275, 12, "<col=660000>Goblin Village</col> <col=330099>near the crossing of</col> <col=660000>Falador</col> <col=330099>to</col> <col=660000>Varrock</col><col=330099>.</col>");
        player.getPackets().sendIComponentText(275, 13, "");
        player.getPackets().sendIComponentText(275, 14, "<col=330099>There aren't any requirements but</col> <col=660000>15 mining</col> <col=330099>will help</col>");
        for (int i = 15; i < 300; i++) {
            player.getPackets().sendIComponentText(275, i, "");
        }
    }

    /**
     * The interface during the quest when the player gathers the items.
     *
     * @param player
     */
    public static void handleProgressQuestInterface(final Player player) {
        player.getInterfaceManager().sendInterface(275);
        player.getPackets().sendRunScript(1207, 10);
        player.getPackets().sendConfig(31, 1);
        player.getPackets().sendIComponentText(275, 1, "Doric's Quest");
        player.getPackets().sendIComponentText(275, 10, "");
        player.getPackets().sendIComponentText(275, 11, "<str><col=330099>I can start this quest by speaking to</col> <col=660000>Doric</col>, <col=330099>the dwarf, near");
        player.getPackets().sendIComponentText(275, 12, "<str><col=660000>Goblin Village</col> <col=330099>near the crossing of</col> <col=660000>Falador</col> <col=330099>to</col> <col=660000>Varrock</col><col=330099>.</col>");
        player.getPackets().sendIComponentText(275, 13, "");
        player.getPackets().sendIComponentText(275, 14, "<col=330099>I've spoken to with Doric and accepted his request. He wants me to");
        player.getPackets().sendIComponentText(275, 15, "<col=3300099>gather the following materials:");
        player.getPackets().sendIComponentText(275, 16, "<col=330099>I " + (!player.getInventory().containsItem(435, 6) ? "need</col>" : "have</col>") + " <col=660000>6 clay (noted)</col> <col=330099>to give to Doric.</col>");
        player.getPackets().sendIComponentText(275, 17, "<col=330099>I " + (!player.getInventory().containsItem(437, 4) ? "need</col>" : "have</col>") + " <col=660000>4 copper ores (noted)</col> <col=330099>to give to Doric.</col>");
        player.getPackets().sendIComponentText(275, 18, "<col=330099>I " + (!player.getInventory().containsItem(441, 2) ? "need</col>" : "have</col>") + " <col=660000>2 iron ores (noted)</col> <col=330099>to give to Doric.</col>");
        for (int i = 19; i < 300; i++) {
            player.getPackets().sendIComponentText(275, i, "");
        }
    }

    /**
     * Handles the reward the player gets when completing the quest.
     *
     * @param player
     */
    public static void handleQuestComplete(final Player player) {
        player.inProgressDoricsQuest = false;
        player.completedDoricsQuest = true;
        player.questPoints += 1;
        player.getSkills().addXp(Skills.MINING, 1300);
        player.getInventory().addItemDrop(995, 180);
        player.getPackets().sendConfig(31, 100);
        player.getPackets().sendConfig(101, player.questPoints); // Quest Points
        player.getInterfaceManager().sendInterfaces();
        player.getPackets().sendUnlockIComponentOptionSlots(190, 15, 0, 201, 0, 1, 2, 3);
    }

    /**
     * The interface shown after the player has completed the quest.
     *
     * @param player
     */
    public static void handleQuestCompletionTabInterface(final Player player) {
        player.getInterfaceManager().sendInterface(275);
        player.getPackets().sendRunScript(1207, 12);
        player.getPackets().sendIComponentText(275, 1, "Doric's Quest");
        player.getPackets().sendIComponentText(275, 10, "");
        player.getPackets().sendIComponentText(275, 11, "<str><col=330099>I can start this quest by speaking to</col> <col=660000>Doric</col>, <col=330099>the dwarf, near");
        player.getPackets().sendIComponentText(275, 12, "<str><col=660000>Goblin Village</col> <col=330099>near the crossing of</col> <col=660000>Falador</col> <col=330099>to</col> <col=660000>Varrock</col><col=330099>.</col>");
        player.getPackets().sendIComponentText(275, 13, "");
        player.getPackets().sendIComponentText(275, 14, "<str><col=330099>I've spoken to with Doric and accepted his request. He wants me to");
        player.getPackets().sendIComponentText(275, 15, "<str><col=3300099>gather the following materials:");
        player.getPackets().sendIComponentText(275, 16, "<str><col=330099>I have</col> <col=660000>6 clay (noted)</col> <col=330099>to give to Doric.</col>");
        player.getPackets().sendIComponentText(275, 17, "<str><col=330099>I have</col> <col=660000>4 copper ores (noted)</col> <col=330099>to give to Doric.</col>");
        player.getPackets().sendIComponentText(275, 18, "<str><col=330099>I have</col> <col=660000>2 iron ores (noted)</col> <col=330099>to give to Doric.</col>");
        player.getPackets().sendIComponentText(275, 19, "");
        player.getPackets().sendIComponentText(275, 20, "<col=660000>QUEST COMPLETE</col>");
        for (int i = 21; i < 300; i++) {
            player.getPackets().sendIComponentText(275, i, "");
        }
    }

    /**
     * The interface as the player gets the reward for completion of the quest.
     *
     * @param player
     */
    public static void handleQuestCompleteInterface(final Player player) {
        player.getInterfaceManager().sendInterface(277);
        player.getPackets().sendIComponentText(277, 4, "You have completed Doric's Quest.");
        player.getPackets().sendIComponentText(277, 7, "" + player.questPoints);
        player.getPackets().sendIComponentText(277, 9, "You are awarded:");
        player.getPackets().sendIComponentText(277, 10, "1 Quest Point");
        player.getPackets().sendIComponentText(277, 11, "1300 Mining XP");
        player.getPackets().sendIComponentText(277, 12, "180 Coins");
        player.getPackets().sendIComponentText(277, 13, "Use of Doric's Anvils");
        player.getPackets().sendIComponentText(277, 14, "");
        player.getPackets().sendIComponentText(277, 15, "");
        player.getPackets().sendIComponentText(277, 16, "");
        player.getPackets().sendIComponentText(277, 17, "");
        player.getPackets().sendItemOnIComponent(277, 5, 1269, 1);
        player.getPackets().sendGameMessage("Congratulations! You have completed Doric's Quest!");
    }

}