package Controller;

import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register",this::postNewAccountHandler);
        app.post("/login",this::postExistingAccountHandler);
        app.post("/messages", this::postNewMessageHandler);
        app.get("/messages", this::getMessageHandler);
        app.get("/messages/{message_id}", this::getMessageBasedOnIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageBasedOnIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUserHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json(context);
    }

    //API 1: POST localhost:8080/register
    //Handler to post a new account
    private void postNewAccountHandler(Context context) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(),Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount!=null){
            context.json(mapper.writeValueAsString(addedAccount));
        }
        else{
            context.status(400);
        }

    }

    //API 2: POST localhost:8080/login
     //Handler to post an existing account
     private void postExistingAccountHandler(Context context) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(),Account.class);
        Account existingAccount = accountService.existingAccount(account);
        if((existingAccount!= null) && existingAccount.getUsername() == account.getUsername() && existingAccount.getPassword() == account.getPassword()){
            context.json(mapper.writeValueAsString(existingAccount));
        }
        else{
            context.status(401);
        }

    }

    // API 3: POST localhost:8080/messages
    //Handler to post a new message
    private void postNewMessageHandler(Context context) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(),Message.class);
        Message newMessage = messageService.newMessage(message);
        if(newMessage!=null){
            context.json(mapper.writeValueAsString(newMessage));
        }
        else{
            context.status(400);
        }
    }

    //API 4: GET localhost:8080/messages
    //Handler to retrieve all messages
    private void getMessageHandler(Context cxt) throws SQLException{
        List<Message> message = messageService.getAllMessages();
        cxt.json(message);
    }

    //API 5: GET localhost:8080/messages/{message_id}
    //Handler to retrieve a message based on message_id
    private void getMessageBasedOnIdHandler (Context ctx) throws SQLException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message msg = messageService.getMessageBasedOnId(message_id);
        
        if(msg!= null){
            //Found the message in database
            ctx.status(200);
            ctx.json(msg);
        }
        else {
            //Message is not present in database
            ctx.status(200);
        }
    }

    //API 6: DELETE localhost:8080/messages/{message_id}
    //Handler to delete a message based on message_id
    private void deleteMessageBasedOnIdHandler(Context ctx){
       int message_id = Integer.parseInt(ctx.pathParam("message_id"));
       Message deletedMessage = messageService.deleteMessageBasedOnId(message_id);

       if(deletedMessage!= null){
        //Message was deleted
        ctx.status(200);
        ctx.json(deletedMessage);
       }
       else {
        //Message was not deleted
        ctx.status(200);
       }
    }

    //API 7: PATCH localhost:8080/messages/{message_id}
    //Handler to update a message based on message_id
    private void updateMessageHandler (Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message newMessage = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessage(message_id, newMessage.message_text);

        if(updatedMessage!= null){
            //Message was updated
            ctx.status(200);
            ctx.json(updatedMessage);
        }
        else {
            ctx.status(400);
        }
    }

    //API 8: GET localhost:8080/accounts/{account_id}/messages
    //Handler to get all messages by a user
    private void getAllMessagesByUserHandler(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesByUser(id);
        if(messages != null){
            ctx.status(200);
            ctx.json(messages);
        }
        else {
            ctx.status(200);
        }
    }

}