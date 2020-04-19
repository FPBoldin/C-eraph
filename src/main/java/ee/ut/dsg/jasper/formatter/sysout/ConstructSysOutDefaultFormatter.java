package ee.ut.dsg.jasper.formatter.sysout;

import ee.ut.dsg.jasper.formatter.ConstructResponseDefaultFormatter;

public class ConstructSysOutDefaultFormatter extends ConstructResponseDefaultFormatter {

    public ConstructSysOutDefaultFormatter(String format, boolean distinct) {
        super(format, distinct);
    }

    @Override
    protected void out(String s) {
        System.out.println(s);
    }
}
