package com.newmedia.erxeslibrary.graphqlfunction;

import android.content.Context;
import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.newmedia.erxes.basic.GetMessengerIntegrationQuery;
import com.newmedia.erxes.basic.MessengerConnectMutation;
import com.newmedia.erxeslibrary.Configuration.Config;
import com.newmedia.erxeslibrary.Configuration.ErxesRequest;
import com.newmedia.erxeslibrary.Configuration.Helper;
import com.newmedia.erxeslibrary.Configuration.ReturnType;
import com.newmedia.erxeslibrary.DataManager;

import javax.annotation.Nonnull;

public class GetInteg {
    final static String TAG = "GETINTEG";
    private ErxesRequest ER;
    private Config config ;
    public GetInteg(ErxesRequest ER, Context context) {
        this.ER = ER;
        config = Config.getInstance(context);

    }
    public void run(){
        ER.apolloClient.query(GetMessengerIntegrationQuery.builder().brandCode(config.brandCode).build())
                .enqueue(request);
    }

    private ApolloCall.Callback<GetMessengerIntegrationQuery.Data> request =  new ApolloCall.Callback<GetMessengerIntegrationQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<GetMessengerIntegrationQuery.Data> response) {
            if(!response.hasErrors()) {


//                config.changeLanguage(response.data().getMessengerIntegration().languageCode());
//                Helper.load_uiOptions(response.data().getMessengerIntegration().uiOptions());
//                Helper.load_messengerData( response.data().getMessengerIntegration().messengerData());
//                Log.d("xaxa","goodway"+response.data().getMessengerIntegration().messengerData().toString());
                ER.notefyAll(ReturnType.INTEGRATION_CHANGED,null ,null);
            }
            else{
                Log.d(TAG, "errors " + response.errors().toString());
                ER.notefyAll(ReturnType.SERVERERROR,null,response.errors().get(0).message());
            }
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            ER.notefyAll(ReturnType.CONNECTIONFAILED,null, e.getMessage());
            Log.d(TAG, "failed ");
            e.printStackTrace();

        }
    };
}
