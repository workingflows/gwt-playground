package com.logikas.gwt.sample.client;

import com.google.gwt.core.client.EntryPoint;
import com.logikas.gwt.sample.client.databinding.PathObserver;
import com.logikas.gwt.sample.client.databinding.listener.OpenPathObserverListener;
import com.logikas.gwt.sample.client.model.Person;
//import com.workingflows.js.bootstraps.client.SwitchElement;
//import com.workingflows.js.bootstraps.client.factory.BootstrapFactory;
import com.workingflows.js.jquery.client.api.JQueryElement;
import static com.workingflows.js.jquery.client.factory.Factories.$;
import com.workingflows.js.jscore.client.api.Array;
import com.workingflows.js.jscore.client.api.Console;
import com.workingflows.js.jscore.client.api.Document;
import com.workingflows.js.jscore.client.api.Window;
import com.workingflows.js.jscore.client.api.Function;
import com.workingflows.js.jscore.client.api.JsObject;
import com.workingflows.js.jscore.client.api.core.EventListener;
import com.workingflows.js.jscore.client.api.core.Node;
import com.workingflows.js.jscore.client.api.core.NodeList;
import com.workingflows.js.jscore.client.api.html.HTMLBodyElement;
import com.workingflows.js.jscore.client.api.html.HTMLElement;
import com.workingflows.js.jscore.client.api.promise.Promise;
import com.workingflows.js.jscore.client.api.promise.PromiseFn;
import com.workingflows.js.jscore.client.api.promise.PromiseThenFn;
import com.workingflows.js.jscore.client.api.promise.Rejected;
import com.workingflows.js.jscore.client.api.promise.Resolve;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class gwt_sample implements EntryPoint {

    @Override
    public void onModuleLoad() {

        final Console console = Window.Static.get().getConsole();
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
        
        Window.Static.get().getConsole().log(JsObject.Static.get());

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
