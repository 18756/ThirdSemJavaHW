package info.kgeorgiy.java.advanced.implementor.basic.interfaces.standard;

import info.kgeorgiy.java.advanced.implementor.basic.interfaces.standard.Descriptor;
import java.lang.String;
import java.lang.Object;
import javax.management.RuntimeOperationsException;

public class DescriptorImpl implements Descriptor {
    public void setFields(String[] e0, Object[] e1) throws RuntimeOperationsException {
    }

    public boolean isValid() throws RuntimeOperationsException {
        return true;
    }

    public boolean equals(Object e0) {
        return true;
    }

    public int hashCode() {
        return 0;
    }

    public Object clone() throws RuntimeOperationsException {
        return null;
    }

    public String[] getFields() {
        return null;
    }

    public Object getFieldValue(String e0) throws RuntimeOperationsException {
        return null;
    }

    public void setField(String e0, Object e1) throws RuntimeOperationsException {
    }

    public String[] getFieldNames() {
        return null;
    }

    public Object[] getFieldValues(String[] e0) {
        return null;
    }

    public void removeField(String e0) {
    }
}