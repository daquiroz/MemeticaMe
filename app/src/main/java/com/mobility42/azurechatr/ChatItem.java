package com.mobility42.azurechatr;

import java.util.*;

/**
 * Represents an item in a Chat conversation
 */
public class ChatItem {

	/**
	 * Item text
	 */
	@com.google.gson.annotations.SerializedName("text")
	private String mText;

	/**
	 * Item Id
	 */
	@com.google.gson.annotations.SerializedName("id")
	private String mId;


	@com.google.gson.annotations.SerializedName("idchat")
	private String mIdChat;

	/**
	 * Indicates who sent the message
	 */
	@com.google.gson.annotations.SerializedName("username")
	private String mUserName;

	/**
	 * Indicates when the message was posted
	 */
	@com.google.gson.annotations.SerializedName("timestamp")
	private Date mTimeStamp;

	/**
	 * Indicates the status of the message
	 */
	@com.google.gson.annotations.SerializedName("status")
	private String mStatus;
	/**
	 * ChatItem constructor
	 */
	public ChatItem() {

	}

	@Override
	public String toString() {
		return getText();
	}

	/**
	 * Initializes a new ChatItem
	 *
	 * @param text
	 *            The item text
	 * @param username
	 *            The item username
	 * @param id
	 *            The item id
	 */
	public ChatItem(String text, String username, String id, String mIdChat) {
		this.setText(text);
		this.setUserName(username);
		this.setId(id);
		this.setmIdChat(mIdChat);
	}

	/**
	 * Returns the item text
	 */
	public String getText() {
		return mText;
	}

	/**
	 * Sets the item text
	 *
	 * @param text
	 *            text to set
	 */
	public final void setText(String text) {
		mText = text;
	}

	/**
	 * Returns the item id
	 */
	public String getId() {
		return mId;
	}

	/**
	 * Sets the item id
	 *
	 * @param id
	 *            id to set
	 */
	public final void setId(String id) {
		mId = id;
	}

	/**
	 * Returns the item username
	 */
	public String getUserName() {
		return mUserName;
	}

	public String getStatus() {
		return mStatus;
	}


	public final void setStatus(String status) {
		mStatus=status;
	}



	/**
	 * Sets the item username
	 *
	 * @param username
	 *            username to set
	 */
	public final void setUserName(String username) {
		mUserName = username;
	}

	/**
	 * Returns the item TimeStamp
	 */
	public Date getTimeStamp() {
		return mTimeStamp;
	}

	/**
	 * Sets the item TimeStamp
	 *
	 * @param timestamp
	 *            timestamp to set
	 */
	public final void setTimeStamp(Date timestamp) {
		mTimeStamp = timestamp;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof ChatItem && ((ChatItem) o).mId == mId;
	}

	/**
	 * Item Id
	 */
	public String getmIdChat() {
		return mIdChat;
	}

	public void setmIdChat(String mIdChat) {
		this.mIdChat = mIdChat;
	}
}
