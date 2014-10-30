package com.logikas.gwt.sample.client;

import com.google.gwt.core.client.EntryPoint;
import com.logikas.gwt.sample.client.databinding.PathObserver;
import com.logikas.gwt.sample.client.databinding.factory.PathObserverFactory;
import com.logikas.gwt.sample.client.databinding.listener.OpenPathObserverListener;
import com.logikas.gwt.sample.client.model.Person;
//import com.workingflows.js.bootstraps.client.SwitchElement;
//import com.workingflows.js.bootstraps.client.factory.BootstrapFactory;
import com.workingflows.js.jquery.client.api.JQueryElement;
import static com.workingflows.js.jquery.client.factory.Factories.$;
import com.workingflows.js.jscore.client.api.Array;
import com.workingflows.js.jscore.client.api.Console;
import com.workingflows.js.jscore.client.api.Document;
import com.workingflows.js.jscore.client.api.Function;
import com.workingflows.js.jscore.client.api.JsObject;
import com.workingflows.js.jscore.client.api.core.EventListener;
import com.workingflows.js.jscore.client.api.core.NodeList;
import com.workingflows.js.jscore.client.api.html.HTMLBodyElement;
import com.workingflows.js.jscore.client.api.html.HTMLElement;
import com.workingflows.js.jscore.client.api.promise.Promise;
import com.workingflows.js.jscore.client.api.promise.PromiseFn;
import com.workingflows.js.jscore.client.api.promise.PromiseThenFn;
import com.workingflows.js.jscore.client.api.promise.Rejected;
import com.workingflows.js.jscore.client.api.promise.Resolve;
import com.workingflows.js.jscore.client.factory.Browser;
import static com.workingflows.js.jscore.client.factory.Browser.getDocument;
import com.workingflows.js.jscore.client.factory.JS;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class gwt_sample implements EntryPoint {

    @Override
    public void onModuleLoad() {

        final Console console = Browser.getWindow().getConsole();
        final Person person = new Person();

        final Document doc = getDocument();
        final HTMLBodyElement body = doc.getBody();
        final HTMLElement div = doc.createElement("div");
        final HTMLElement p = doc.createElement("p");
        final HTMLElement input = doc.createElement("input");
        final HTMLElement button = doc.createElement("button");
        button.setInnerText("Clear changes");
        button.addEventListener("click", JS.createEventListener(new EventListener() {
            @Override
            public void onEvent(JsObject event) {
                NodeList list = doc.querySelectorAll("p[data-change]");
                console.log(list);
                for (int i = 0; i < list.length(); i++) {
                    console.log(list.pop());
                }
            }
        }));

        final PathObserver<Person, String> observer = PathObserverFactory.createPathObserver(person, "name");
        input.bind("value", observer);
        final PathObserver<Person, String> observer1 = PathObserverFactory.createPathObserver(person, "name");
        final String original = observer1.open(PathObserverFactory.createOpenPathObserverListener(new OpenPathObserverListener<Person>() {
            @Override
            public void onOpen(String newValue, String oldValue) {
                HTMLElement p = doc.createElement("P");
                p.setInnerText("The new Value is: " + newValue);
                p.setAttribute("data-change", newValue);
                body.appendChild(p);
            }
        }), person);

         JS.Object.observe(person, JS.Function(new Function<Array, Object>() {
         @Override
         public Object f(Array changed) {
         return null;
         }
         }));

         Browser.getWindow().getConsole().log("%cWelcome to JSInterop!%c", "font-size:1.5em;color:#4558c9;", "color:#d61a7f;font-size:1em;");

         Browser.getWindow().getConsole().log("Definido Observe .... ");

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
        }));*/

        /*JQueryElement checked = $("<input type='checkbox' checked></input>");
         checked.data("on-text", "SI");
         checked.data("off-text", "NO");
         $("body").append(checked);*/
        //SwitchElement enabled = BootstrapFactory.SwitchElement("#enabled");
        //enabled.setOnText("SI");
        //enabled.setOffText("NO");
         
        person.setName("Cristian");
        person.setEmail("csrinaldi@gmail.com");
        person.setName("Cristian Sebastian");

        final Promise p1 = Browser.newPromise(JS.Function(new PromiseFn() {
            @Override
            public void f(Resolve resolve, Rejected rejected) {
                $("body").append($("<p>\"Resolve Promise P1\"</p>"));
                resolve.resolve("Resolve Promise P1");
            }
        }));

        final Promise p3 = Browser.newPromise(JS.Function(new PromiseFn() {
            @Override
            public void f(Resolve resolve, Rejected rejected) {
                $("body").append($("<p>\"Resolve Promise P3\"</p>"));
                resolve.resolve("Resolve Promise P3");
            }
        }));

        JQueryElement promiseButton = $("<button>Ejecutar Promise</button>");
        $("body").append(promiseButton);
        promiseButton.on("click", JS.Function(new Function<Object, Object>() {
            @Override
            public Object f(Object changed) {
                $("body").append($("<p>Launch Promise all with Promise 1 and Promise 3</p>"));
                JS.Promise.all(true, p1, p3).then(
                        JS.Function(
                                new PromiseThenFn() {
                                    @Override
                                    public Promise f(Object changed) {
                                        $("body").append($("<p>Then in Promise all with Promise 1 and Promise 3, without Errors</p>"));
                                        return null;
                                    }
                                }
                        ),
                        JS.Function(
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
                JS.Function(
                        new PromiseThenFn() {
                            @Override
                            public Promise f(final Object changed) {
                                $("body").append($("<p>Resolve P1</p>"));
                                return Browser.newPromise(JS.Function(new PromiseFn() {
                                    @Override
                                    public void f(Resolve resolve, Rejected rejected) {
                                        $("body").append($("<p>" + changed + " > Other Promise" + "</p>"));
                                        resolve.resolve(changed + " > Other Promise");
                                    }
                                }));
                            }
                        }),
                JS.Function(
                        new PromiseThenFn() {
                            @Override
                            public Promise f(final Object changed) {
                                $("body").append($("<p>Error with P1</p>"));
                                return Browser.newPromise(JS.Function(new PromiseFn() {
                                    @Override
                                    public void f(Resolve resolve, Rejected rejected) {
                                        $("body").append($("<p>" + changed + " > Other With Error Promise" + "</p>"));
                                        rejected.rejected(changed + " > Other With Error Promise");
                                    }
                                }));
                            }
                        })
        ).then(
                JS.Function(
                        new PromiseThenFn() {
                            @Override
                            public Promise f(final Object changed) {
                                return null;
                            }
                        }), JS.Function(
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
