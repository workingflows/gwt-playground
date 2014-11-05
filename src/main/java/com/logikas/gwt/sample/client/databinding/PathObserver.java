/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logikas.gwt.sample.client.databinding;

import com.google.gwt.core.client.js.JsType;
import com.logikas.gwt.sample.client.databinding.listener.OpenPathObserverListener;
import com.workingflows.js.jscore.client.api.JsObject;

/**
 * {@link PathObserver} observes a "value-at-a-path" from a given object
 *
 * @author Cristian Rinaldi <a
 * href="mailto:csrinaldi@gmail.com">csrinaldi@gmail.com</a>
 *
 * Logikas
 * @param <T>
 * @param <E>
 *
 */
@JsType(prototype = "PathObserver")
public interface PathObserver<T extends JsObject, E extends Object> extends Observable<T, E> {

    /**
     * PathObserver also exposes a setValue method which attempts to update the
     * underlying value. Setting the value does not affect notification state
     * (in other words, a caller sets the value but does not discardChanges, the
     * changeFn will be notified of the change).
     *
     * @param value
     */
    void setValue(E value);

    public static class Static {

        public static native <T extends JsObject, E extends Object> PathObserver<T, E> create(T obj, E path)/*-{
         return new $wnd.PathObserver(obj, path);
         }-*/;

        public static native <T extends JsObject> OpenPathObserverListener<T> listener(OpenPathObserverListener<T> listener)/*-{
         return function(newValue, oldValue){
         listener.onOpen(newValue, oldValue);
         }
         }-*/;
    }

}
