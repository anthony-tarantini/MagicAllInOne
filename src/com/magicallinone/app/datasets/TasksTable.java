package com.magicallinone.app.datasets;

import android.content.ContentValues;

import com.magicallinone.app.models.Task;
import com.magicallinone.app.providers.DatabaseTable;

import java.util.Map;

/**
 * Created by tony on 2014-03-21.
 */
public class TasksTable extends DatabaseTable {
    public static final String TABLE_NAME = "tasks";

    public static final class State {
        public static final String RUNNING = "running";
        public static final String SUCCESS = "success";
        public static final String FAILURE = "failure";
    }

    public static final class Columns extends DatabaseTable.Columns {
        public static final String TASK_ID = "task_id";
        public static final String STATE = "state";
        public static final String TIME = "time";
    }

    public static ContentValues getContentValues(Task task){
        final ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.TASK_ID, task.task_id);
        contentValues.put(Columns.STATE, task.state);
        contentValues.put(Columns.TIME, task.time);
        return contentValues;
    }

    @Override
    protected Map<String, String> getColumnTypes() {
        final Map<String, String> columnTypes = super.getColumnTypes();
        columnTypes.put(Columns.TASK_ID, "TEXT");
        columnTypes.put(Columns.STATE, "TEXT");
        columnTypes.put(Columns.TIME, "INTEGER");
        return columnTypes;
    }

    @Override
    protected String getConstraint() {
        return "UNIQUE (" + Columns.TASK_ID + ") ON CONFLICT REPLACE";
    }

    @Override
    public String getName() {
        return TABLE_NAME;
    }
}
