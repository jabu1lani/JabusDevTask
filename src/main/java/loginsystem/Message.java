/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package loginsystem;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 *
 * @author Jabu
 */

public class Message {
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String content;
    private String messageHash;
    private static List<Message> allMessages = new ArrayList<>();
    private static int totalMessages = 0;
    
    public String getMessageID() { return messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getContent() { return content; }
    public String getMessageHash() { return messageHash; }

    public Message(int messageNumber, String recipient, String content) {
        this.messageID = generateMessageID();
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.content = content;
        this.messageHash = createMessageHash();
        allMessages.add(this);
        totalMessages++;
    }

    private String generateMessageID() {
        Random rand = new Random();
        return String.format("%010d", rand.nextInt(1000000000));
    }

    public boolean checkMessageID() {
        return this.messageID.length() == 10;
    }

    public int checkRecipientCell() {
        if (recipient == null || recipient.isEmpty()) {
            return -1; // Invalid
        }
        // Must start with +27 and have exactly 9 more digits
        return recipient.matches("^\\+27\\d{9}$") ? 1 : 0;
    }

    public String createMessageHash() {
        String[] words = content.split(" ");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length-1] : firstWord;
        return (messageID.substring(0, 2) + ":" + messageNumber + ":" + 
               (firstWord + lastWord)).toUpperCase();
    }

    public String SentMessage() {
        return "Choose action for message:\n1. Send\n2. Store\n3. Disregard";
    }

    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        for (Message msg : allMessages) {
            sb.append("ID: ").append(msg.messageID)
              .append("\nTo: ").append(msg.recipient)
              .append("\nMessage: ").append(msg.content)
              .append("\nHash: ").append(msg.messageHash)
              .append("\n\n");
        }
        return sb.toString();
    }


    public static int returnTotalMessages() {
        return totalMessages;
    }

    public JSONObject storeMessage() {
        JSONObject json = new JSONObject();
        json.put("messageID", this.messageID);
        json.put("messageNumber", this.messageNumber);
        json.put("recipient", this.recipient);
        json.put("content", this.content);
        json.put("messageHash", this.messageHash);
        return json;
    }

    public static JSONArray storeAllMessages() {
        JSONArray jsonArray = new JSONArray();
        for (Message msg : allMessages) {
            jsonArray.put(msg.storeMessage());
        }
        return jsonArray;
    }
    public static void resetMessageSystem() {
    allMessages.clear();
    totalMessages = 0;
}
}