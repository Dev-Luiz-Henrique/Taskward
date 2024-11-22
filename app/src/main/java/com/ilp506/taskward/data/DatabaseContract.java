package com.ilp506.taskward.data;

public final class DatabaseContract {

    private DatabaseContract() {}

    public static class UserTable {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PHOTO = "photo";
        public static final String COLUMN_POINTS = "points";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String[] ALL_COLUMNS = {
                COLUMN_ID, COLUMN_NAME, COLUMN_PHOTO, COLUMN_POINTS, COLUMN_CREATED_AT
        };
    }
}
