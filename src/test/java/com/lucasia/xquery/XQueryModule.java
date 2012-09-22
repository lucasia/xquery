package com.lucasia.xquery;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
* User: ialucas
*/
public class XQueryModule {

    private QName qName;

    private List<String> methods = new ArrayList<String>();

    public XQueryModule(QName qName) {
        this.qName = qName;
    }

    public QName getQName() {
        return qName;
    }

    public List<String> getMethods() {
        return Collections.unmodifiableList(methods);
    }

    public void addMethod(final String method) {
        methods.add(method);
    }


}
