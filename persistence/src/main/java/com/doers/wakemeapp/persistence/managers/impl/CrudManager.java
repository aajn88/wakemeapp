package com.doers.wakemeapp.persistence.managers.impl;

import android.util.Log;

import com.doers.wakemeapp.common.utils.StringUtils;
import com.doers.wakemeapp.persistence.DatabaseHelper;
import com.doers.wakemeapp.persistence.managers.api.ICrudManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * The CRUD implementation for the Entity
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio A. Jimenez N.</a>
 */
public class CrudManager<Entity, Id> implements ICrudManager<Entity, Id> {

    /** Logs Tag **/
    private static final String TAG_LOG = CrudManager.class.getName();

    /** Entity's DAO **/
    private final Dao<Entity, Id> dao;

    /** Entity's Class **/
    private final Class<Entity> clazz;

    /** The DB helper **/
    private DatabaseHelper helper;

    /**
     * This is the main constructor of the CrudManager
     *
     * @param helper
     *         The DBHelper
     * @param clazz
     *         The entity class that will be managed
     *
     * @throws SQLException
     *         If there's an error creating the Entity's DAO
     */
    public CrudManager(DatabaseHelper helper, Class<Entity> clazz) throws SQLException {
        this.helper = helper;
        this.clazz = clazz;
        this.dao = helper.getDao(clazz);
    }

    /**
     * Creates or Updates the given entity. If the given entity is using auto-generated Id, then
     * this will be loaded into the instance
     *
     * @param entity
     *         Entity instance to be created or updated into the DB
     *
     * @return True if the entity was created/updated. False otherwise
     */
    @Override
    public boolean createOrUpdate(Entity entity) {
        boolean created = false;

        try {
            dao.createOrUpdate(entity);
            created = true;
        } catch (SQLException e) {
            Log.e(TAG_LOG,
                    String.format("An error occurs creating an element of the Entity {%s}", clazz),
                    e);
        }

        return created;
    }

    /**
     * Finds an element of the Entity given its Id
     *
     * @param id
     *         Entity's Id
     *
     * @return If there's a match, the Entity element is returned. Otherwise returns null
     */
    @Override
    public Entity findById(Id id) {
        Entity entity = null;

        try {
            entity = dao.queryForId(id);
        } catch (SQLException e) {
            Log.e(TAG_LOG, StringUtils
                    .format("Error ocurrs finding an element of the Entity {%s} with id {%s}",
                            clazz, id), e);
        }

        return entity;
    }

    /**
     * Returns a list of all stored elements of the Entity in the DB
     *
     * @return List of all stored elements of the Entity in the DB
     */
    @Override
    public List<Entity> all() {
        List<Entity> all = null;
        try {
            all = dao.queryForAll();
        } catch (SQLException e) {
            Log.e(TAG_LOG, StringUtils
                            .format("An error occurs requesting all elements of the entity {%s}", clazz),
                    e);
        }
        return all;
    }

    /**
     * Deletes an Entity from the DB given its Id. Returns the deleted entity
     *
     * @param id
     *         Entity's Id to be deleted
     *
     * @return The deleted Entity element
     */
    @Override
    public Entity deleteById(Id id) {
        Entity entity = findById(id);
        try {
            if (entity != null) {
                dao.deleteById(id);
            }
        } catch (SQLException e) {
            Log.e(TAG_LOG, StringUtils
                    .format("Error occurs deleting an element of the entity {%s} with Id {%s}",
                            clazz, id), e);
        }
        return entity;
    }

    /**
     * This method deletes all Table content
     */
    @Override
    public void deleteAll() {
        try {
            dao.deleteBuilder().delete();
        } catch (SQLException e) {
            Log.e(TAG_LOG,
                    String.format("An error has occurred while cleaning all table %s", this.clazz),
                    e);
        }
    }

    /**
     * Returns the Entity DAO. This should be used to add custom queries
     *
     * @return Entity's DAO
     */
    @Override
    public Dao<Entity, Id> getDao() {
        return dao;
    }
}
