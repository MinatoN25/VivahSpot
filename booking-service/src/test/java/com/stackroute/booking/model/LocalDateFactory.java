package com.stackroute.booking.model;

import java.time.LocalDate;

import org.meanbean.lang.Factory;

class LocalDateFactory implements Factory<LocalDate>
{
    @Override
    public LocalDate create()
    {
        return LocalDate.now();
    }
}