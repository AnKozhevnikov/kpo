package com.anton.kpo.application.services.interfaces;

import java.io.IOException;
import java.io.InputStream;

public interface IStorageService {
    void saveFile(InputStream is, String fileName) throws IOException;
    InputStream getFile(String fileName) throws IOException;
}
