package com.doers.wakemeapp.persistence.managers.api;

import com.j256.ormlite.dao.Dao;

import java.util.List;

/**
 * This Interface offers the CRUD methods to manage the database entities.
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio A. Jimenez N.</a>
 */
public interface ICrudManager<Entity, Id> {

    /**
     * Creates or Updates the given entity. If the given entity is using auto-generated Id, then
     * this will be loaded into the instance
     *
     * @param entity
     *         Entity instance to be created or updated into the DB
     *
     * @return True if the entity was created/updated. False otherwise
     */
    boolean createOrUpdate(Entity entity);

    /**
     * Finds an element of the Entity given its Id
     *
     * @param id
     *         Entity's Id
     *
     * @return If there's a match, the Entity element is returned. Otherwise returns null
     */
    Entity findById(Id id);

    /**
     * Returns a list of all stored elements of the Entity in the DB
     *
     * @return List of all stored elements of the Entity in the DB
     */
    List<Entity> all();

    /**
     * Deletes an Entity from the DB given its Id. Returns the deleted entity
     *
     * @param id
     *         Entity's Id to be deleted
     *
     * @return The deleted Entity element
     */
    Entity deleteById(Id id);

    /**
     * This method deletes all Table content
     */
    void deleteAll();

    /**
     * Returns the Entity DAO. This should be used to add custom queries
     *
     * @return Entity's DAO
     */
    Dao<Entity, Id> getDao();

}
