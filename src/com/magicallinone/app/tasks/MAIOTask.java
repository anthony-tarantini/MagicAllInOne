package com.magicallinone.app.tasks;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.magicallinone.app.application.MAIOApplication;
import com.magicallinone.app.datasets.TasksTable;
import com.magicallinone.app.models.Task;
import com.magicallinone.app.providers.MAIOContentProvider;

/**
 * Created by tony on 2014-03-20.
 */
public abstract class MAIOTask implements Runnable  {

    private final Context mContext;
    private final Uri mTaskId;

    public MAIOTask(final Context context, final Uri taskId) {
        mContext = context;
        mTaskId = taskId;
    }

    public Context getContext() {
        return mContext;
    }

    public Uri getTaskId() {
        return mTaskId;
    }

    public void run() {
        try {
            notifyRunning();
            executeTask();
            onSuccess();
        } catch (final Exception exception) {
            onFailure();
        } finally{
            Log.d(MAIOApplication.DEBUG_TAG, "complete");
        }
    }

    private void notifyRunning() {
        final ContentResolver contentResolver = getContext().getContentResolver();
        final String whereClause = TasksTable.Columns.TASK_ID + "=? AND " + TasksTable.Columns.STATE + "<>?";
        final String[] whereArguments = new String[] { mTaskId.toString(), TasksTable.State.RUNNING };
        final Task task = new Task(mTaskId.toString(), TasksTable.State.RUNNING, System.currentTimeMillis());
        final ContentValues contentValues = TasksTable.getContentValues(task);
        final int rows = contentResolver.update(MAIOContentProvider.Uris.TASKS_URI, contentValues, whereClause, whereArguments);
        if (rows == 0) {
            final String queryWhereClause = TasksTable.Columns.TASK_ID + "=? AND " + TasksTable.Columns.STATE + "=?";
            final String[] queryWhereArguments = new String[] { mTaskId.toString(), TasksTable.State.RUNNING };

            final Cursor cursor = contentResolver.query(MAIOContentProvider.Uris.TASKS_URI, null, queryWhereClause, queryWhereArguments, null);
            try {
                if (cursor.getCount() != 0)
                    return;
            } finally {
                cursor.close();
            }
            contentResolver.insert(MAIOContentProvider.Uris.TASKS_URI, contentValues);

        }
        contentResolver.notifyChange(MAIOContentProvider.Uris.TASKS_URI, null);
    }

    private void onFailure() {
        notifyState(TasksTable.State.FAILURE);
        Log.d(MAIOApplication.DEBUG_TAG, "fail");
    }

    private void onSuccess() {
        notifyState(TasksTable.State.SUCCESS);
        Log.d(MAIOApplication.DEBUG_TAG, "success");
    }

    private void notifyState(final String state) {
        final ContentResolver contentResolver = getContext().getContentResolver();
        final String whereClause = TasksTable.Columns.TASK_ID + "=?";
        final String[] whereArguments = new String[] { mTaskId.toString() };
        final ContentValues contentValues = new ContentValues();
        contentValues.put(TasksTable.Columns.STATE, state);
        contentValues.put(TasksTable.Columns.TASK_ID, mTaskId.toString());
        contentValues.put(TasksTable.Columns.TIME, System.currentTimeMillis());
        contentResolver.update(MAIOContentProvider.Uris.TASKS_URI, contentValues, whereClause, whereArguments);
        contentResolver.notifyChange(MAIOContentProvider.Uris.TASKS_URI, null);
    }

    public abstract void executeTask() throws Exception;
}
