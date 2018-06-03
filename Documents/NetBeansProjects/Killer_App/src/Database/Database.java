/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Models.*;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marc
 */
public class Database implements IDatabase, Serializable{
    
    private String connectionString = "jdbc:mysql://localhost:3306/killer_app";
    private Connection conn;
    
    public void InitConnection() {
        try {
            conn = DriverManager.getConnection(connectionString, "root", "passwordroot");
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

    @Override
    public void RegisterUser(String name, String username, String password) {
        try {
            InitConnection();
            PreparedStatement registerUser = conn.prepareStatement("INSERT INTO User(name, username, password) VALUES(?, ?, ?)");
            registerUser.setString(1, name);
            registerUser.setString(2, username);
            registerUser.setString(3, password);
            registerUser.execute();
        } catch (SQLException ex) {
            throw new IllegalArgumentException(ex);
        }
        finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public ArrayList<ChatMessage> GetChatMessages(String chatname, ArrayList<User> users) {
        ArrayList<ChatMessage> messages = new ArrayList<>();
        try {
            InitConnection();
            PreparedStatement getChatMessages = conn.prepareStatement("SELECT MessageID, sender, text FROM Message WHERE chatname = ? and type = 'ChatMessage'");
            getChatMessages.setString(1, chatname);
            ResultSet resultSet = getChatMessages.executeQuery();
            while(resultSet.next()) {
                messages.add(new ChatMessage(resultSet.getInt("MessageID"),chatname, new User(resultSet.getString("sender")), resultSet.getString("text")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return messages;
    }

    @Override
    public ArrayList<User> GetFriends(User user) {
        ArrayList<User> friends = new ArrayList<>();
        try {
            InitConnection();
            PreparedStatement friend1 = conn.prepareStatement("SELECT user1 FROM friends WHERE user2 = ?");
            PreparedStatement friend2 = conn.prepareStatement("SELECT user2 FROM friends WHERE user1 = ?");
            friend1.setString(1, user.getName());
            friend2.setString(1, user.getName());
            ResultSet resultSet1 = friend1.executeQuery();
            ResultSet resultSet2 = friend2.executeQuery();
            while(resultSet1.next()) {
                friends.add(new User(resultSet1.getString("user1")));
            }
            while(resultSet2.next()) {
                friends.add(new User(resultSet2.getString("user2")));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return friends;
    }

    @Override
    public User Login(String username, String password) {
        try {
            InitConnection();
            PreparedStatement loginstatement = conn.prepareStatement("SELECT name FROM User WHERE username=? AND password=?");
            loginstatement.setString(1, username);
            loginstatement.setString(2, password);
            ResultSet resultSet = loginstatement.executeQuery();
            if(resultSet.next()) {        
                String name = resultSet.getString("name");
                return new User(name, GetFriends(new User(name)), Status.Online);
            }
        } catch (SQLException ex) {
            return null;
        }
        finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    @Override
    public ArrayList<ChatInfo> GetChats(User user) {
        ArrayList<ChatInfo> chats = new ArrayList<ChatInfo>();
        try {
            InitConnection();
            PreparedStatement chatStatement = conn.prepareStatement("SELECT chat FROM user_in_chat WHERE user = ?;");
            PreparedStatement numberOfMembers = conn.prepareStatement("SELECT count(user) as users FROM user_in_chat WHERE chat = ?;");
            chatStatement.setString(1, user.getName());
            ResultSet resultSet = chatStatement.executeQuery();
            while(resultSet.next()) {
                String chatname = resultSet.getString("chat");
                numberOfMembers.setString(1, chatname);
                ResultSet count = numberOfMembers.executeQuery();
                count.next();
                int amountOfMembers = count.getInt("users");
                chats.add(new ChatInfo(chatname, amountOfMembers, 0));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return chats;
    }

    @Override
    public void FriendRequest(User reciever, User sender) {
        try {
            InitConnection();
            PreparedStatement friendRequest = conn.prepareStatement("INSERT into Message(reciever, sender, text, type) VALUES(?, ?, 'I would like to be your friend', 'FriendRequest')");
            friendRequest.setString(1, reciever.getName());
            friendRequest.setString(2, sender.getName());
            friendRequest.execute();
        } catch (SQLException ex) {
            throw new IllegalArgumentException();
        }
        finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void AcceptFriendRequest(IMessage message, User reciever) {
        try {
            InitConnection();
            PreparedStatement friends = conn.prepareStatement("INSERT INTO Friends(user1, user2) VALUES(?, ?)");
            friends.setString(1, reciever.getName());
            friends.setString(2, message.getSendername());
            friends.execute();
            RemoveMessage(message);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void RemoveMessage(IMessage message) {
        try {
            InitConnection();
            PreparedStatement removeMessageStatement = conn.prepareCall("DELETE FROM Message WHERE MessageID = ?");
            removeMessageStatement.setInt(1, message.getID());
            removeMessageStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void RemoveFriend(User user1, User user2) {
        try {
            InitConnection();
            PreparedStatement removeFriendStatement = conn.prepareStatement("DELETE FROM friends WHERE (User1 = ? AND User2 = ?) OR (User2 = ? AND User1 = ?)");
            removeFriendStatement.setString(1, user1.getName());
            removeFriendStatement.setString(2, user2.getName());
            removeFriendStatement.setString(3, user1.getName());
            removeFriendStatement.setString(4, user2.getName());
            removeFriendStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void CreateChat(ArrayList<User> users, String chatname) {
        try {
            InitConnection();
            PreparedStatement createChatStatement = conn.prepareStatement("INSERT INTO Chat(name) VALUES (?)");
            PreparedStatement addUserStatement = conn.prepareStatement("INSERT INTO user_in_chat(user, chat) VALUES (?, ?)");
            createChatStatement.setString(1, chatname);     
            createChatStatement.execute();
            for(int i =0; i < users.size(); i++) {
                addUserStatement.setString(1, users.get(i).getName());
                addUserStatement.setString(2, chatname);
                addUserStatement.execute();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    
    public ArrayList<User> GetChatUsers(String chatname) {
        ArrayList<User> users = new ArrayList<>();
        try {
            InitConnection();
            PreparedStatement getUsersStatement = conn.prepareStatement("SELECT user FROM user_in_chat WHERE chat=?");
            getUsersStatement.setString(1, chatname);
            ResultSet resultSet = getUsersStatement.executeQuery();
            while(resultSet.next()) {
                users.add(new User(resultSet.getString("user")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return users;
    }
    
    public Chat GetChat(String chatname) {
        ArrayList<User> users = GetChatUsers(chatname);
        ArrayList<ChatMessage> messages = GetChatMessages(chatname, users);
        return new Chat(chatname, users, messages);
    }

    @Override
    public void SendChatMessage(ChatMessage chatMessage) {
        try {
            InitConnection();
            PreparedStatement sendChatMessageStatement = conn.prepareStatement("INSERT INTO Message(chatname, sender, text, type) VALUES (?, ?, ?, 'ChatMessage')");
            sendChatMessageStatement.setString(1, chatMessage.GetReciever());
            sendChatMessageStatement.setString(2, chatMessage.getSendername());
            sendChatMessageStatement.setString(3, chatMessage.getText());
            sendChatMessageStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public ArrayList<IMessage> GetMessages(User user) {
        ArrayList<IMessage> messages = new ArrayList<>();
        try {
            InitConnection();
            PreparedStatement getMessagesStatement = conn.prepareStatement("SELECT MessageID, sender, text, type, chatname FROM Message WHERE reciever = ?");
            getMessagesStatement.setString(1, user.getName());
            ResultSet resultSet = getMessagesStatement.executeQuery();
            while(resultSet.next()) {
                int id = resultSet.getInt("MessageID");
                String sender = resultSet.getString("sender");
                String text = resultSet.getString("text");
                String type = resultSet.getString("type");
                String chatname = resultSet.getString("chatname");
                if(type.equals("PrivateMessage")) {
                    PrivateMessage privateMessage = new PrivateMessage(id, user, new User(sender), text);
                    messages.add(privateMessage);
                }
                else if(type.equals("FriendRequest")) {
                    FriendRequest friendRequest = new FriendRequest(id, user, new User(sender), text);
                    messages.add(friendRequest);
                }
                else if(type.equals("GroupInvitation")) {
                    GroupInvitation groupInvitation = new GroupInvitation(id, user, new User(chatname), text);
                    messages.add(groupInvitation);
                }
                else {
                    continue;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return messages;
    }

    @Override
    public void PrivateMessage(PrivateMessage privateMessage) {
        try {
            InitConnection();
            PreparedStatement privateMessageStatement = conn.prepareStatement("INSERT INTO Message(reciever, sender, text, type) VALUES (?, ?, ?, 'PrivateMessage')");
            privateMessageStatement.setString(1, privateMessage.getReciever().getName());
            privateMessageStatement.setString(2, privateMessage.getSendername());
            privateMessageStatement.setString(3, privateMessage.getText());
            privateMessageStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void GroupInvitation(GroupInvitation groupInvitation) {
        try {
            InitConnection();
            PreparedStatement groupInvitationStatement = conn.prepareStatement("INSERT INTO Message(chatname, reciever, text, type) VALUES (?, ?, ?, 'GroupInvitation')");
            groupInvitationStatement.setString(1, groupInvitation.getSendername());
            groupInvitationStatement.setString(2, groupInvitation.GetReciever().getName());
            groupInvitationStatement.setString(3, groupInvitation.getText());
            groupInvitationStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void AcceptGroupInvitation(GroupInvitation groupInvitation) {
        try {
            InitConnection();
            PreparedStatement AcceptGroupInvitationsStatement = conn.prepareStatement("INSERT INTO user_in_chat(user, chat) VALUES (?,?)");
            AcceptGroupInvitationsStatement.setString(1, groupInvitation.GetReciever().getName());
            AcceptGroupInvitationsStatement.setString(2, groupInvitation.getSendername());
            AcceptGroupInvitationsStatement.execute();
            RemoveMessage(groupInvitation);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void RemoveUserFromChat(User user, Chat chat) {
        try {
            InitConnection();
            PreparedStatement removeUserStatement = conn.prepareStatement("DELETE FROM user_in_chat WHERE user = ? AND chat = ?");
            removeUserStatement.setString(1, user.getName());
            removeUserStatement.setString(2, chat.GetName());
            removeUserStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
