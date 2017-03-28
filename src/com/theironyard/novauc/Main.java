package com.theironyard.novauc;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

public class Main {



    public static void main(String[] args){

        HashMap<String, User> users = new HashMap<>();

        Spark.staticFileLocation("templates");


        Spark.init();
        Spark.get(
                "/",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("userName");
                    User user = users.get(name);

                    HashMap m = new HashMap<>();
                    if (user == null) {
                        return new ModelAndView(m, "login.html");
                    }
                    else {
                        return new ModelAndView(user, "home.html");
                    }
                }),
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/login",
                ((request, response) -> {
                    String name = request.queryParams("loginName");
                    User user = users.get(name);
                    if (user == null) {
                        user = new User(name);
                        users.put(name, user);
                    }

                    Session session = request.session();
                    session.attribute("userName", name);

                    response.redirect("/");
                    return "";
                })
        );
        Spark.post(
                "/create-messages",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("userName");
                    User user = users.get(name);
                    if (user == null) {
                        throw new Exception("User is not logged in");
                    }

                    String messagesName = request.queryParams("messagesName");

                    Messages messages = new Messages(messagesName);

                    user.messages.add(messages);

                    response.redirect("/");
                    return "";
                })

        );
        Spark.post(
                "/delete",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("userName");
                    User user = users.get(name);
                    user.messages.remove(Integer.valueOf(request.queryParams("deletemessagesName"))-1);

                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
        "/logout",
            ((request, response) -> {
            Session session = request.session();
            session.invalidate();
            response.redirect("/");
            return "";
            })
            );
        }
    }















