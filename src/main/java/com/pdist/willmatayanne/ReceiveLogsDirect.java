package com.pdist.willmatayanne;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class ReceiveLogsDirect {


    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) throws IOException, TimeoutException {
        System.out.println("Aluna: Willma Tayanne Costa da Silva");

        ConnectionFactory connnectionFactory = new ConnectionFactory();
        connnectionFactory.setHost("localhost");
        connnectionFactory.setUsername("guest");
        connnectionFactory.setPassword("guest");

        Connection con = connnectionFactory.newConnection();
        Channel channel = con.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName, EXCHANGE_NAME, "gato");
        channel.queueBind(queueName, EXCHANGE_NAME, "cachorro");

        System.out.println("[*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback callback = (consumerTag, delivery) -> {
            String text = new String(delivery.getBody(), "UTF-8");
            System.out.println("[x] Received '" + delivery.getEnvelope().getRoutingKey() + "': '" + text + "'");
        };

        channel.basicConsume(queueName, true, callback, consumerTag -> {});
    }


}
