package automatic_logging.app;

import automatic_logging.annotation.Log;

public class LoggingImpl implements Logging {


    @Log
    @Override
    public void calculation(String param1, int param2) {
    }

    @Log
    @Override
    public void calculation2(String param1) {

    }

    @Log
    @Override
    public void calculation3() {

    }
}
