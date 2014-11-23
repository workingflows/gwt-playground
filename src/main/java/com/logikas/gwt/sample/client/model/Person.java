/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logikas.gwt.sample.client.model;

import com.google.gwt.core.client.js.JsExport;
import com.google.gwt.core.client.js.JsNamespace;
import com.google.gwt.core.client.js.JsType;
import com.logikas.gwt.sample.client.model.datatable.Model;
import com.workingflows.js.jscore.client.api.JsObject;

/**
 *
 * @author Cristian Rinaldi
 */
@JsType
@JsNamespace("$wnd.Logikas.Model")
public class Person implements Model, JsObject {

    private String uuid;
    
    private String name;

    private String email;

    @JsExport
    public Person() {
    }

    @JsExport
    public Person(String name) {
        this.name = name;
        this.email = "";
    }

    @JsExport
    public Person(String name, String email, String uuid) {
        this.name = name;
        this.email = email;
        this.uuid = email;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    @Override
    public String toString() {
        return "\"Username\" : \""+name+"\",\"Email\":\""+email+"\"";
    }
}
