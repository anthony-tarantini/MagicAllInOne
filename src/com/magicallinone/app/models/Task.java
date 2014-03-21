package com.magicallinone.app.models;

/**
 * Created by tony on 2014-03-21.
 */
public class Task {
    public String task_id;
    public String state;
    public long time;

    public Task(final String task_id, final String state, final long time) {
        this.task_id = task_id;
        this.state = state;
        this.time = time;
    }
}
