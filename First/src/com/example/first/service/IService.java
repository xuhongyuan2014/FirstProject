package com.example.first.service;

public interface IService
{
        void startApp(String packageName);
        
        void stopApp(String packageName);

        void setPass(String pass);
        
        String getPass();
}