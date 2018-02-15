package com.onlineslotsgo.hesh;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import java.util.ArrayList;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;


public class Feedback extends AppCompatActivity {
    private static final String TAG = Feedback.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_feedback);

        ArrayList<ChatMessage> list = init();

        ChatView view = findViewById(R.id.chat_view);
        view.addMessages(list);
        view.setOnSentMessageListener(chatMessage -> {
            chatMessage.setUserName(getString(R.string.you));
            chatMessage.setTimestamp(System.currentTimeMillis());
            return true;
        });
    }

    private ArrayList<ChatMessage> init() {
        ArrayList<ChatMessage> list = new ArrayList<>();

        String[] names = getResources().getStringArray(R.array.users);
        String[] comments = getResources().getStringArray(R.array.comments);

        for (int i = 0; i < names.length; ++i) {
            ChatMessage message = new ChatMessage(comments[i],
                    names[i],
                    System.currentTimeMillis(),
                    ChatMessage.Type.RECEIVED);
            message.setTimestamp(System.currentTimeMillis());
            list.add(message);
        }

        return list;
    }

    @NonNull
    public static Intent getFeedbackActivityIntent(Context context) {
        return new Intent(context, Feedback.class);
    }
}
