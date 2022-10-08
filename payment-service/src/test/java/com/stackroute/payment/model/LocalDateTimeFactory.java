package com.stackroute.payment.model;

import java.time.LocalDateTime;

import org.meanbean.lang.Factory;

class LocalDateTimeFactory implements Factory<LocalDateTime>
{
    @Override
    public LocalDateTime create()
    {
        return LocalDateTime.now();
    }
}