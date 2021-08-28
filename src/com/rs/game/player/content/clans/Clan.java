package com.rs.game.player.content.clans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.player.Player;



public class Clan implements Serializable {

	public static final int RECRUIT = 0, CORPORAL = 1, SERGEANT = 2, LIEUTENANT = 3, CAPTAIN = 4, GENERAL = 5,
			ADMIN = 100, ORGANISER = 101, COORDINATOR = 102, OVERSEER = 103, DEPUTY_OWNER = 125,
			LEADER = 126, MAX_MEMBERS = 500;

    /**
	 * 
	 */
    private static final long serialVersionUID = 4062231702422979939L;

    private String clanLeaderUsername; // index in list
    private List<ClanMember> members;
    private List<String> bannedUsers;
    private int timeZone, clanMemberAmount;
    private boolean recruiting;
    public boolean isIronOnly;
    private boolean isClanTime;
    private int worldId;
    private int clanFlag;
    private boolean guestsInChatCanEnter;
    private boolean guestsInChatCanTalk;
	private ClanBank clanBank;
    private String threadId;
    private String motto;

    private int mottifTop, mottifBottom;
    private int[] mottifColors;

    private int minimumRankForKick;

    private transient String clanName;
    
    public Clan(String clanName, Player leader) {
		setDefaults();
	    if (leader.getTemporaryAttributtes().get("makingIronOnlyClan") != null) {
			isIronOnly = true;
			leader.getTemporaryAttributtes().remove("makingIronOnlyClan");
		}
		this.members = new ArrayList<ClanMember>();
		this.bannedUsers = new ArrayList<String>();
		this.clanBank = new ClanBank();
		setClanLeaderUsername(addMember(leader, LEADER));
		init(clanName);
    }
    
    public boolean canMakeCitadel() {
    	if (getClanMemberAmount() > 5)
    		return true;
    	return false;
    }
    
    public boolean isOnTile(Player player) {
    	if ((player.getX() == 2956 && player.getY() == 3295) ||
    			(player.getX() == 2962 && player.getY() == 3296) ||
    			(player.getX() == 2965 && player.getY() == 3291) ||
    			(player.getX() == 2962 && player.getY() == 3286) ||
    			(player.getX() == 2956 && player.getY() == 3287))
    		return true;
    	return false;
    }
    
    public boolean hasMemberOnEachTile(Player player) {
    	if (members.equals(1))
    		if (isOnTile(player))
    			return true;
    	else if (members.equals(2))
    		if (isOnTile(player))
    			return true;
    	else if (members.equals(3))
    		if (isOnTile(player))
    			return true;
    	else if (members.equals(4))
    		if (isOnTile(player))
    			return true;
    	else if (members.equals(5))
    		if (isOnTile(player))
    			return true;
    	return false;    	
    }
    
    public void setDefaults() {
    	recruiting = true;
    	isIronOnly = false;
    	guestsInChatCanEnter = true;
    	guestsInChatCanTalk = true;
    	worldId = 1;
    	mottifColors = Arrays.copyOf(ItemDefinitions.getItemDefinitions(20709).originalModelColors, 4);
    }

    public void init(String clanName) {
    	this.clanName = clanName;
    }

    public ClanMember addMember(Player player, int rank) {
    	ClanMember member = new ClanMember(player.getUsername(), rank);
    	members.add(member);
    	clanMemberAmount ++;
    	return member;
    }

    public void setClanLeaderUsername(ClanMember member) {
    	clanLeaderUsername = member.getUsername();
    }

    public int getMemberId(ClanMember member) {
    	return members.indexOf(member);
    }

    public List<ClanMember> getMembers() {
    	return members;
    }

    public List<String> getBannedUsers() {
    	return bannedUsers;
    }

    public String getClanName() {
    	return clanName;
    }

    public int getTimeZone() {
    	return timeZone;
    }

    public boolean isRecruiting() {
    	return recruiting;
    }
    
    public boolean isIronOnly() {
    	return isIronOnly;
    }

    public void switchRecruiting() {
    	recruiting = !recruiting;
    }

    public void setTimeZone(int gameTime) {
    	this.timeZone = gameTime;
    }

    public int getMinimumRankForKick() {
    	return minimumRankForKick;
    }

    public void setMinimumRankForKick(int minimumRankForKick) {
    	this.minimumRankForKick = minimumRankForKick;
    }

    public String getThreadId() {
    	return threadId;
    }

    public void setThreadId(String threadId) {
    	this.threadId = threadId;
    }

    public boolean isGuestsInChatCanEnter() {
    	return guestsInChatCanEnter;
    }

    public void switchGuestsInChatCanEnter() {
    	this.guestsInChatCanEnter = !guestsInChatCanEnter;
    }

    public String getMotto() {
    	return motto;
    }

    public void setMotto(String motto) {
    	this.motto = motto;
    }

    public boolean isClanTime() {
    	return isClanTime;
    }

    public void switchClanTime() {
	isClanTime = !isClanTime;
    }

    public int getWorldId() {
	return worldId;
    }

    public void setWorldId(int worldId) {
	this.worldId = worldId;
    }

    public int getClanFlag() {
	return clanFlag;
    }

    public void setClanFlag(int clanFlag) {
	this.clanFlag = clanFlag;
    }

    public String getClanLeaderUsername() {
	return clanLeaderUsername;
    }

    public boolean isGuestsInChatCanTalk() {
	return guestsInChatCanTalk;
    }

    public int getMottifTop() {
	return mottifTop;
    }

    public void setMottifTop(int mottifTop) {
	this.mottifTop = mottifTop;
    }

    public int getMottifBottom() {
	return mottifBottom;
    }

    public void setMottifBottom(int mottifBottom) {
	this.mottifBottom = mottifBottom;
    }

    public int[] getMottifColors() {
	return mottifColors;
    }

    public void setMottifColours(int[] mottifColors) {
	this.mottifColors = mottifColors;
    }

    public void switchGuestsInChatCanTalk() {
	guestsInChatCanTalk = !guestsInChatCanTalk;
    }
    
    public int getClanMemberAmount() {
    	return clanMemberAmount;
    }

	public ClanMember getMemberByName(String username) {
		for (final ClanMember member : members) {
			if (member.getUsername().toLowerCase().equals(username.toLowerCase())) {
				return member;
			}
		}
		return null;
	}

	/**
	 * @return the clanBank
	 */
	public ClanBank getClanBank() {
		return clanBank;
	}
	
	public ClanBank getClanBank(Player player) {
		return clanBank;
	}

}
