package com.pdist.willmatayanne;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;


public class EmitLogDirect {

    private static String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) {
        System.out.println("Aluna: Willma Tayanne Costa da Silva");

        ConnectionFactory connnectionFactory = new ConnectionFactory();
        connnectionFactory.setHost("localhost");
        connnectionFactory.setUsername("guest");
        connnectionFactory.setPassword("guest");

        Scanner scanner = new Scanner(System.in);
        String[] bindingKeys = new String[]{"gato","cachorro"};
        int op = 0;

        for(;;) {
            System.out.print("Deseja enviar a mensagem para gatos ou cachorros? (1-Gato | 2-Cachorro): ");
            try {
                op = scanner.nextInt();
            } catch (Exception e) {
                new Exception("Erro ao ler opção!");
            }

            if( op != 1 && op != 2 )
                System.out.println("\nOpção invalida, tente novamente\n");
            else break;
        }


        System.out.print("\nEscreva a frase a ser enviada: ");
        scanner = new Scanner(System.in);
        String sendText = scanner.nextLine();
        String bindingKey = bindingKeys[op-1];

        try (Connection con = connnectionFactory.newConnection();
             Channel channel = con.createChannel();) {
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            channel.basicPublish(EXCHANGE_NAME, bindingKey, null, sendText.getBytes("UTF-8"));
            System.out.println(String.format("\nEnviando mensagem de " +  sendText + " para " + bindingKey +".\n"));
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }


}
