package com.fulfilment.application.monolith.stores;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fulfilment.application.monolith.common.BusinessException;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Status;
import jakarta.transaction.Synchronization;
import jakarta.transaction.TransactionSynchronizationRegistry;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.List;
import org.jboss.logging.Logger;

@Path("store")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class StoreResource {

  @Inject TransactionSynchronizationRegistry txRegistry;

  @Inject LegacyStoreManagerGateway legacyStoreManagerGateway;

  private static final Logger LOGGER = Logger.getLogger(StoreResource.class.getName());

  @GET
  public List<Store> get() {
    return Store.listAll(Sort.by("name"));
  }

  @GET
  @Path("{id}")
  public Store getSingle(Long id) {
    Store entity = Store.findById(id);
    if (entity == null) {
      throw new NotFoundException("Store with id of " + id + " does not exist.");
    }
    return entity;
  }

  @POST
  @Transactional
  public Response create(Store store) {
    if (store.id != null) {
      throw new BusinessException("Id was invalidly set on request.");
    }

    store.persist();
    txRegistry.registerInterposedSynchronization(new Synchronization() {
      @Override
      public void beforeCompletion() {}

      @Override
      public void afterCompletion(int status) {
        if(status == Status.STATUS_COMMITTED){
          legacyStoreManagerGateway.createStoreOnLegacySystem(store);
        }
      }
    });

    return Response.ok(store).status(201).build();
  }

  @PUT
  @Path("{id}")
  @Transactional
  public Store update(Long id, Store updatedStore) {
    if (updatedStore.name == null) {
      throw new BusinessException("Store Name was not set on request.");
    }

    Store entity = Store.findById(id);

    if (entity == null) {
      throw new NotFoundException("Store with id of " + id + " does not exist.");
    }

    entity.name = updatedStore.name;
    entity.quantityProductsInStock = updatedStore.quantityProductsInStock;

    txRegistry.registerInterposedSynchronization(new Synchronization() {
      @Override
      public void beforeCompletion() {}

      @Override
      public void afterCompletion(int status) {
        if (status == Status.STATUS_COMMITTED) {
          legacyStoreManagerGateway.updateStoreOnLegacySystem(updatedStore);
        }
      }
    });

    return entity;
  }

  @PATCH
  @Path("{id}")
  @Transactional
  public Store patch(Long id, Store updatedStore) {
    if (updatedStore.name == null) {
      throw new BusinessException("Store Name was not set on request.");
    }

    Store entity = Store.findById(id);

    if (entity == null) {
      throw new NotFoundException("Store with id of " + id + " does not exist.");
    }

    if (entity.name != null) {
      entity.name = updatedStore.name;
    }

    if (entity.quantityProductsInStock != 0) {
      entity.quantityProductsInStock = updatedStore.quantityProductsInStock;
    }

    txRegistry.registerInterposedSynchronization(new Synchronization() {
      @Override
      public void beforeCompletion() {}

      @Override
      public void afterCompletion(int status) {
        if (status == Status.STATUS_COMMITTED) {
          legacyStoreManagerGateway.updateStoreOnLegacySystem(updatedStore);
        }
      }
    });

    return entity;
  }

  @DELETE
  @Path("{id}")
  @Transactional
  public Response delete(Long id) {
    Store entity = Store.findById(id);
    if (entity == null) {
      throw new NotFoundException("Store with id of " + id + " does not exist.");
    }
    entity.delete();
    return Response.status(204).build();
  }
}
