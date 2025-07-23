package com.myprojects.convo.Database_Setup;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.myprojects.convo.Modal.Personal__message_List; // Apna MessagesBean model import karein

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import java.util.concurrent.TimeUnit;

public class WebSocketManager {

    private WebSocket webSocket;
    private static WebSocketManager instance;
    private OkHttpClient client;

    private static final String TAG = "WebSocketManager";

    private String wsUrl;
    private Gson gson;


    public interface WebSocketListner
    {
        void onConnected();
        void onMessageReceived(Personal__message_List.DataBean.MessagesBean messagesBean);
        void onDisconnected(String reason);
        void onError(String message);
    }

    private WebSocketListner listener;

    private WebSocketManager(String url)
    {
        this.wsUrl = url;
        this.gson = new Gson();
        this.client = new OkHttpClient.Builder().readTimeout(30,TimeUnit.SECONDS).connectTimeout(30,TimeUnit.SECONDS).build();
    }

    public static synchronized WebSocketManager getWebSocketManager(String url)
    {
        if (instance == null)
        {
            instance = new WebSocketManager(url);
        }
        return instance;
    }

    public void setListner(WebSocketListner listner)
    {
       this.listener = listner;
    }

    public void connect()
    {
        if (webSocket != null)
        {
            Log.d(TAG,"Web Socket Already Connected and Active.");
            if (listener != null)
            {
               listener.onConnected();
            }
            return;
        }


        if (webSocket != null)
        {
            webSocket.close(1000,"Reconnecting");
            webSocket = null;
        }

        Request request = new Request.Builder().url(wsUrl).build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                Log.d(TAG,"WebSocket Connected: " + response.message());
                if (listener!= null)
                {
                    listener.onConnected();
                }
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                Log.d(TAG,"Received message: " + text);
                if (listener!=null)
                {
                    try {
                        Personal__message_List.DataBean.MessagesBean messagesBean = gson.fromJson(text, Personal__message_List.DataBean.MessagesBean.class);

                        listener.onMessageReceived(messagesBean);
                    }catch (Exception e)
                    {
                        Log.e(TAG,"Error parsing message: "+ text,e);
                        listener.onError("Failed to parse message: "+e.getMessage());
                    }
                }
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
                Log.d(TAG,"Received bytes: "+bytes.hex());
            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
                Log.e(TAG, "WebSocket Failure: "+t.getMessage(),t);

                String errorMessage = (response != null) ? response.message() : t.getMessage();
                if (listener != null)
                {
                    listener.onError("WebSocket connection error: " + errorMessage);
                }
                WebSocketManager.this.webSocket = null;
            }

            @Override
            public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                Log.d(TAG, "WebSocket Closing: " + code + " / " + reason);
            }

            @Override
            public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                Log.d(TAG,"WebSocket Closed: "+code+ " / "+reason);
                if (listener != null)
                {
                    listener.onDisconnected(reason);
                }
                WebSocketManager.this.webSocket = null;
            }
        });
    }

    public void sendMessage(Personal__message_List.DataBean.MessagesBean messagesBean)
    {
        String jsonToSend = gson.toJson(messagesBean); // JSON स्ट्रिंग बनाएं
        Log.d(TAG, "Attempting to send JSON: " + jsonToSend); // इसे लॉग करें

        if (webSocket != null && webSocket.send(gson.toJson(messagesBean)))
        {
            Log.d(TAG,"Message sent: "+ gson.toJson(messagesBean));
        }
        else
        {
            Log.e(TAG,"Failed to send message, WebSocket not connected or failed to send.");
            if (listener != null)
            {
                listener.onError("Failed to send message. Not connected.");
            }
        }
    }

    public void disconnect()
    {
        if (webSocket != null)
        {
            webSocket.close(1000,"User disconnected");
            webSocket = null;
            Log.d(TAG,"WebSocket Disconnected.");
        }
    }


    public boolean isConnected()
    {
        return webSocket != null;
    }

    public void sendAuthMessage(int userId) {

        String userCr = String.valueOf(userId);
        if (webSocket != null && webSocket.send("{\"type\":\"auth\", \"userId\":" + userId + "}")) {
            Log.d(TAG, "Auth message sent: {\"type\":\"auth\", \"userId\":" + userId + "}");
        } else {
            Log.e(TAG, "Failed to send auth message. WebSocket not connected.");
            if (listener != null) {
                listener.onError("Failed to send auth message. Not connected.");
            }
        }
    }


}
