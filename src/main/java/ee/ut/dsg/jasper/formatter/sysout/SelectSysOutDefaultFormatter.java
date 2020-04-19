package ee.ut.dsg.jasper.formatter.sysout;

import ee.ut.dsg.jasper.formatter.SelectResponseDefaultFormatter;

public class SelectSysOutDefaultFormatter extends SelectResponseDefaultFormatter {

    public SelectSysOutDefaultFormatter(String format, boolean distinct) {
        super(format, distinct);
    }

    @Override
    protected void out(String s) {
        System.out.println(s);
    }
}
