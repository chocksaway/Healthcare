package com.chocksaway.healthcare.testutils;

import org.modelmapper.ModelMapper;

public class ModelMapperTestFixture {
    public static ModelMapper createConfiguredMapper() {
        return new ModelMapper();
    }
}

