package com.doers.wakemeapp.config;

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

    }

    /**
     * Method that binds the managers' interfaces with their implementation
     */
    private void bindManagers() {

    }
}
