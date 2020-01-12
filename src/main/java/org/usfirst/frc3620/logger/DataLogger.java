package org.usfirst.frc3620.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class DataLogger extends DataLoggerBase {
    PrintWriter w;
    double t0;
    double flushInterval = 2.0;

    public void setFlushInterval(double flushInterval) {
        this.flushInterval = flushInterval;
    }

    @Override
    public void startTimer() {
        t0 = getTimeInSeconds();

        timer = new Timer();
        long interval = Math.max(1, Math.round(intervalInSeconds * 1000));
        logger.info("DataLogger interval = {}ms", interval);
        timer.schedule(new SlowDataLoggerTimerTask(), 0, interval);
    }

    Object[] data;

    class SlowDataLoggerTimerTask extends TimerTask {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SS");
        double tFlushed = getTimeInSeconds();

        @Override
        public void run() {
            if (w == null) {
                synchronized (DataLogger.this) {
                    if (w == null) {
                        setupOutputFile();
                        if (outputFile != null) {
                            openTheFile();
                        }
                    }
                }
            }
            if (w != null) {
            	if (data == null) {
            		data = new Object[namedDataProviders.size()];
            	}
            	
                for (int i = 0; i < data.length; i++) {
                    NamedDataProvider namedDataProvider = namedDataProviders.get(i);
                    try {
                        data[i] = namedDataProvider.iDataLoggerDataProvider.get();
                    } catch (Exception e) {
                    	data[i] = "ERROR";
                    }
                }
                
                double t = getTimeInSeconds();
                Date curDate = new Date();

                w.print(format.format(curDate));
                w.print(',');
                w.format("%.6f", t - t0);

                for (Object v : data) {
                    w.print(',');
                    w.print(v);
                }
                w.println();

                // flush once every couple seconds
                if (t - tFlushed > flushInterval)
                    w.flush();
            }
        }
    }
    
    void openTheFile() {
        try {
            w = new PrintWriter(new FileWriter(outputFile));
            logger.info("Writing dataLogger to {}", outputFile.getAbsolutePath());
            
            writeHeader(w, namedDataProviders, metadata);
            w.flush();

        } catch (IOException e) {
            logger.error("trouble when logging data: {}", e);
        }
    }
}
