import dayjs from 'dayjs'

interface LoggerOptions {
  showTimeStamp?: boolean
  showLevel?: boolean
}

export class Logger {
  public static options: LoggerOptions = {
    showTimeStamp: true,
    showLevel: true,
  }
  public static setOptions(options: LoggerOptions) {
    const origin = Logger.options
    Logger.options = Object.assign(origin, options)
  }
  public static Levels = {
    "TRACE": 0, "DEBUG": 1, "INFO": 2, "WARN": 3,
    "ERROR": 4, "SILENT": 5
  }
  private logMethods = [
    "trace",
    "debug",
    "info",
    "warn",
    "error"
  ];
  debug = Logger.noop;
  trace = Logger.noop;
  info = Logger.noop;
  warn = Logger.noop;
  error = Logger.noop;

  methodFactory: (methodName: string) => (message?: any, ...optionalParams: any[]) => void;
  userLevel: number | null = null;
  defaultLevel: number = Logger.Levels.DEBUG;

  tag: string | null = null

  constructor(name: string | undefined = undefined, factory = Logger.defaultMethodFactory) {
    this.methodFactory = factory;
    this.rebuildLoggerMethod()
  }

  private rebuildLoggerMethod() {
    const level = this.getLevel();

    // Replace the actual methods.
    for (let i = 0; i < this.logMethods.length; i++) {
      const methodName = this.logMethods[i];
      Object.defineProperty(this, methodName, {
        get: () => {
          return i >= level ? this.methodFactory(methodName) : Logger.noop
        }
      })
    }
  }

  getLevel() {
    if (this.userLevel != null) {
      return this.userLevel;
    } else if (this.defaultLevel != null) {
      return this.defaultLevel;
    }
    return Logger.Levels.SILENT
  };

  setLevel(level: string | number) {
    const preLv = this.userLevel
    const newLv = this.normalizeLevel(level)
    if (preLv != newLv) {
      this.userLevel = newLv
      this.rebuildLoggerMethod()
    }
  }

  resetLevel() {
    this.userLevel = null
    this.rebuildLoggerMethod()
  }

  setDefaultLevel(level: string | number) {
    const preLv = this.defaultLevel
    const newLv = this.normalizeLevel(level)
    if (preLv != newLv) {
      this.defaultLevel = newLv
    }
  }

  enableAll() {
    this.setLevel(Logger.Levels.TRACE)
  }

  disableAll() {
    this.setLevel(Logger.Levels.SILENT)
  }

  private normalizeLevel(level: string | number) {
    let realLevel = Logger.Levels.SILENT
    if (typeof level === "string" && Object.getOwnPropertyNames(Logger.Levels).indexOf(level.toUpperCase()) != -1) {
      //@ts-ignore
      realLevel = Logger.levels[level.toUpperCase()]
    }
    if (typeof level === "number" && level >= 0 && level <= Logger.Levels.SILENT) {
      realLevel = level
    }
    return realLevel;
  }

  private static noop = (message?: any, ...optionalParams: any[]) => { }
  private static __bindMethod(obj: any, methodName: string) {
    const method = obj[methodName];
    const args: any[] = []
    if (Logger.options.showLevel) {
      if (methodName == 'log') methodName = 'debug'
      args.unshift(`[${methodName.toUpperCase()}]`)
    }
    if (Logger.options.showTimeStamp) {
      args.unshift(`[${dayjs().format("YYYY-MM-DD HH:mm:ss_SSS")}]`)
    }
    if (typeof method.bind === 'function') {
      return method.bind(obj, ...args);
    } else {
      try {
        return Function.prototype.bind.call(method, obj, ...args);
      } catch (e) {
        // Missing bind shim or IE8 + Modernizr, fallback to wrapping
        return function () {
          const newArgs: any[] = [].slice.call(arguments);
          if (Logger.options.showLevel) {
            if (methodName == 'log') methodName = 'debug'
            newArgs.unshift(`[${methodName.toUpperCase()}]`)
          }
          if (Logger.options.showTimeStamp) {
            newArgs.unshift(`[${dayjs().format("YYYY-MM-DD HH:mm:ss_SSS")}]`)
          }
          return Function.prototype.apply.apply(method, [obj, newArgs]);
        };
      }
    }
  }
  private static __realMethod(methodName: string) {
    if (methodName === 'debug') {
      methodName = 'log';
    }
    if (Object.getOwnPropertyNames(console).indexOf(methodName) != -1) {
      return Logger.__bindMethod(console, methodName);
    } else if (console.log !== undefined) {
      return Logger.__bindMethod(console, 'log');
    } else {
      return Logger.noop;
    }
  }
  public static defaultMethodFactory(methodName: string) {
    return Logger.__realMethod(methodName)
  }
}
const defaultLogger = new Logger();
export default defaultLogger;

