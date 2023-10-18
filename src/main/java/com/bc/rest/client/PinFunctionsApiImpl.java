package com.bc.rest.client;

import com.bc.requestResponse.*;
import com.bc.service.PinServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Implementation class for PIN functions API
 * Only IBM 3624 PIN and Offset method is supported at this time
 */
@Path("/EmvUtilities")
@Slf4j
public class PinFunctionsApiImpl {

    @Inject
    PinServiceImpl pinServiceImpl;
    /**
     * Method hosting REST API and functions for generating PIN and PIN Offset
     * @return JSON response object containing PIN and PIN Offset
     */

    @POST
    @Path("/Pin/Generate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generatePin(@Valid PinGenerateRequest pinGenerateRequest) throws Exception {
//        try {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info(objectMapper.writer().writeValueAsString(pinGenerateRequest));
        PinGenerateResponse pinGenerateResponse = PinServiceImpl.generatePin(pinGenerateRequest);
        return Response.status(Response.Status.OK).entity(pinGenerateResponse).build();
//        }
//        catch(Exception e){
//            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
//        }
    }

    /**
     * Method hosting REST API and functions for generating PIN Verification Value
     * @return JSON response object containing PIN and PIN Verification Value
     */

    @POST
    @Path("/Pvv/Generate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generatePvv(@Valid PvvGenerateRequest pvvGenerateRequest) throws Exception {
//        try {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info(objectMapper.writer().writeValueAsString(pvvGenerateRequest));
        PvvGenerateResponse pvvGenerateResponse = PinServiceImpl.generatePvv(pvvGenerateRequest);
        return Response.status(Response.Status.OK).entity(pvvGenerateResponse).build();
//        }
//        catch(Exception e){
//            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
//        }
    }

    /**
     * Method hosting REST API and functions for decrypting a PIN block and deriving clear PIN
     * @return JSON response object containing decrypted PIN block, clear PIN and PIN length
     */

    @POST
    @Path("/Pinblock/Decrypt")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generatePvv(@Valid PinblockDecryptRequest pinblockDecryptRequest) throws Exception {
//        try {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info(objectMapper.writer().writeValueAsString(pinblockDecryptRequest));
        PinblockDecryptResponse pinblockDecryptResponse = PinServiceImpl.decryptPinblock(pinblockDecryptRequest);
        return Response.status(Response.Status.OK).entity(pinblockDecryptResponse).build();
//        }
//        catch(Exception e){
//            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
//        }
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
