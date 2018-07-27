package com.trending.trending_bucket.util;

import com.trending.trending_bucket.model.User;

/**
 * Created by prab on 15/02/18.
 */
public interface GetUserCallBack {
    public abstract void done(User returnedUser);
}
