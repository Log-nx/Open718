package com.rs.game.player.dialogues.impl;

import com.rs.Settings;
import com.rs.game.player.dialogues.Dialogue;

		public class Banker extends Dialogue {

					int npcId;

					@Override
					public void start() {
						npcId = (Integer) parameters[0];
						sendNPCDialogue(npcId, 9827, "Good day, how may I help you?" );
					}

					@Override
					public void run(int interfaceId, int componentId)  {
						if (stage == -1) {
							if (player.isLendingItem == true) {
								sendNPCDialogue(npcId, 9827, "... Not available..");
							}
							stage = 0;
							sendOptionsDialogue("What would you like to say?",
									"I'd like to acess my bank account, please.",
									"I'd like to check my PIN settings.",
									"I'd like to see my collection box.", "What is this place?");
						} else if (stage == 0) {
							if (componentId == OPTION_1) {
								player.getBank().openBank();
								end();
							} else if (componentId == OPTION_2) {
								/*stage = 6;
								sendOptionsDialogue("Select an Option", 
										"How can I make myself a PIN?",
										"How do I remove my PIN?", 
										"Close, nevermind.");
										player.getBank().openSetPin();
							*/
								
								player.getDialogueManager().startDialogue("BankPinD", 45);
							} else if (componentId == OPTION_3) {
								// TODO collection boss
								end();
							} else if (componentId == OPTION_4) {
								stage = 1;
								sendPlayerDialogue( 9827, "What is this place?" );
							} else
								end();
						} else if (stage == 6) {
							if (componentId == OPTION_1) {
								sendNPCDialogue(npcId, 9827, "It's simple, all you have to do is use our command, which is '::SetPin', .. So you may be asking, how do I use it? It's even more simple, follow me: <u>':: SetPin <col = ff0000> 1234 <col />'</u>.. You can change the red to whatever you want.");
						    
							} else if (componentId == OPTION_2) {
								sendNPCDialogue(npcId, 9827, "We do not recommend you to remove your PIN. If you still want to remove your PIN, type ::Removepin.");
							} else if (componentId == OPTION_3) {
								end();
						} else if (stage == 1) {
							stage = 2;
							sendNPCDialogue(npcId, 9827, "This is a branch of the Bank of "
									+ Settings.SERVER_NAME + ". We have",
							"branches in many towns." );
						} else if (stage == 2) {
							stage = 3;
							sendOptionsDialogue("What would you like to say?",
									"And what do you do?",
									"Didn't you used to be called the Bank of Varrock?");
						} else if (stage == 3) {
							if (componentId == OPTION_1) {
								stage = 4;
								sendPlayerDialogue( 9827, "And what do you do?" );
							} else if (componentId == OPTION_2) {
								stage = 5;
								sendPlayerDialogue( 9827, "Didn't you used to be called the Bank of Varrock?" );
							} else
								end();
						} else if (stage == 4) {
							stage = -2;
							sendNPCDialogue(npcId, 9827, "We will look after your items and money for you.",
									"Leave your valuables with us if you want to keep them",
									"safe.");
						} else if (stage == 5) {
							stage = -2;
							sendNPCDialogue(npcId, 9827, "Yes we did, but people kept on coming into our",
									"signs were wrong. They acted as if we didn't know",
									"what town we were in or something.");
						} else
							end();
					}}

					@Override
					public void finish() {

					}

				}
				