package com.frq.postgres;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLEventHandler implements RequestHandler<S3Event, Boolean> {
    private static final Dao<Customer, Integer> CUSTOMER_DAO = new PostgresSqlDao();
    private static final AmazonS3 s3Client = AmazonS3Client.builder()
            .withCredentials(new DefaultAWSCredentialsProviderChain())
            .build();
    @Override
    public Boolean handleRequest(S3Event input, Context context) {

        try {
            Customer customer = getCustomer(1);
        } catch (Exception ex) {
            ex.printStackTrace();
//            Logger.log(Level.WARNING, ex.getMessage());
        }

        // Test whether a customer can be added to the database
        Customer firstCustomer =
                new Customer(1, "Manuel", "Kelley", "ManuelMKelley@jourrapide.com");
        Customer secondCustomer =
                new Customer(2, "Joshua", "Daulton", "JoshuaMDaulton@teleworm.us");
        Customer thirdCustomer =
                new Customer(3, "April", "Ellis", "AprilMellis@jourrapide.com");
        addCustomer(firstCustomer).ifPresent(firstCustomer::setId);
        addCustomer(secondCustomer).ifPresent(secondCustomer::setId);
        addCustomer(thirdCustomer).ifPresent(thirdCustomer::setId);
        return true;
    }

    public static Customer getCustomer(int id) throws Exception {
        Optional<Customer> customer = CUSTOMER_DAO.get(id);
        return customer.orElseThrow(Exception::new);
    }

    public static Optional<Integer> addCustomer(Customer customer) {
        return CUSTOMER_DAO.save(customer);
    }
}
