package com.github.halfbull.weightlog.weightlog;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.halfbull.weightlog.database.Weight;
import com.github.halfbull.weightlog.database.WeightDao;

import java.util.List;

public class WeightLogViewModel {

    private final WeightDao weightDao;
    @NonNull
    private final WeightsRecycleBinViewModel recycle;

    public WeightLogViewModel(WeightDao weightDao) {
        this.weightDao = weightDao;
        this.recycle = new WeightsRecycleBinViewModel(weightDao);
    }

    void addWeight(final Weight weight) {
        new AsyncTask<Void, Void, Void>() {
            @Nullable
            @Override
            protected Void doInBackground(Void... voids) {
                weightDao.insert(weight);
                return null;
            }
        }.execute();
    }

    void delWeight(final Weight weight) {
        new AsyncTask<Void, Void, Void>() {
            @Nullable
            @Override
            protected Void doInBackground(Void... voids) {
                weightDao.delete(weight);
                return null;
            }
        }.execute();

        recycle.add(weight);
    }

    @NonNull
    WeightsRecycleBinViewModel getRecycle() {
        return recycle;
    }
}
