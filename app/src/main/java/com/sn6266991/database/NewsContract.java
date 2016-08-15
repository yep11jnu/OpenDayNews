package com.sn6266991.database;

/**
 * Table and column names for local database for this app
 */
interface NewsContract {

    interface ReadNews {
        String TABLE_NAME = "ReadNews";
        String URL = "URL"; // String
    }

    interface Bookmark {
        String TABLE_NAME = "Bookmark";
        String ID = "ID"; // int
        String TITLE = "Title"; // String
        String URL = "URL"; // String
        String CREATED_AT = "CreatedAt"; // long
    }

}
