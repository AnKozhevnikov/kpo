package com.anton.kpo.application.services.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

public interface IHasherService {
    public String hashFile(InputStream file) throws IOException, NoSuchAlgorithmException;
}
