package com.mobility42.azurechatr;

/**
 * Represents an item in a Chat conversation
 */
public class FeedChat {

    /**
     * Item text
     */
    private String mText;

    /**
     * Item Id
     */
    private String mId;

    private String status;

    /**
     * Indicates who sent the message
     */
    private String mUserName;

    private boolean IsTheDeviceUser;

    /**
     * Indicates when the message was posted
     */
    private String mTimeStamp;

    /**
     * ChatItem constructor
     */
    public FeedChat() {

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
    public FeedChat(String text, String username, String id, boolean isTheDeviceUser) {
        this.setText(text);
        this.setUserName(username);
        this.setId(id);
        this.setIsTheDeviceUser(isTheDeviceUser);
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
    public String getTimeStamp() {
        return mTimeStamp;
    }

    /**
     * Sets the item TimeStamp
     *
     * @param timestamp
     *            timestamp to set
     */
    public final void setTimeStamp(String timestamp) {
        mTimeStamp = timestamp;
    }



    public void setIsTheDeviceUser(boolean isTheDeviceUser) {
        IsTheDeviceUser = isTheDeviceUser;
    }

    public boolean isTheDeviceUser() {
        return IsTheDeviceUser;
    }

    /**
     * Item Id
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


}
