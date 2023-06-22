package com.example.p0003_internet;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<Joke> joke = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isError = new MutableLiveData<>();
    private MutableLiveData<Boolean> isPunchlineVisible = new MutableLiveData<>(true);

    private CompositeDisposable compositeDisposable = new CompositeDisposable();



    public MutableLiveData<Joke> getJoke() {
        return joke;
    }
    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public MutableLiveData<Boolean> getIsError() {
        return isError;
    }
    public MutableLiveData<Boolean> getIsPunchlineVisible() {
        return isPunchlineVisible;
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
    }


    public void loadJoke() {
        Disposable disposable = loadJokeRx()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Throwable {
                        isError.setValue(false);
                        isLoading.setValue(true);
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        isError.setValue(true);
                        Log.d("TAG", "there is a problem");
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Throwable {
                        isLoading.setValue(false);
                    }
                })
                .subscribe(new Consumer<Joke>() {
                               @Override
                               public void accept(Joke newJoke) throws Throwable {
                                   joke.setValue(newJoke);
                               }
                           }
                        , new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                Log.d("tag_me", "problem :" + throwable.getMessage());
                            }
                        }
                );
        compositeDisposable.add(disposable);
    }

    public Single<Joke> loadJokeRx() {
        return ApiFactory.getApiService().loadJoke();
    }

    public void changeIs() {
        if(isPunchlineVisible.getValue()) isPunchlineVisible.setValue(false);
        else isPunchlineVisible.setValue(true);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
