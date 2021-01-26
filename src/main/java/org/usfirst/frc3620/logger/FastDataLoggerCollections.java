package org.usfirst.frc3620.logger;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.IllegalFormatConversionException;
import java.util.List;

public class FastDataLoggerCollections extends FastDataLoggerBase {
    List<Object[]> data = new ArrayList<>();
    List<Double> timestamps = new ArrayList<>();

    @Override
    public String start() {
        int initialSize = (int) (maxLengthInSeconds * ( 1.0 / getInterval() ));
        data = new ArrayList<>(initialSize);
        timestamps = new ArrayList<>(initialSize);

        return super.start();
    }

    @Override
    void logData(double timestamp, Object[] d) {
        timestamps.add(timestamp);
        data.add(d);
    }

    @Override
    void writeData(PrintWriter w) {
        for (int i = 0; i < data.size(); i++) {
            w.print(timestamps.get(i));
            w.print(",");            
            w.format("%.6f", timestamps.get(i));
            Object[] row = data.get(i);
            for (int c = 0; c < row.length; c++) {
                w.print(",");
                Object o = row[c];
                if (o instanceof Number) {
                    if (o instanceof Integer) {
                        w.format("%d", o);
                    } else {
                        w.format("%.6f", o);
                    }
                } else {
                    w.print(o);
                }
            }
            w.println();
        }
    }

}
