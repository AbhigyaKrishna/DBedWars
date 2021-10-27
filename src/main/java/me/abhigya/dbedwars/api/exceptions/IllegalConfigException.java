package me.abhigya.dbedwars.api.exceptions;

public class IllegalConfigException extends RuntimeException {

  public IllegalConfigException(String key, String argument, String configFile) {
    super("Value for `" + key + "` in '" + configFile + "' cannot be " + argument + "!");
  }
}
