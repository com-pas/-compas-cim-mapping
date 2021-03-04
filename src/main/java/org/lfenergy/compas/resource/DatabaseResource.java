// SPDX-FileCopyrightText: 2020 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0

package org.lfenergy.compas.resource;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.lfenergy.compas.service.BaseXService;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

@OpenAPIDefinition(
    info = @Info(
        title = "Database API",
        version = "0.0.1",
        description = "Using this API, you can do some underlying database actions!"
    )
)
@Path("/database")
public class DatabaseResource {

    private static final Logger LOGGER = Logger.getLogger(DatabaseResource.class);

    /**
     * Hardcoded BaseX choice
     */
    @Inject
    BaseXService service;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String initial() {
        LOGGER.info("initial");
        return service.executeCommand("list");
    }

    @DELETE
    @Path("/{database}")
    public String dropDatabase(@PathParam String database) {
        LOGGER.info("dropDatabase");
        return service.executeCommand("drop db ".concat(database));
    }

    @PUT
    @Path("/{database}")
    public String addDatabase(@PathParam String database, String file) {
        LOGGER.info("addDatabase");
        return service.executeCommand("create db ".concat(database).concat(" ").concat(file));
    }

    @POST
    @Path("/{database}/query/")
    public String query(@PathParam String database, String query) {
        LOGGER.info("query");
        return service.executeQuery(database, query);
    }

    @POST
    @Path("/command")
    public String command(String command) {
        LOGGER.info("command");
        return service.executeCommand(command);
    }
}