package com.newmedia.erxeslibrary.model;



import com.apollographql.apollo.api.Response;
import com.newmedia.erxes.basic.ConversationsQuery;
import com.newmedia.erxes.basic.InsertMessageMutation;
import com.newmedia.erxeslibrary.configuration.Config;


import java.util.ArrayList;
import java.util.List;

public class Conversation {
    public String id;
    public String customerId;
    public String integrationId;
    public String status;
    public String content;
    public String date;
    public boolean isread = true;
    public List<ConversationMessage> conversationMessages = new ArrayList<>();
    public List<String> readUserIds;
    public List<User> participatedUsers;

    static public List<Conversation> convert(Response<ConversationsQuery.Data> response, Config config){
        List<ConversationsQuery.Conversation> data = response.data().conversations();
        List<Conversation> dataConverted = new ArrayList<>();
        Conversation thisO;
        for(ConversationsQuery.Conversation item:data) {
            thisO = new Conversation();
            thisO.id = item._id();
            thisO.date = item.createdAt();
            thisO.content = item.content();
            thisO.status = item.status();
            thisO.customerId = item.customerId();
            thisO.integrationId = item.integrationId();
            if (item.messages() != null && item.messages().size() > 0) {
                for (int i = 0 ; i < item.messages().size() ; i ++) {
                    ConversationMessage message = new ConversationMessage();
                    message.id = item.messages().get(i)._id();
                    message.content = item.messages().get(i).content();
                    message.conversationId = item.messages().get(i).conversationId();
                    message.createdAt = item.messages().get(i).createdAt();
                    message.customerId = item.messages().get(i).customerId();
                    if (item.messages().get(i).internal() != null)
                        message.internal = item.messages().get(i).internal();
                    if (item.messages().get(i).user() != null) {
                        User user = new User();
                        user.id = item.messages().get(i).user()._id();
                        if (item.messages().get(i).user().details() != null) {
                            user.avatar = item.messages().get(i).user().details().avatar();
                            user.fullName = item.messages().get(i).user().details().fullName();
                        }
                        message.user = user;
                    }
                    thisO.conversationMessages.add(message);
                }
            }
            dataConverted.add(thisO);

        }
        return dataConverted;

    }
    static public Conversation update(InsertMessageMutation.InsertMessage a, String message,Config config){
        config.conversationId = a.conversationId();
        Conversation conversation = new Conversation();
        conversation.id = config.conversationId;
        conversation.content = message;
        conversation.status = "open";
        conversation.date = a.createdAt();
        conversation.customerId = config.customerId;
        conversation.integrationId = config.integrationId;
        return conversation;
    }

}
