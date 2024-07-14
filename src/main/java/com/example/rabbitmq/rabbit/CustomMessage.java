package com.example.rabbitmq.rabbit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
// a custom message that we'll pass in our queue
public class CustomMessage {

    private String messageId;
    private String message;
    private Date messageDate;

}
