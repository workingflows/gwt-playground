package com.logikas.gwt.sample.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.js.JsType;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.logikas.gwt.sample.client.databinding.PathObserver;
import com.logikas.gwt.sample.client.databinding.listener.OpenPathObserverListener;
import com.logikas.gwt.sample.client.model.Person;
import com.workingflows.js.jquery.client.api.JQueryElement;
import static com.workingflows.js.jquery.client.api.JQueryElement.Static.$;
import com.workingflows.js.jscore.client.api.Console;
import com.workingflows.js.jscore.client.api.Document;
import com.workingflows.js.jscore.client.api.Window;
import com.workingflows.js.jscore.client.api.Function;
import com.workingflows.js.jscore.client.api.JsObject;
import com.workingflows.js.jscore.client.api.core.EventListener;
import com.workingflows.js.jscore.client.api.core.Node;
import com.workingflows.js.jscore.client.api.core.NodeList;
import com.workingflows.js.jscore.client.api.db.IDBDatabase;
import com.workingflows.js.jscore.client.api.db.IDBObjectStore;
import com.workingflows.js.jscore.client.api.db.IDBOpenDBRequest;
import com.workingflows.js.jscore.client.api.db.IDBTransaction;
import com.workingflows.js.jscore.client.api.db.IDBVersionChangeEvent;
import com.workingflows.js.jscore.client.api.html.HTMLBodyElement;
import com.workingflows.js.jscore.client.api.html.HTMLElement;
import com.workingflows.js.jscore.client.api.promise.Promise;
import com.workingflows.js.jscore.client.api.promise.PromiseFn;
import com.workingflows.js.jscore.client.api.promise.PromiseThenFn;
import com.workingflows.js.jscore.client.api.promise.Rejected;
import com.workingflows.js.jscore.client.api.promise.Resolve;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class gwt_sample implements EntryPoint {

    @JsType
    private class Structure implements JsObject{

        private String keyPath;

        public Structure(String keyPath) {
            this.keyPath = keyPath;
        }

        public String getKeyPath() {
            return keyPath;
        }

        public void setKeyPath(String keyPath) {
            this.keyPath = keyPath;
        }
        
    }

    @JsType
    private class IndexOptions implements JsObject {

        private boolean unique;

        public IndexOptions(boolean unique) {
            this.unique = unique;
        }

        public boolean isUnique() {
            return unique;
        }

        public void setUnique(boolean unique) {
            this.unique = unique;
        }
        
        @Override
        public String toString() {
            return "{ \"unique\" : \""+unique+"\"}";
        }
        
        
    }

    private IDBDatabase db;
    
    

    @Override
    public void onModuleLoad() {

        final Console console = Window.Static.get().getConsole();

        //Abriendo la Base de Datos
        final IDBOpenDBRequest req = Window.Static.get().indexedDB().open("kratos", 15);
        req.onsuccess(
                Function.Static.newInstance(new Function<Object, Void>() {
                    @Override
                    public Void f(Object changed) {
                        db = req.result();
                        
                        IDBTransaction trx = db.transaction(new String[]{"person"}, IDBTransaction.READWRITE);
                        IDBObjectStore person = trx.objectStore("person");
                        JSONObject struture = new JSONObject();
                        struture.put("name", new JSONString("Cristian"));
                        struture.put("email", new JSONString("Rinaldi"));
                        
                        console.log(struture.toString());
                        
                        person.add(struture, "5");
                        
                        trx.oncomplete(Function.Static.newInstance(new Function<Object, Void>() {
                            @Override
                            public Void f(Object changed) {
                                console.log("All Person adds");
                                console.log(changed.getClass().getName());
                                return null;
                            }
                        }));
                        
                        trx.onerror(Function.Static.newInstance(new Function<Object, Void>() {
                            @Override
                            public Void f(Object changed) {
                                console.log("Errors ",changed);
                                return null;
                            }
                        }));
                        
                        return null;
                    }
                }));

        //Regenerando estrucutura de datos
        req.onupgradeneeded(Function.Static.newInstance(
                new Function<IDBVersionChangeEvent<IDBOpenDBRequest>, Void>() {
                    @Override
                    public Void f(IDBVersionChangeEvent<IDBOpenDBRequest> event) {
                        console.log("onupgradeneeded");
                        db = event.target().result();

                        if (Arrays.asList(db.objectStoreNames()).contains("person")) {
                            db.deleteObjectStore("person");
                        }

                        JSONObject conf = new JSONObject();
                        //conf.put("keyPath", new JSONString("uuid"));
                        //conf.put("autoIncrement", JSONBoolean.getInstance(true));
                        
                        console.log(conf);
                        
                        JSONObject index = new JSONObject();
                        index.put("unique", JSONBoolean.getInstance(false));
                        
                            IDBObjectStore objectStore = db.createObjectStore("person", conf);

                        console.log(objectStore);
                        
                        GWT.log(objectStore.keyPath());
                        
                        console.log("Terminamos de procesar");
                        
                        index.put("unique", JSONBoolean.getInstance(false));
                        objectStore.createIndex("name", "name", index);
                        index.put("unique", JSONBoolean.getInstance(true));
                        objectStore.createIndex("email", "email", index);
                        
                        console.log(objectStore);
                        
                        console.log("Terminamos!!!");

                        return null;
                    }
                }));

        /*db.onversionchange(Function.Static.newInstance(new Function<Object, Void>() {
         @Override
         public Void f(Object changed) {
         console.log("The version is changed ", changed);
         return null;
         }

         }));*/
        final Person person = new Person();

        final Document doc = Document.Static.get();
        final HTMLBodyElement body = doc.getBody();
        final HTMLElement div = doc.createElement("div");
        final HTMLElement p = doc.createElement("p");
        final HTMLElement input = doc.createElement("input");
        final HTMLElement button = doc.createElement("button");

        button.setInnerText("Clear changes");
        button.addEventListener("click", EventListener.Static.newInstance(new EventListener() {
            @Override
            public void onEvent(JsObject event) {
                NodeList list = doc.querySelectorAll("p[data-change]");
                for (int i = 0; i < list.length(); i++) {
                    Node parent = list.item(i).parentElement();
                    parent.removeChild(list.item(i));
                }
            }
        }));

        final PathObserver<Person, String> observer = PathObserver.Static.create(person, "name");
        input.bind("value", observer);
        final PathObserver<Person, String> observer1 = PathObserver.Static.create(person, "name");

        final String original = observer1.open(PathObserver.Static.listener(new OpenPathObserverListener<Person>() {
            @Override
            public void onOpen(String newValue, String oldValue) {
                $("body").append($("<p>").text("The new Value is: " + newValue).attr("data-change", newValue));
            }
        }), person);

        /*JsObject.Static.get().observe(person, Function.Static.newInstance(new Function<Array, Object>() {
         @Override
         public Object f(Array changed) {
         return null;
         }
         }));*/
        Window.Static.get().getConsole().log("%cWelcome to JSInterop!%c", "font-size:1.5em;color:#4558c9;", "color:#d61a7f;font-size:1em;");

        Window.Static.get().getConsole().log("Definido Observe .... ");

        div.appendChild(p);
        div.appendChild(input);
        body.appendChild(div);
        body.appendChild(button);

        JQueryElement addData = $("<button>");
        addData.text("Add Data").on("click", Function.Static.newInstance(new Function<Object, Void>() {
            @Override
            public Void f(Object changed) {
                
                console.log("Agregando datos ....");
                
                Person p = new Person("7a153e4d-a866-4d50-a6ad-72b1dfff685a", "Cristian Rinaldi", "csrinaldi@gmail.com");
                ArrayList<String> objs = new ArrayList<>();
                objs.add("person");
                String[] arr = new String[objs.size()];
                IDBTransaction tx = db.transaction(objs.toArray(arr), IDBTransaction.READWRITE);

                IDBObjectStore store = tx.objectStore("person");
                //IDBRequest<Object> req = store.add(p);
                
                req.onsuccess(Function.Static.newInstance(new Function<Void, Void>() {
                    @Override
                    public Void f(Void changed) {
                        JQueryElement p = $("p").text("Data Add Success!!!");
                        $("body").append(p);
                        return null;
                    }
                }));

                
                return null;
            }
        }));

        $("body").append(addData);

        /*JQueryElement checked = $("<input type='checkbox' id='c' checked></input>");
         $("body").append(checked);

         final BootstrapSwitchElement b = BootstrapFactory.BootstrapSwitch("#c");
         b.bootstrapSwitch(BootstrapSwitchElement.ONTEXT, "SI");
         b.bootstrapSwitch(BootstrapSwitchElement.OFFTEXT, "NO");
         b.on(BootstrapSwitchElement.initEvent, JS.Function(new Function<Object, Void>() {
         @Override
         public Object f(Array changed) {
         $("body").append($("<p>" + changed + "</p>"));
         return null;
         }
         }));*/ /*JQueryElement checked = $("<input type='checkbox' checked></input>");
         checked.data("on-text", "SI");
         checked.data("off-text", "NO");
         $("body").append(checked);*/ //SwitchElement enabled = BootstrapFactory.SwitchElement("#enabled");
        //enabled.setOnText("SI");
        //enabled.setOffText("NO");
        person.setName("Cristian");
        person.setEmail("csrinaldi@gmail.com");
        person.setName("Cristian Sebastian");

        final Promise p1 = Promise.Static.create(PromiseFn.Static.newInstance(new PromiseFn() {
            @Override
            public void f(Resolve resolve, Rejected rejected) {
                $("body").append($("<p>\"Resolve Promise P1\"</p>"));
                resolve.resolve("Resolve Promise P1");
            }
        }));

        Window.Static.get().getConsole().log("HOLA");

        final Promise p3 = Promise.Static.create(PromiseFn.Static.newInstance(new PromiseFn() {
            @Override
            public void f(Resolve resolve, Rejected rejected) {
                $("body").append($("<p>\"Resolve Promise P3\"</p>"));
                resolve.resolve("Resolve Promise P3");
            }
        }));

        JQueryElement promiseButton = $("<button>Ejecutar Promise</button>");
        $("body").append(promiseButton);
        promiseButton.on("click", Function.Static.newInstance(new Function<Object, Object>() {
            @Override
            public Object f(Object changed) {
                $("body").append($("<p>Launch Promise all with Promise 1 and Promise 3</p>"));
                Promise.Static.get().all(true, p1, p3).then(
                        PromiseThenFn.Static.newInstance(
                                new PromiseThenFn() {
                                    @Override
                                    public Promise f(Object changed) {
                                        $("body").append($("<p>Then in Promise all with Promise 1 and Promise 3, without Errors</p>"));
                                        return null;
                                    }
                                }
                        ),
                        PromiseThenFn.Static.newInstance(
                                new PromiseThenFn() {
                                    @Override
                                    public Promise f(Object changed) {
                                        $("body").append($("<p>Then in Promise all with Promise 1 and Promise 3, with Errors</p>"));
                                        return null;
                                    }
                                }
                        )
                );

                return null;
            }
        }));

        p1.then(
                PromiseThenFn.Static.newInstance(
                        new PromiseThenFn() {
                            @Override
                            public Promise f(final Object changed) {
                                $("body").append($("<p>Resolve P1</p>"));
                                return Promise.Static.create(PromiseFn.Static.newInstance(new PromiseFn() {
                                    @Override
                                    public void f(Resolve resolve, Rejected rejected) {
                                        $("body").append($("<p>" + changed + " > Other Promise" + "</p>"));
                                        resolve.resolve(changed + " > Other Promise");
                                    }
                                }));
                            }
                        }),
                PromiseThenFn.Static.newInstance(
                        new PromiseThenFn() {
                            @Override
                            public Promise f(final Object changed) {
                                $("body").append($("<p>Error with P1</p>"));
                                return Promise.Static.create(PromiseFn.Static.newInstance(new PromiseFn() {
                                    @Override
                                    public void f(Resolve resolve, Rejected rejected) {
                                        $("body").append($("<p>" + changed + " > Other With Error Promise" + "</p>"));
                                        rejected.rejected(changed + " > Other With Error Promise");
                                    }
                                }));
                            }
                        })
        ).then(
                PromiseThenFn.Static.newInstance(
                        new PromiseThenFn() {
                            @Override
                            public Promise f(final Object changed) {
                                return null;
                            }
                        }),
                PromiseThenFn.Static.newInstance(
                        new PromiseThenFn() {
                            @Override
                            public Promise f(Object changed) {
                                return null;
                            }
                        })
        );
    }

    public static native void newJSModule()/*-{
     var module = new $wnd.Logikas.ModuleImpl("m", "1.0.0");
     console.log(module);
     console.log(module.getName());
     }-*/;
}
