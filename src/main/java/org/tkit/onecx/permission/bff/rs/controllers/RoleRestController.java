package org.tkit.onecx.permission.bff.rs.controllers;

import gen.org.tkit.onecx.permission.bff.rs.internal.RoleApiService;
import gen.org.tkit.onecx.permission.bff.rs.internal.model.*;
import gen.org.tkit.onecx.permission.client.api.AssignmentInternalApi;
import gen.org.tkit.onecx.permission.client.api.RoleInternalApi;
import gen.org.tkit.onecx.permission.client.model.Assignment;
import gen.org.tkit.onecx.permission.client.model.AssignmentPageResult;
import gen.org.tkit.onecx.permission.client.model.Role;
import gen.org.tkit.onecx.permission.client.model.RolePageResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.tkit.onecx.permission.bff.rs.mappers.AssignmentMapper;
import org.tkit.onecx.permission.bff.rs.mappers.ExceptionMapper;
import org.tkit.onecx.permission.bff.rs.mappers.RoleMapper;
import org.tkit.quarkus.log.cdi.LogService;

@ApplicationScoped
@Transactional(value = Transactional.TxType.NOT_SUPPORTED)
@LogService
public class RoleRestController implements RoleApiService {

  @RestClient
  @Inject
  RoleInternalApi roleClient;

  @Inject
  RoleMapper mapper;

  @Inject
  ExceptionMapper exceptionMapper;

  @Override
  public Response createRole(CreateRoleRequestDTO createRoleRequestDTO) {
    try (Response response = roleClient
            .createRole(mapper.map(createRoleRequestDTO))) {
      RoleDTO responseDTO = mapper.map(response.readEntity(Role.class));
      return Response.status(response.getStatus()).entity(responseDTO).build();
    }
  }

  @Override
  public Response deleteRole(String id) {
    try (Response response = roleClient.deleteRole(id)) {
      return Response.status(response.getStatus()).build();
    }
  }

  @Override
  public Response getRoleById(String id) {
    try (Response response = roleClient.getRoleById(id)) {
      RoleDTO responseDTO = mapper.map(response.readEntity(Role.class));
      return Response.status(response.getStatus()).entity(responseDTO).build();
    }
  }

  @Override
  public Response searchRoles(RoleSearchCriteriaDTO roleSearchCriteriaDTO) {
    try (Response response = roleClient.searchRoles(mapper.map(roleSearchCriteriaDTO))) {
      RolePageResultDTO responseDTO = mapper.map(response.readEntity(RolePageResult.class));
      return Response.status(response.getStatus()).entity(responseDTO).build();
    }
  }

  @Override
  public Response updateRole(String id, UpdateRoleRequestDTO updateRoleRequestDTO) {
    try (Response response = roleClient.updateRole(id,
            mapper.map(updateRoleRequestDTO))) {
      return Response.status(response.getStatus()).build();
    }
  }

  @ServerExceptionMapper
  public RestResponse<ProblemDetailResponseDTO> constraint(ConstraintViolationException ex) {
    return exceptionMapper.constraint(ex);
  }

  @ServerExceptionMapper
  public Response restException(WebApplicationException ex) {
    return Response.status(ex.getResponse().getStatus()).build();
  }
}
