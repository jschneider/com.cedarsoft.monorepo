package com.cedarsoft.rest.generator.test.jaxb;

import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.cedarsoft.com/rest/generator/test/BarModel")
public class BarModel {

    private int daInt;
    private String daString;
    private List<String> stringList;
    private List<? extends String> wildStringList;
    private Set<? extends String> set;

    public int getDaInt() {
        return daInt;
    }

    public void setDaInt(int daInt) {
        this.daInt = daInt;
    }

    public String getDaString() {
        return daString;
    }

    public void setDaString(String daString) {
        this.daString = daString;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public List<? extends String> getWildStringList() {
        return wildStringList;
    }

    public void setWildStringList(List<? extends String> wildStringList) {
        this.wildStringList = wildStringList;
    }

    public Set<? extends String> getSet() {
        return set;
    }

    public void setSet(Set<? extends String> set) {
        this.set = set;
    }

}
