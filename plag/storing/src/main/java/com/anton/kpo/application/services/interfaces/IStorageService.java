package com.anton.kpo.application.services.interfaces;

import java.io.IOException;
import java.io.InputStream;

public interface IStorageService {
    public void saveFile(InputStream is, String fileName) throws IOException;
    public InputStream getFile(String fileName) throws IOException;
}
