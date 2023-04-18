package com.wyl.super_robot.openai.exception;

/**
 * Custom exception class for chat-related errors
 *
 * @author wyl
 */
public class ChatException extends RuntimeException {


    /**
     * Constructs a new ChatException with the specified detail message.
     *
     * @param msg the detail message (which is saved for later retrieval by the getMessage() method)
     */
    public ChatException(String msg) {
        super(msg);
    }


}
