/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.logikas.gwt.sample.client.model.datatable;

import com.google.gwt.core.client.js.JsProperty;
import com.google.gwt.core.client.js.JsType;

/**
 *
 * @author Cristian Rinaldi
 */
@JsType
public interface ColumnConfig {
    
    @JsProperty
    void setData(String data);
    
    @JsProperty
    String getData();
    
    @JsProperty
    String getTitle();
    
    @JsProperty
    void setTitle(String title);
    
    
}