package com.company;

import static com.company.Frame.pathFound;

public class EndNodeFoundException extends Exception{
    @Override
    public void printStackTrace() {
        System.out.println("Error");
    }
}
