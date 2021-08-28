package com.rs.game.item;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.ItemsEquipIds;

/**
 * Represents a single item.
 * <p/>
 * 
 * @author Graham / edited by Dragonkk(Alex)
 */
public class Item implements Serializable {

	private static final long serialVersionUID = -6485003878697568087L;
	private int newId = -1;
	private short id;
	protected int amount;
	private ConcurrentHashMap<Object, Object> attributes;

	public int getId() {
		if (id != -1) {
			newId = id;
			id = -1;
		}
		return newId;
	}

	@Override
	public Item clone() {
		return new Item(getId(), amount);
	}

	public Item(int id) {
		this(id, 1);
	}

	public Item(int id, int amount) {
		this(id, amount, false);
	}

	public Item(int id, int amount, boolean amt0) {
		this.newId = id;
		this.id = -1;
		this.amount = amount;
		if (this.amount <= 0 && !amt0) {
			this.amount = 1;
		}
	}

	public ItemDefinitions getDefinitions() {
		return ItemDefinitions.getItemDefinitions(getId());
	}

	public int getEquipId() {
		return ItemsEquipIds.getEquipId(getId());
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setId(int id) {
		this.newId = id;
	}

	public int getAmount() {
		return amount;
	}

	public String getName() {
		return getDefinitions().getName();
	}

	public boolean isNote() {
		return getDefinitions().isNoted();
	}

	public ItemDefinitions getDefs() {
		return ItemDefinitions.getItemDefinitions(getId());
	}

    public boolean isStackable() {
        if (attributes != null && !attributes.isEmpty()) 
        	return false;  
        return getDefinitions().isStackable();
    }

	public ItemDefinitions getDefs(int itemId) {
		return ItemDefinitions.getItemDefinitions(itemId);
	}

	public boolean hasNote() {
		String getOriginalName = getDefinitions().getName().toLowerCase();
		String nextItem = getDefs(getId() + 1).getName().toLowerCase();
		boolean isNewItemNoted = getDefs(getId() + 1).isNoted();
		return getOriginalName.equals(nextItem) && isNewItemNoted == true;
	}

	public int getNotedId() {
		return (getId() + 1);
	}

	public static String getItemName(Item item) {
		return ItemDefinitions.getItemDefinitions(item.getId()).getName();
	}

	public ConcurrentHashMap<Object, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(ConcurrentHashMap<Object, Object> attributes) {
		this.attributes = attributes;
	}
	
    public <T> void setAttribute(Object key, T value) {
        if(attributes == null) attributes = new ConcurrentHashMap<>();
        attributes.put(key, value);
    }
    
    public void removeAttribute(Object key) {
        if(attributes == null) return;
        attributes.remove(key);
    }
	
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(Object key) {
        if(attributes == null) return null;
        Object value = attributes.get(key);
        if(value == null) return null;
        return (T) value;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getAttributeOrDefault(Object key, T def) {
        if(attributes == null) return def;
        return (T) attributes.getOrDefault(key, def);
    }

}
