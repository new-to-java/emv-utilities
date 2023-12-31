package com.bc.rest.client;

import com.bc.requestResponse.CvxGenerateRequest;
import com.bc.requestResponse.CvxGenerateResponse;
import com.bc.service.CardVerificationCodesServiceImpl;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Implementation class for CVx functions API
 */
@Path("/EmvUtilities")
public class CvxFunctionsApiImpl {

    @Inject
    CardVerificationCodesServiceImpl cardVerificationCodesServiceImpl;
    /**
     * Method hosting REST API and functions for generating CVx values for a Pan
     * @return JSON response object containing CVx value and the type of CVx
     */

    @POST
    @Path("/Cvx/Generate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generateCvx(@Valid CvxGenerateRequest cvxGenerateRequest) throws Exception {
//        try {
            CvxGenerateResponse cvxGenerateResponse = CardVerificationCodesServiceImpl.generateCvx(cvxGenerateRequest);
            return Response.status(Response.Status.OK).entity(cvxGenerateResponse).build();
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
