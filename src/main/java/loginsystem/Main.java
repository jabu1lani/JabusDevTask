/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package loginsystem;


import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.JOptionPane;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LoginSystem loginSystem = new LoginSystem();
        
        System.out.println("Welcome to the Registration System");
        System.out.println("---------------------------------");
        
        // Registration
        System.out.println("\nREGISTRATION");
        System.out.print("Enter username (must contain _ and be ≤5 chars): ");
        String username = scanner.nextLine();
        
        System.out.print("Enter password (≥8 chars, with capital, number, special char): ");
        String password = scanner.nextLine();
        
        System.out.print("Enter SA phone number (+27xxxxxxxxx): ");
        String phone = scanner.nextLine();
        
        String regResult = loginSystem.registerUser(username, password, phone);
        System.out.println(regResult);
        
        // Login
        if (regResult.equals("User registered successfully!")) {
            System.out.println("\nLOGIN");
            System.out.print("Enter username: ");
            String loginUser = scanner.nextLine();
            
            System.out.print("Enter password: ");
            String loginPass = scanner.nextLine();
            
            boolean loginStatus = loginSystem.loginUser(loginUser, loginPass);
            System.out.println(loginSystem.returnLoginStatus(loginStatus, loginUser));
            
            if (loginStatus) {
                JOptionPane.showMessageDialog(null, "Welcome to QuickChat!");
                
                int messageCount = 0;
                String totalInput = JOptionPane.showInputDialog(null, "How many messages would you like to send?");
                int totalToSend = Integer.parseInt(totalInput);
                
                int choice;
                do {
                    // Create menu options array
                    String[] options = {
                        "Send Messages",
                        "Show recently sent messages", 
                        "Store messages to JSON",
                        "Quit"
                    };
                    
                    // Show option dialog
                    choice = JOptionPane.showOptionDialog(
                        null,
                        "Choose an option:",
                        "QuickChat Menu",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]
                    );
                    
                    // Handle choice (choice returns index, so add 1 to match original numbering)
                    switch (choice + 1) {
                        case 1: // Send Messages
                            if (messageCount < totalToSend) {
                                String recipient = JOptionPane.showInputDialog(null, 
                                    "Enter recipient phone number (+27xxxxxxxxx):");
                                
                                if (recipient == null) break; // User cancelled
                                
                                String content = JOptionPane.showInputDialog(null, 
                                    "Enter your message (max 250 chars):");
                                
                                if (content == null) break; // User cancelled
                                
                                if (content.length() > 250) {
                                    JOptionPane.showMessageDialog(null, 
                                        "Message exceeds 250 characters!", 
                                        "Error", 
                                        JOptionPane.ERROR_MESSAGE);
                                    break;
                                }
                                
                                Message msg = new Message(messageCount+1, recipient, content);
                                
                                // Message action handling with JOptionPane
                                String[] actionOptions = {"Send", "Store", "Disregard"};
                                int action = JOptionPane.showOptionDialog(
                                    null,
                                    msg.SentMessage() + "\nSelect action:",
                                    "Message Action",
                                    JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    actionOptions,
                                    actionOptions[0]
                                );
                                
                                switch (action) {
                                    case 0: // Send
                                        JOptionPane.showMessageDialog(null, 
                                            "Message Details:\n" + 
                                            "ID: " + msg.getMessageID() + "\n" +
                                            "Hash: " + msg.createMessageHash() + "\n" +
                                            "To: " + msg.getRecipient() + "\n" +
                                            "Content: " + msg.getContent());
                                        JOptionPane.showMessageDialog(null, "Message sent successfully!");
                                        messageCount++;
                                        break;
                                    case 1: // Store
                                        JSONObject json = msg.storeMessage();
                                        JOptionPane.showMessageDialog(null, 
                                            "Message stored as JSON:\n" + json.toString(),
                                            "JSON Storage",
                                            JOptionPane.INFORMATION_MESSAGE);
                                        break;
                                    case 2: // Disregard
                                        JOptionPane.showMessageDialog(null, "Message disregarded");
                                        break;
                                    default:
                                        if (action == -1) { // User closed dialog
                                            JOptionPane.showMessageDialog(null, "Action cancelled");
                                        }
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, 
                                    "You've reached your message limit.",
                                    "Limit Reached",
                                    JOptionPane.WARNING_MESSAGE);
                            }
                            break;
                            
                        case 2: // Show recent messages
                            JOptionPane.showMessageDialog(null, 
                                "Recent Messages:\n\n" + Message.printMessages(),
                                "Recent Messages",
                                JOptionPane.INFORMATION_MESSAGE);
                            break;
                            
                        case 3: // Store all messages
                            JSONArray jsonArray = Message.storeAllMessages();
                            JOptionPane.showMessageDialog(null, 
                                "All messages stored:\n\n" + jsonArray.toString(2),
                                "All Messages JSON",
                                JOptionPane.INFORMATION_MESSAGE);
                            break;
                            
                        case 4: // Quit
                            JOptionPane.showMessageDialog(null, 
                                "Total messages sent: " + Message.returnTotalMessages() + "\nGoodbye!");
                            break;
                            
                        default:
                            if (choice == -1) { // User closed dialog
                                JOptionPane.showMessageDialog(null, "Goodbye!");
                                choice = 3; // Set to quit value to exit loop
                            }
                    }
                } while (choice != 3 && choice != -1); // Exit on Quit or dialog close
            }
        }
        scanner.close();
    }
}