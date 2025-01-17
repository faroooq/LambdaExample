package com.frq.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.frq.example.model.Employee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 */
public class HelloWorld
{
    //String input/output
    public String handler(String name){
        return "Hello " + name;
    }

    //boolean true
    public boolean handlerBoolean(boolean flag){
        return !flag;
    }

    //List of integer and return sum of all the passed integers [1,2,3,4]
    public int handlerList(List<Integer> ints) {
        return ints.stream().mapToInt(Integer::intValue).sum();
    }
    /*
     {
        "farooq": 1000,
        "anil": 2000,
        "ravi": 3000
     }
     */
    //List of integer and return sum of all the passed integers
    public Map<String, Integer> handlerMap(Map<String, Integer> inputMap){
        final Map<String, Integer> updatedSalaryMap = new HashMap<>();
        inputMap.forEach((k,v) -> updatedSalaryMap.put(k, v+500));
        return updatedSalaryMap;
    }

    /*
    [
        { "name": "Farooq", "id":101 },
        { "name": "Akash", "id":102 }
    ]
    */
    //POJO
    public List<Employee> handlerPojo(List<Employee> employeeList){
        return employeeList.stream()
                .filter(emp -> emp.getName().startsWith("A"))
                .collect(Collectors.toList());
    }

    //Events
    public String handleEvent(S3Event event, Context context){
        String bucketName = "";
        LambdaLogger logger = context.getLogger();
        //System.out.println("Inside out Lambda");
        logger.log("Inside out Lambda");
        if(!event.getRecords().isEmpty()){
            S3EventNotification.S3Entity s3 = event.getRecords().get(0).getS3();
            bucketName = s3.getBucket().getName();
            logger.log("Object key is:" + s3.getObject().getKey());
        }
        return bucketName;
    }


}
