package com.andrew;

import com.google.gson.Gson;

import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RestApi {
    public static void main(String[] args) throws Exception {

        Transcript transcript = new Transcript();
        transcript.setAudio_url("https://thinkpoint.files.wordpress.com/2012/12/blaming-god-focus.mp3");
        Gson gson = new Gson();
        String jsonRequest = gson.toJson(transcript);
        System.out.println(jsonRequest);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/transcript"))
                .header("Authorization", Constants.API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println(postResponse.body());

        transcript = gson.fromJson(postResponse.body(), Transcript.class);

        System.out.println(transcript.getId());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/transcript/" + transcript.getId()))
                .header("Authorization", Constants.API_KEY)
                .GET( )
                .build();

        while (true) {
            HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            transcript = gson.fromJson(getResponse.body(), Transcript.class);

            System.out.println(transcript.getStatus());

            if ("completed".equals(transcript.getStatus()) || "error".equals(transcript.getStatus())) {
                break;
            }
            Thread.sleep(2500);
        }

        System.out.println("Transcription Completed");
        System.out.println(transcript.getText());

    }
}
