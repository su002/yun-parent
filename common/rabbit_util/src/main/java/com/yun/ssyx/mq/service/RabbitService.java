package com.yun.ssyx.mq.service;

public interface RabbitService {

    public boolean sendMessage( String exchange , String routeKey , Object msg);

}
