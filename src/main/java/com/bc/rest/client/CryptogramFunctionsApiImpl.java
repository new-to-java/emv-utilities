package com.bc.rest.client;

import com.bc.requestResponse.ArqcGenerateResponse;
import com.bc.service.CryptogramServiceImpl;
import io.vertx.core.impl.logging.Logger;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.bc.requestResponse.ArqcGenerateRequest;
/**
 * Implementation class for Cryptogram functions API Interface
 */
@Path("/EmvUtilities")
public class CryptogramFunctionsApiImpl {

    @Inject
    CryptogramServiceImpl cryptogramServiceImpl;
    /**
     * Method hosting REST API and functions for generating Authorisation Request Cryptogram (ARQC) and
     * Authrorisation Response Cryptogram (ARPC)
     * @return JSON response object containing ARQC and ARPC
     */

    @POST
    @Path("/Cryptogram/Arqc/Generate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response ArqcGenerate(@Valid ArqcGenerateRequest arqcGenerateRequest) {
        try {
            ArqcGenerateResponse arqcGenerateResponse = CryptogramServiceImpl.generateArqcAndArpc(arqcGenerateRequest);
            return Response.status(Response.Status.OK).entity(arqcGenerateResponse).build();
        }
        catch(Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /*
     * Pending implementation
     */

    /*
     * Method hosting REST API and functions for generating Authorisation Response Cryptogram (ARPC)
     * @return JSON response object containing ARQC and ARPC
     */
/*
    @POST
    @Path("/Cryptogram/Arpc/Generate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response ArpcGenerate(ArpcGenerateRequest arpcGenerateRequest) {
        return null;
    }

    /**
     * Method hosting REST API and functions for validating Authorisation Request Cryptogram (ARQC)
     * and generating Authorisation Response Cryptogram
     * @return JSON response object containing the following:
     * Boolean attribute indicating if ARQC validation was successful
     * ARQC value that was input
     * ARPC value generated, if ARQC validation was successful, else set to null
     */
/*
    @POST
    @Path("/Cryptogram/Arqc/Validate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response ArqcValidate(ArqcValidateRequest arqcValidateRequest) {
        return null;
    } */
}
