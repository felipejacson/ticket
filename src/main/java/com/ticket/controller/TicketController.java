package com.ticket.controller;


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

public class TicketController {

    static TicketDAOImpl productDAO = new TicketDAOImpl();

    public static Route getAllTicketes = (Request request, Response response) -> {
        response.type(Path.CONTENT_TYPE_JSON);
        Collection<Ticket> list = productDAO.select(new Ticket());

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

    public static Route getTicketById = (Request request, Response response) -> {
        response.type(Path.CONTENT_TYPE_JSON);
        StandardResponse ret = null;

        Optional<String> param = Optional.of(request.params("id"));
        if (param.isPresent()) {
            try {
                Long id = Long.valueOf(param.get());

                Optional<Ticket> p = productDAO.getTicketById(id);

                if (p.isPresent()) {
                    response.status(200);
                    ret = new StandardResponse(StatusResponse.OK, gson.toJsonTree(p.get()));
                } else {
                    response.status(200);
                    ret = new StandardResponse(StatusResponse.ERROR, "Ticket not found.");
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

    public static Route getTicketByCidadeId = (Request request, Response response) -> {
        response.type(Path.CONTENT_TYPE_JSON);
        StandardResponse ret = null;

        Optional<String> param = Optional.of(request.params("id"));
        if (param.isPresent()) {
            try {
                Long id = Long.valueOf(param.get());

                Ticket t = new Ticket();
                t.setCidadeId(id);

                Collection<Ticket> list = productDAO.select(t);

                if(list.size() > 0) {
                    response.status(200);
                    ret = new StandardResponse(StatusResponse.OK, gson.toJsonTree(list));
                } else {
                    response.status(400);
                    ret = new StandardResponse(StatusResponse.ERROR, "Empty list!");
                }
            } catch (NumberFormatException e) {
                response.status(400);
                ret = new StandardResponse(StatusResponse.ERROR, "The param must be a number.");
            }
        }

        return gson.toJson(ret);
    };

    public static Route deleteTicket = (Request request, Response response) -> {
        response.type(Path.CONTENT_TYPE_JSON);
        StandardResponse ret = null;

        Optional<String> param = Optional.of(request.params("id"));
        if (param.isPresent()) {
            try {
                Long id = Long.valueOf(param.get());

                Optional<Ticket> p = productDAO.getTicketById(id);

                if (p.isPresent()) {
                    if (productDAO.delete(p.get().getId())) {
                        response.status(200);
                        ret = new StandardResponse(StatusResponse.OK, "Ticket deleted.");
                    } else {
                        response.status(400);
                        ret = new StandardResponse(StatusResponse.ERROR, "The product has not been deleted.");
                    }
                } else {
                    response.status(200);
                    ret = new StandardResponse(StatusResponse.ERROR, "Ticket not found.");
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

    public static Route putTicket = (Request request, Response response) -> {
        response.type(Path.CONTENT_TYPE_JSON);
        StandardResponse ret = null;

        Optional<String> param = Optional.of(request.params("id"));
        if (param.isPresent()) {
            try {
                Long id = Long.valueOf(param.get());

                Optional<Ticket> p = productDAO.getTicketById(id);

                if (p.isPresent()) {
                    Ticket ticket = p.get();
                    ticket.setNome(request.queryParams("nome"));
                    ticket.setDescricao(request.queryParams("descricao"));
                    ticket.setCidadeId(Long.parseLong(request.queryParams("cidadeId")));
                    ticket.setFornecedorId(Long.parseLong(request.queryParams("fornecedorId")));

                    DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    ticket.setData(sdf.parse(request.queryParams("data")));
                    if (productDAO.update(ticket)) {
                        response.status(200);
                        ret = new StandardResponse(StatusResponse.OK, "Ticket updated.");
                    } else {
                        response.status(400);
                        ret = new StandardResponse(StatusResponse.ERROR, "The product has not been updated.");
                    }
                } else {
                    response.status(200);
                    ret = new StandardResponse(StatusResponse.ERROR, "Ticket not found.");
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

    public static Route postTicket = (Request request, Response response) -> {
        response.type(Path.CONTENT_TYPE_JSON);
        StandardResponse ret = null;

        Optional<String> name = Optional.ofNullable(request.queryParams("nome"));
        Optional<String> descricao = Optional.ofNullable(request.queryParams("descricao"));
        Optional<String> data = Optional.ofNullable(request.queryParams("data"));
        Optional<String> cidadeId = Optional.ofNullable(request.queryParams("cidadeId"));
        Optional<String> fornecedorId = Optional.ofNullable(request.queryParams("fornecedorId"));
        if (name.isPresent() && descricao.isPresent() && data.isPresent() && cidadeId.isPresent() && fornecedorId.isPresent()) {
            Ticket ticket= new Ticket();
            ticket.setNome(name.get());
            ticket.setDescricao(descricao.get());
            ticket.setCidadeId(Long.parseLong(cidadeId.get()));
            ticket.setFornecedorId(Long.parseLong(fornecedorId.get()));

            DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            ticket.setData(sdf.parse(data.get()));
            if (productDAO.insert(ticket)) {
                ret = new StandardResponse(StatusResponse.OK, "Ticket inserted.");
                response.status(201);
            } else {
                response.status(400);
                ret = new StandardResponse(StatusResponse.ERROR, "The product has not been inserted.");
            }
        } else {
            response.status(400);
            ret = new StandardResponse(StatusResponse.ERROR, "The params must be entered.");
        }

        return gson.toJson(ret);
    };

    public static Route getTicketsByCidade = (Request request, Response response) -> {
        response.type(Path.CONTENT_TYPE_JSON);
        StandardResponse ret = null;

        Optional<String> name = Optional.ofNullable(request.queryParams("cidade"));
        Optional<String> descricao = Optional.ofNullable(request.queryParams("descricao"));
        Optional<String> data = Optional.ofNullable(request.queryParams("data"));
        if (name.isPresent() && descricao.isPresent() && data.isPresent()) {
            Ticket ticket= new Ticket();
            ticket.setNome(name.get());
            ticket.setDescricao(descricao.get());

            DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            ticket.setData(sdf.parse(data.get()));
            if (productDAO.insert(ticket)) {
                ret = new StandardResponse(StatusResponse.OK, "Ticket inserted.");
                response.status(201);
            } else {
                response.status(400);
                ret = new StandardResponse(StatusResponse.ERROR, "The product has not been inserted.");
            }
        } else {
            response.status(400);
            ret = new StandardResponse(StatusResponse.ERROR, "The params must be entered.");
        }

        return gson.toJson(ret);
    };

}
