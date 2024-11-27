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

    public static class RewardTable {
        public static final String TABLE_NAME = "rewards";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_ICON = "icon";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_POINTS_REQUIRED = "points_required";
        public static final String COLUMN_DATE_REDEEMED = "date_redeemed";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String[] ALL_COLUMNS = {
                COLUMN_ID, COLUMN_USER_ID, COLUMN_ICON, COLUMN_TITLE, COLUMN_DESCRIPTION,
                COLUMN_POINTS_REQUIRED, COLUMN_DATE_REDEEMED, COLUMN_CREATED_AT
        };
    }

}
