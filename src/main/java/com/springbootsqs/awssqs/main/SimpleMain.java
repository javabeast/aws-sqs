package com.springbootsqs.awssqs.main;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;


import java.util.List;

public class SimpleMain {

    private String awsAccessKey = "";

    private String awsSecretKey = "";

    private String sqsEndPoint = "";

    public void sendMessage() {

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(sqsEndPoint)
                .withMessageBody("hello from Spring Boot");
        amazonSQS().sendMessage(send_msg_request);
    }

    public void readMessage() {
        final ReceiveMessageRequest receiveMessageRequest =
                new ReceiveMessageRequest(sqsEndPoint)
                        .withMaxNumberOfMessages(1)
                        .withWaitTimeSeconds(3);
        final List<Message> messages =
                amazonSQS().receiveMessage(receiveMessageRequest).getMessages();

        messages.forEach(item -> System.out.println(item));

        //deleteMessage(messages);
    }

    public void deleteMessage(List<Message> messages) {
        for (Message message : messages) {
            String receiptHandle = message.getReceiptHandle();
            amazonSQS().deleteMessage(new DeleteMessageRequest(sqsEndPoint, receiptHandle));
        }
    }

    public AmazonSQS amazonSQS() {
        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
                .withRegion(Regions.AP_SOUTH_1)
                .build();
        return sqs;
    }

    public static void main(String[] args) {
        SimpleMain simpleMain = new SimpleMain();
        //simpleMain.sendMessage();
        simpleMain.readMessage();
    }
}
