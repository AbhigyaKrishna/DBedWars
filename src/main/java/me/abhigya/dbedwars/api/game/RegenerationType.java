package me.abhigya.dbedwars.api.game;

public enum RegenerationType {
  SINGLE_THREADED,
  MULTI_THREADED_ASYNC,
  MULTI_THREADED_SYNC,
  ;

  public boolean isMultiThreaded() {
    switch (this) {
      case SINGLE_THREADED:
        return false;
      case MULTI_THREADED_ASYNC:
      case MULTI_THREADED_SYNC:
        return true;
    }

    return false;
  }

  public boolean isSync() {
    switch (this) {
      case SINGLE_THREADED:
      case MULTI_THREADED_SYNC:
        return true;
      case MULTI_THREADED_ASYNC:
        return false;
    }

    return true;
  }
}
