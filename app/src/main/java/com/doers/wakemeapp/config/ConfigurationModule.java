package com.doers.wakemeapp.config;

import com.doers.wakemeapp.business.services.api.IAlarmsService;
import com.doers.wakemeapp.business.services.impl.AlarmsService;
import com.doers.wakemeapp.persistence.managers.api.IAlarmsManager;
import com.doers.wakemeapp.persistence.managers.api.IPlaylistsManager;
import com.doers.wakemeapp.persistence.managers.impl.AlarmsManager;
import com.doers.wakemeapp.persistence.managers.impl.PlaylistsManager;
import com.google.inject.AbstractModule;

/**
 * Configuration module for RoboGuice dependencies
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio A. Jimenez N.</a>
 */
public class ConfigurationModule extends AbstractModule {

    @Override
    protected void configure() {
        bindServices();
        bindManagers();
    }

    /**
     * Method that binds the services' interfaces with their implementation
     */
    private void bindServices() {
        bind(IAlarmsService.class).to(AlarmsService.class);
    }

    /**
     * Method that binds the managers' interfaces with their implementation
     */
    private void bindManagers() {
        bind(IAlarmsManager.class).to(AlarmsManager.class);
        bind(IPlaylistsManager.class).to(PlaylistsManager.class);
    }
}
