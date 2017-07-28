package com.movilizer.connector.persistence.entities.listeners;

/**
 * DON'T AUTOWIRE IN LISTENERS. JPA manages his own context and own threading circumstances
 * so adding Singleton beans might end up in hidden problems.
 */