package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    //Method to create a new message
    public Message createMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Message (posted_by,message_text,time_posted_epoch) VALUES (?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS );

            //Validations 
            if(message.message_text.isEmpty() || message.message_text.length() >= 255 || message.posted_by <=0 ){
                return null;
            }
            preparedStatement.setInt(1,message.getPosted_by());
            preparedStatement.setString(2,message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()){
                int generated_message_id  = resultSet.getInt("message_id");
                message.setMessage_id(generated_message_id);
                return message;
            }
         } 
         catch(SQLException e){
                System.out.println(e.getMessage());
            }
            return null;
    }

    //Method to return all messages
    public List<Message> getAllMessages() throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
        String sql = "SELECT * FROM Message;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        while(resultSet.next()){
            Message message = new Message( resultSet.getInt("message_id"),
                        resultSet.getInt("posted_by"),
                        resultSet.getString("message_text"),
                        resultSet.getLong("time_posted_epoch"));
                        messages.add(message);
        }
    } catch(SQLException e){
        System.out.println(e.getMessage());
    }
    return messages;
    }

    //Method to get a message based on message_id
    public Message getMessageBasedOnId(int messageId) {
        Connection connection = ConnectionUtil.getConnection();
        //List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Message msg = new Message( resultSet.getInt("message_id"),
                resultSet.getInt("posted_by"),
                resultSet.getString("message_text"),
                resultSet.getLong("time_posted_epoch"));
                System.out.println("Check 3: DAO " + msg);
                //messages.add(msg);
                return msg;
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        //return messages;
        return new Message();
    }
    
}
