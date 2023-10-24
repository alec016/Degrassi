package es.degrassi.forge.util;

import org.apache.logging.log4j.Level;

@SuppressWarnings("unused")
public enum LoggingLevel {
  FATAL(Level.FATAL),
  ERROR(Level.ERROR),
  WARN(Level.WARN),
  INFO(Level.INFO),
  DEBUG(Level.DEBUG),
  ALL(Level.ALL);

  private final Level level;
  LoggingLevel(Level level) {
    this.level = level;
  }

  public Level getLevel() {
    return this.level;
  }
}
