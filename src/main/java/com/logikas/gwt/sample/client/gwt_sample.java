package com.logikas.gwt.sample.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.workingflows.js.jscore.client.JSNI;
import com.workingflows.js.jscore.client.api.Changed;
import com.workingflows.js.jscore.client.api.Console;
import com.workingflows.js.jscore.client.api.Function;
import com.workingflows.js.jscore.client.api.JsObject;
import com.workingflows.js.jscore.client.api.promise.Promise;
import com.workingflows.js.jscore.client.api.promise.PromiseFn;
import com.workingflows.js.jscore.client.api.promise.PromiseThen;
import com.workingflows.js.jscore.client.api.promise.RejectedFn;
import com.workingflows.js.jscore.client.api.promise.ResolveFn;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class gwt_sample implements EntryPoint {
    
    Console console = JSNI.createConsole();
    
    final Timer timer = new Timer() {
      public void run() {
        //TODO
      }
    };
    
    @Override
    public void onModuleLoad() {

        Promise p = new Promise((ResolveFn resolve, RejectedFn rejected) -> {
            timer.schedule(3000);
            resolve.resolve(true);
        });
        
        PromiseThen then = (Object obj) -> {
            console.log("Resolve");
            console.log(obj);
            return null;
        };
        
        PromiseThen error = (Object obj) -> {
            console.log("Rejected");
            console.log(obj);
            return null;
        };
        
        p.then(then).catchException(error);
        
        Person model= new Person("Cristian", "Rinaldi");
        JsObject.observe(model, (Changed<Person>[] changed) -> {
            console.log("Observe:");
            console.log(changed);
            return null;
        });
        
        model.setName("Cristian Sebastian");
        
        
        
        
    }

    public static native void newJSModule()/*-{
     var module = new $wnd.Logikas.ModuleImpl("m", "1.0.0");
     console.log(module);
     console.log(module.getName());
     }-*/;
}
