package com.ticket.controller;


import com.skip.dao.cidade.CidadeDAOImpl;
import com.skip.dao.model.Cidade;
import com.skip.dao.model.Ticket;
import com.skip.dao.ticket.TicketDAOImpl;
import com.ticket.util.Path;
import com.ticket.util.StandardResponse;
import com.ticket.util.StatusResponse;
import spark.Request;
import spark.Response;
import spark.Route;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Optional;

import static com.ticket.TicketApp.gson;

public class CidadeController {

    static CidadeDAOImpl cidadeDAO = new CidadeDAOImpl();
    static TicketDAOImpl ticketDAO = new TicketDAOImpl();

    public static Route getAllCidades = (Request request, Response response) -> {
        response.type(Path.CONTENT_TYPE_JSON);
        Collection<Cidade> list = cidadeDAO.select(new Cidade());

        System.out.println(request.pathInfo());

        StandardResponse ret = null;
        if(list.size() > 0) {
            response.status(200);

            ret = new StandardResponse(StatusResponse.OK, gson.toJsonTree(list));
        } else {
            response.status(400);
            ret = new StandardResponse(StatusResponse.ERROR, "Empty list!");
        }

        return gson.toJson(ret);
    };

    public static Route getCidadeById = (Request request, Response response) -> {
        response.type(Path.CONTENT_TYPE_JSON);
        StandardResponse ret = null;

        Optional<String> param = Optional.of(request.params("id"));
        if (param.isPresent()) {
            try {
                Long id = Long.valueOf(param.get());

                Optional<Cidade> p = cidadeDAO.getCidadeById(id);

                if (p.isPresent()) {
                    response.status(200);
                    ret = new StandardResponse(StatusResponse.OK, gson.toJsonTree(p.get()));
                } else {
                    response.status(200);
                    ret = new StandardResponse(StatusResponse.ERROR, "Cidade not found.");
                }
            } catch (NumberFormatException e) {
                response.status(400);
                ret = new StandardResponse(StatusResponse.ERROR, "The param must be a number.");
            }
        } else {
            response.status(400);
            ret = new StandardResponse(StatusResponse.ERROR, "The param must be entered.");
        }

        return gson.toJson(ret);
    };

    public static Route deleteCidade = (Request request, Response response) -> {
        response.type(Path.CONTENT_TYPE_JSON);
        StandardResponse ret = null;

        Optional<String> param = Optional.of(request.params("id"));
        if (param.isPresent()) {
            try {
                Long id = Long.valueOf(param.get());

                Optional<Cidade> p = cidadeDAO.getCidadeById(id);

                if (p.isPresent()) {
                    Ticket t = new Ticket();
                    t.setCidadeId(p.get().getId());
                    if(ticketDAO.select(t).isEmpty()) {
                        if (cidadeDAO.delete(p.get().getId())) {
                            response.status(200);
                            ret = new StandardResponse(StatusResponse.OK, "Cidade deleted.");
                        } else {
                            response.status(400);
                            ret = new StandardResponse(StatusResponse.ERROR, "The Cidade has not been deleted.");
                        }
                    } else {
                        response.status(400);
                        ret = new StandardResponse(StatusResponse.ERROR, "The Cidade is inserted in a ticket (" + p.get().getId() + ").");
                    }
                } else {
                    response.status(200);
                    ret = new StandardResponse(StatusResponse.ERROR, "Cidade not found.");
                }
            } catch (NumberFormatException e) {
                response.status(400);
                ret = new StandardResponse(StatusResponse.ERROR, "The param must be a number.");
            }
        } else {
            response.status(400);
            ret = new StandardResponse(StatusResponse.ERROR, "The param must be entered.");
        }

        return gson.toJson(ret);
    };

    public static Route putCidade = (Request request, Response response) -> {
        response.type(Path.CONTENT_TYPE_JSON);
        StandardResponse ret = null;

        Optional<String> param = Optional.of(request.params("id"));
        if (param.isPresent()) {
            try {
                Long id = Long.valueOf(param.get());

                Optional<Cidade> p = cidadeDAO.getCidadeById(id);

                if (p.isPresent()) {
                    Cidade cidade = p.get();
                    cidade.setNome(request.queryParams("nome"));
                    if (cidadeDAO.update(cidade)) {
                        response.status(200);
                        ret = new StandardResponse(StatusResponse.OK, "Cidade updated.");
                    } else {
                        response.status(400);
                        ret = new StandardResponse(StatusResponse.ERROR, "The cidade has not been updated.");
                    }
                } else {
                    response.status(200);
                    ret = new StandardResponse(StatusResponse.ERROR, "Cidade not found.");
                }
            } catch (Exception e) {
                response.status(400);
                ret = new StandardResponse(StatusResponse.ERROR, "The param must be a number.");
            }
        } else {
            response.status(400);
            ret = new StandardResponse(StatusResponse.ERROR, "The param must be entered.");
        }

        return gson.toJson(ret);
    };

    public static Route postCidade = (Request request, Response response) -> {
        response.type(Path.CONTENT_TYPE_JSON);
        StandardResponse ret = null;

        Optional<String> name = Optional.ofNullable(request.queryParams("nome"));
        Optional<String> email = Optional.ofNullable(request.queryParams("email"));
        if (name.isPresent() && email.isPresent()) {
            Cidade consumidor = new Cidade();
            consumidor.setNome(name.get());

            if (cidadeDAO.insert(consumidor)) {
                ret = new StandardResponse(StatusResponse.OK, "Cidade inserted.");
                response.status(201);
            } else {
                response.status(400);
                ret = new StandardResponse(StatusResponse.ERROR, "The cidade has not been inserted.");
            }
        } else {
            response.status(400);
            ret = new StandardResponse(StatusResponse.ERROR, "The params must be entered.");
        }

        return gson.toJson(ret);
    };

}
