package com.epam.esm.service;

import static com.epam.esm.utils.Constants.*;

public enum Directory {
    USERS(USERS_DIR),
    CERTIFICATES(CERTIFICATES_DIR),
    TAGS(TAGS_DIR);

    private final String dir;

    Directory(String dir) {
        this.dir = dir;
    }

    public String getDir() {
        return dir;
    }
}
