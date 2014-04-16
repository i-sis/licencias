package isis.licencias.service;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.hibernate.validator.method.MethodConstraintViolation;
import org.hibernate.validator.method.MethodConstraintViolationException;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<MethodConstraintViolationException> {

    @Override
    public Response toResponse(MethodConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<String, String>();
        for (MethodConstraintViolation<?> methodConstraintViolation : ex.getConstraintViolations()) {
            errors.put(methodConstraintViolation.getParameterName(), methodConstraintViolation.getMessage());
        }
        return Response.status(Status.PRECONDITION_FAILED).entity(errors).build();
    }
}