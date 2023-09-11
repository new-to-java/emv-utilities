package com.bc.api;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.bc.requestResponse.ArqcGenerateRequest;
import com.bc.requestResponse.ArqcValidateRequest;
import com.bc.requestResponse.ArpcGenerateRequest;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


/**
 * API interface implementing cryptogram functions rest API endpoints and methods hosting them
 */

@Path("/EmvUtilities")
//@ApplicationScoped
@RegisterRestClient
public interface CryptogramFunctionsApi {

    /**
     * Method hosting REST API and functions for generating Authorisation Request Cryptogram (ARQC) and
     * Authrorisation Response Cryptogram (ARPC)
     * @return JSON response object containing ARQC and ARPC
     */

    @POST
    @Path("/Cryptogram/Arqc/Generate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response ArqcGenerate(@Valid ArqcGenerateRequest arqcGenerateRequest) throws Exception;

    /**
     * Method hosting REST API and functions for generating Authorisation Response Cryptogram (ARPC)
     * @return JSON response object containing ARQC and ARPC
     */

    @POST
    @Path("/Cryptogram/Arqc/Validate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response ArpcGenerate(@Valid ArpcGenerateRequest arpcGenerateRequest);

    /**
     * Method hosting REST API and functions for validating Authorisation Request Cryptogram (ARQC)
     * and generating Authorisation Response Cryptogram
     * @return JSON response object containing the following:
     * Boolean attribute indicating if ARQC validation was successful
     * ARQC value that was input
     * ARPC value generated, if ARQC validation was successful, else set to null
     */

    @POST
    @Path("/Cryptogram/Arqc/Validate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response ArqcValidate(@Valid ArqcValidateRequest arqcValidateRequest);
}