import android.provider.BaseColumns;

/**
 * Created by Benjamin on 2016-04-30.
 */
public final class EventSplitterContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public EventSplitterContract() {}

    /* Inner class that defines the table contents */
    public static abstract class Event implements BaseColumns {
        public static final String TABLE_NAME = "Event";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }

    public static abstract class User implements BaseColumns {
        public static final String TABLE_NAME = "User";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }

    public static abstract class Event_User implements BaseColumns {
        public static final String TABLE_NAME = "Event_User";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }

    public static abstract class Item implements BaseColumns {
        public static final String TABLE_NAME = "Item";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }

    public static abstract class Group implements BaseColumns {
        public static final String TABLE_NAME = "Group";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }

    public static abstract class Group_User implements BaseColumns {
        public static final String TABLE_NAME = "Group_User";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }

    public static abstract class Tax implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }

    public static abstract class Item_Tax implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
}
