    package log;

    public final class Logger
    {
        private static final BoundedLogWindowSource defaultLogSource;
        static {
            defaultLogSource = new BoundedLogWindowSource(100);
        }

        private Logger()
        {
        }

        public static void debug(String strMessage)
        {
            defaultLogSource.append(LogLevel.Debug, strMessage);
        }

        public static void error(String strMessage)
        {
            defaultLogSource.append(LogLevel.Error, strMessage);
        }

        public static BoundedLogWindowSource getDefaultLogSource()
        {
            return defaultLogSource;
        }
    }