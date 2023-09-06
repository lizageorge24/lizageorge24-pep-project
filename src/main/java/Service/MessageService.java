package Service;

import java.sql.SQLException;
import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    //Method to create a new message
    public Message newMessage(Message message){
        return messageDAO.createMessage(message);
    }

    //Method to retrieve all messages
    public List<Message> getAllMessages() throws SQLException {
        return messageDAO.getAllMessages();
    }

    //Method to get a message based on message_id
    public Message getMessageBasedOnId(int messageId) {
        return messageDAO.getMessageBasedOnId(messageId);

    }
}
    
