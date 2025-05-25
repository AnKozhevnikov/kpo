package com.anton.kpo.infrasructure.externalapi.interfaces;

public interface IWordCloudApi {
    byte[] generateWordCloud(String text) throws Exception;
}
